package dev.RatFjc.ImperiumCore.modules.mailbox;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MailCommands implements CommandExecutor, TabCompleter {

    private final ImperiumCore plugin;
    private final MailDatabase db;
    private final FileConfiguration cfg;
    private final FileConfiguration messages;
    private final FileConfiguration guiCfg;
    private final MailboxGUI gui;

    private final Map<UUID, Long> sendCooldowns = new HashMap<>();

    public MailCommands(
            ImperiumCore plugin,
            MailDatabase db,
            FileConfiguration cfg,
            FileConfiguration messages,
            FileConfiguration guiCfg,
            MailboxGUI gui
    ) {
        this.plugin = plugin;
        this.db = db;
        this.cfg = cfg;
        this.messages = messages;
        this.guiCfg = guiCfg;
        this.gui = gui;
    }

    // Ensure the default group has the permission
    private void ensureDefaultGroupHasPermission(String perm) {
        if (!Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            return;
        }

        LuckPerms lp = LuckPermsProvider.get();
        GroupManager gm = lp.getGroupManager();

        gm.loadGroup("default").thenAccept(opt -> {
            if (!opt.isPresent()) {
                return;
            }

            Group group = opt.get();

            Collection<Node> nodes = group.data().toCollection();

            boolean hasNode = nodes.stream()
                    .anyMatch(n -> n.getKey().equalsIgnoreCase(perm));

            if (hasNode) {
                return;
            }

            Node node = Node.builder(perm).value(true).build();
            group.data().add(node);

            gm.saveGroup(group);
        });
    }

    private boolean isNumber(String s) {
        if (s == null) {
            return false;
        }

        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private int getSendCooldownSeconds() {
        return cfg.getInt("Mailbox.cooldown.send-mail", 20);
    }

    private boolean checkAndUpdateCooldown(Player p) {
        // Admins bypass the sending cooldown.
        if (p.hasPermission("imperium.mail.admin")) {
            return true;
        }

        int cd = getSendCooldownSeconds();
        long now = System.currentTimeMillis();

        long last = sendCooldowns.getOrDefault(
                p.getUniqueId(),
                0L
        );

        long remaining = (last + cd * 1000L) - now;

        if (remaining > 0) {
            int sec = (int) ((remaining + 999) / 1000);

            String message = messages.getString(
                    "mailbox.errors.cooldown",
                    "&cPlease wait %s seconds before sending another mail."
            );

            p.sendMessage(
                    message.replace("%s", String.valueOf(sec))
            );

            return false;
        }

        sendCooldowns.put(p.getUniqueId(), now);
        return true;
    }

    private boolean removeFromPlayerInventory(Player p, ItemStack wanted) {
        return p.getInventory().removeItem(wanted).isEmpty();
    }

    private void restoreToPlayerOrDrop(Player p, ItemStack toRestore) {
        Map<Integer, ItemStack> leftover =
                p.getInventory().addItem(toRestore);

        if (!leftover.isEmpty()) {
            leftover.values().forEach(
                    stack -> p.getWorld().dropItemNaturally(
                            p.getLocation(),
                            stack
                    )
            );
        }
    }

    private int getSendCost() {
        return cfg.getInt("Mailbox.mail.sending-cost", 500);
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command cmd,
            String label,
            String[] args
    ) {
        String commandName = cmd.getName().toLowerCase(Locale.ROOT);

        /*
         * plugin.yml:
         *
         * mail:
         *   aliases: [mailbox, send, gift, rewards, reward]
         *
         * adminmail:
         *   aliases: [adminmailbox, adminsend, admingift, adminrewards, adminreward]
         *
         * Bukkit executes the registered command under its primary
         * command name, so we only need to distinguish mail/adminmail here.
         */

        if (commandName.equals("mail")) {
            return handlePlayerCommand(sender, args);
        }

        if (commandName.equals("adminmail")) {
            return handleAdminCommand(sender, args);
        }

        sender.sendMessage("Unknown command.");
        return true;
    }

    /**
     * /mail
     *
     * Handles:
     * /mail
     * /mail claim
     * /mail claim <amount>
     * /mail reminder
     * /mail <player>
     * /mail <player> <amount>
     * /mail <player> <material> <amount>
     */
    private boolean handlePlayerCommand(
            CommandSender sender,
            String[] args
    ) {
        if (!(sender instanceof Player p)) {
            pOrSenderMessage(
                    sender,
                    messages.getString(
                            "mailbox.errors.unknown_player",
                            "&cOnly players can use the mailbox."
                    )
            );
            return true;
        }

        /*
         * This now matches plugin.yml:
         *
         * imperium.mail.player:
         *   default: true
         */
        if (!p.hasPermission("imperium.mail.player")
                && !p.hasPermission("imperium.mail.admin")) {

            p.sendMessage(
                    messages.getString(
                            "mailbox.errors.no_permission",
                            "&cYou do not have permission to use the mailbox."
                    )
            );

            return true;
        }

        if (args.length == 0) {
            gui.open(p, 1);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        if (sub.equals("reminder")) {
            return toggleReminder(p);
        }

        if (sub.equals("claim")) {
            return handleClaim(p, args);
        }

        return handleSend(p, args);
    }

    private void pOrSenderMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    private boolean handleClaim(Player p, String[] args) {
        int amount = -1;

        if (args.length >= 2 && isNumber(args[1])) {
            amount = Integer.parseInt(args[1]);
        }

        if (amount == -1) {
            gui.claimAll(
                    p,
                    p.getUniqueId(),
                    1,
                    "newest",
                    false
            );

            return true;
        }

        if (amount <= 0) {
            p.sendMessage("&cAmount must be greater than 0.");
            return true;
        }

        int remaining = amount;
        int page = 1;

        int maxPages = cfg.getInt(
                "Mailbox.mail.max-pages",
                50
        );

        while (remaining > 0 && page <= maxPages) {

            List<MailDatabase.Record> recs =
                    db.loadPage(
                            p.getUniqueId().toString(),
                            page,
                            "newest"
                    );

            if (recs.isEmpty()) {
                break;
            }

            for (MailDatabase.Record r : recs) {

                if (remaining <= 0) {
                    break;
                }

                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(
                            messages.getString(
                                    "mailbox.errors.inventory_full",
                                    "&cYour inventory is full."
                            )
                    );

                    return true;
                }

                if (!p.getInventory()
                        .addItem(r.item.clone())
                        .isEmpty()) {

                    p.sendMessage(
                            messages.getString(
                                    "mailbox.errors.inventory_full",
                                    "&cYour inventory is full."
                            )
                    );

                    return true;
                }

                db.deleteById(r.id);
                remaining--;
            }

            page++;
        }

        int claimed = amount - remaining;

        p.sendMessage(messages.getString("mailbox.info.claimed", "&aClaimed %amount% mail item(s).").replace("%amount%", String.valueOf(claimed)));

        return true;
    }

    private boolean handleSend(Player p, String[] args) {

        String targetName = args[0];

        if (targetName.equalsIgnoreCase(p.getName())) {
            p.sendMessage("§cYou cannot mail yourself.");
            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(targetName);

        if (target == null || !target.hasPlayedBefore()) {
            p.sendMessage(
                    messages.getString("mailbox.errors.never_joined", "&cThat player has never joined."
                    )
            );

            return true;
        }

        /*
         * Player sending now uses imperium.mail.player.
         *
         * Admins can also send because imperium.mail.admin
         * bypasses the player permission requirement.
         */
        if (!p.hasPermission("imperium.mail.player")
                && !p.hasPermission("imperium.mail.admin")) {

            p.sendMessage(
                    messages.getString(
                            "mailbox.errors.no_permission",
                            "&cYou do not have permission to send mail."
                    )
            );

            return true;
        }

        if (!checkAndUpdateCooldown(p)) {
            return true;
        }

        ItemStack hand =
                p.getInventory().getItemInMainHand();

        if (hand == null || hand.getType().isAir()) {
            p.sendMessage(
                    messages.getString(
                            "mailbox.errors.no_item_in_hand",
                            "&cYou must be holding an item."
                    )
            );

            return true;
        }

        ItemStack toSend =
                parseSendItem(p, args, hand);

        if (toSend == null) {
            return true;
        }

        int cost = getSendCost();

        boolean requireEngine =
                cfg.getBoolean(
                        "Mailbox.mail.require-coinsengine",
                        false
                );

        if (CoinsEngineHook.isAvailable() && requireEngine) {

            double bal =
                    CoinsEngineHook.getBalance(
                            p.getUniqueId()
                    );

            if (bal < cost
                    && !p.hasPermission("imperium.mail.admin")) {

                p.sendMessage(
                        messages.getString(
                                "mailbox.errors.not_enough_money",
                                "&cYou need %cost% coins."
                        ).replace(
                                "%cost%",
                                String.valueOf(cost)
                        )
                );

                return true;
            }
        }

        if (!removeFromPlayerInventory(p, toSend)) {
            p.sendMessage("§cFailed to remove items.");
            return true;
        }

        if (!db.saveItem(
                target.getUniqueId().toString(),
                p.getName(),
                toSend
        )) {

            restoreToPlayerOrDrop(p, toSend);

            p.sendMessage("§cFailed to store mail.");
            return true;
        }

        notifyNewMail(
                target.getUniqueId(),
                p.getName()
        );

        boolean charged = true;

        if (CoinsEngineHook.isAvailable() && requireEngine) {
            charged = CoinsEngineHook.withdraw(
                    p.getUniqueId(),
                    cost
            );
        }

        if (!charged) {
            p.sendMessage(
                    messages.getString(
                            "mailbox.info.free_send",
                            "&aMail sent for free."
                    )
            );
        } else {
            p.sendMessage(
                    messages.getString(
                            "mailbox.info.sent",
                            "&aMail sent to %player%."
                    ).replace(
                            "%player%",
                            target.getName()
                    )
            );
        }

        return true;
    }

    private ItemStack parseSendItem(
            Player p,
            String[] args,
            ItemStack hand
    ) {
        if (args.length >= 2 && !isNumber(args[1])) {

            Material mat =
                    Material.matchMaterial(args[1]);

            if (mat == null) {
                p.sendMessage(
                        "Unknown material: " + args[1]
                );

                return null;
            }

            int amt =
                    (args.length >= 3 && isNumber(args[2]))
                            ? Integer.parseInt(args[2])
                            : 1;

            amt = Math.max(
                    1,
                    Math.min(
                            amt,
                            mat.getMaxStackSize()
                    )
            );

            if (hand.getType() != mat) {
                p.sendMessage(
                        "§cYou must hold the specified material."
                );

                return null;
            }

            if (hand.getAmount() < amt) {
                p.sendMessage(
                        messages.getString(
                                "mailbox.errors.insufficient_stack",
                                "&cYou do not have %amount% of that item."
                        ).replace(
                                "%amount%",
                                String.valueOf(amt)
                        )
                );

                return null;
            }

            ItemStack clone = hand.clone();
            clone.setAmount(amt);

            return clone;
        }

        int amt =
                (args.length >= 2 && isNumber(args[1]))
                        ? Integer.parseInt(args[1])
                        : hand.getAmount();

        if (hand.getAmount() < amt) {
            p.sendMessage(
                    messages.getString(
                            "mailbox.errors.insufficient_stack",
                            "&cYou do not have %amount% of that item."
                    ).replace(
                            "%amount%",
                            String.valueOf(amt)
                    )
            );

            return null;
        }

        ItemStack clone = hand.clone();
        clone.setAmount(amt);

        return clone;
    }

    /**
     * /adminmail
     *
     * Handles:
     * /adminmail open <player>
     * /adminmail view <player>
     * /adminmail clear <player>
     * /adminmail online <material> [amount]
     * /adminmail all <material> [amount]
     * /adminmail <player> <material> [amount]
     */
    private boolean handleAdminCommand(
            CommandSender sender,
            String[] args
    ) {
        if (!sender.hasPermission("imperium.mail.admin")) {
            sender.sendMessage(
                    messages.getString(
                            "mailbox.errors.no_permission",
                            "&cYou do not have permission to do that."
                    )
            );

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(
                    "Usage: /adminmail <open|view|clear|player|online|all> [item|amount] [amount]"
            );

            return true;
        }

        String sub =
                args[0].toLowerCase(Locale.ROOT);

        switch (sub) {

            case "open":
            case "view": {

                if (args.length < 2
                        || !(sender instanceof Player admin)) {

                    sender.sendMessage(
                            "Console cannot open GUI."
                    );

                    return true;
                }

                OfflinePlayer target =
                        Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(
                            messages.getString(
                                    "mailbox.errors.never_joined",
                                    "&cThat player has never joined."
                            )
                    );

                    return true;
                }

                gui.openForAdmin(
                        admin,
                        target.getUniqueId(),
                        1
                );

                return true;
            }

            case "clear": {

                if (args.length < 2) {
                    sender.sendMessage(
                            "Usage: /adminmail clear <player>"
                    );

                    return true;
                }

                OfflinePlayer target =
                        Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(
                            messages.getString(
                                    "mailbox.errors.never_joined",
                                    "&cThat player has never joined."
                            )
                    );

                    return true;
                }

                db.clearPlayer(
                        target.getUniqueId().toString()
                );

                sender.sendMessage(
                        messages.getString(
                                "mailbox.info.cleared",
                                "&aCleared mail for %player%."
                        ).replace(
                                "%player%",
                                target.getName()
                        )
                );

                return true;
            }

            case "online": {

                ItemStack giveOnline =
                        parseAdminGive(
                                sender,
                                args,
                                1
                        );

                if (giveOnline == null) {
                    return true;
                }

                for (Player p :
                        Bukkit.getOnlinePlayers()) {

                    db.saveItem(
                            p.getUniqueId().toString(),
                            "admin",
                            giveOnline.clone()
                    );

                    notifyNewMail(
                            p.getUniqueId(),
                            "admin"
                    );
                }

                sender.sendMessage(
                        messages.getString(
                                "mailbox.info.rewarded_online",
                                "&aRewarded all online players."
                        )
                );

                return true;
            }

            case "all": {

                ItemStack giveAll =
                        parseAdminGive(
                                sender,
                                args,
                                1
                        );

                if (giveAll == null) {
                    return true;
                }

                for (OfflinePlayer op :
                        Bukkit.getOfflinePlayers()) {

                    if (op.hasPlayedBefore()) {

                        db.saveItem(
                                op.getUniqueId().toString(),
                                "admin",
                                giveAll.clone()
                        );

                        notifyNewMail(
                                op.getUniqueId(),
                                "admin"
                        );
                    }
                }

                sender.sendMessage(
                        messages.getString(
                                "mailbox.info.rewarded_all",
                                "&aRewarded all players."
                        )
                );

                return true;
            }
        }

        /*
         * Everything else is treated as:
         *
         * /adminmail <player> <item> [amount]
         */
        OfflinePlayer target =
                Bukkit.getOfflinePlayer(sub);

        if (!target.hasPlayedBefore()) {
            sender.sendMessage(
                    messages.getString(
                            "mailbox.errors.never_joined",
                            "&cThat player has never joined."
                    )
            );

            return true;
        }

        ItemStack give =
                parseAdminGive(
                        sender,
                        args,
                        1
                );

        if (give == null) {
            return true;
        }

        db.saveItem(
                target.getUniqueId().toString(),
                "admin",
                give
        );

        notifyNewMail(
                target.getUniqueId(),
                "admin"
        );

        sender.sendMessage(
                messages.getString(
                        "mailbox.info.sent",
                        "&aMail sent to %player%."
                ).replace(
                        "%player%",
                        target.getName()
                )
        );

        return true;
    }

    private ItemStack parseAdminGive(
            CommandSender sender,
            String[] args,
            int start
    ) {
        if (args.length <= start) {

            if (!(sender instanceof Player p)) {
                sender.sendMessage(
                        "Console must specify item."
                );

                return null;
            }

            ItemStack hand =
                    p.getInventory().getItemInMainHand();

            if (hand == null || hand.getType().isAir()) {
                p.sendMessage(
                        messages.getString(
                                "mailbox.errors.no_item_in_hand",
                                "&cYou must be holding an item."
                        )
                );

                return null;
            }

            return hand.clone();
        }

        String a = args[start];

        if (isNumber(a)) {

            if (!(sender instanceof Player p)) {
                sender.sendMessage(
                        "Console must specify material with an amount."
                );

                return null;
            }

            ItemStack hand =
                    p.getInventory().getItemInMainHand();

            if (hand == null || hand.getType().isAir()) {
                p.sendMessage(
                        messages.getString(
                                "mailbox.errors.no_item_in_hand",
                                "&cYou must be holding an item."
                        )
                );

                return null;
            }

            int amt = Integer.parseInt(a);

            amt = Math.max(
                    1,
                    Math.min(
                            amt,
                            hand.getType().getMaxStackSize()
                    )
            );

            ItemStack out = hand.clone();
            out.setAmount(amt);

            return out;
        }

        Material mat =
                Material.matchMaterial(a);

        if (mat == null) {
            sender.sendMessage(
                    "Unknown material: " + a
            );

            return null;
        }

        int amt = 1;

        if (args.length > start + 1
                && isNumber(args[start + 1])) {

            amt = Integer.parseInt(
                    args[start + 1]
            );
        }

        amt = Math.max(
                1,
                Math.min(
                        amt,
                        mat.getMaxStackSize()
                )
        );

        return new ItemStack(mat, amt);
    }

    private boolean toggleReminder(Player p) {
        String perm = "imperium.mail.reminder";

        if (Bukkit.getPluginManager()
                .isPluginEnabled("LuckPerms")) {

            try {
                LuckPerms lp =
                        LuckPermsProvider.get();

                net.luckperms.api.model.user.User user =
                        lp.getUserManager()
                                .getUser(p.getUniqueId());

                if (user == null) {
                    p.sendMessage(
                            "§cLuckPerms error: no user data."
                    );

                    return true;
                }

                boolean hasDisabledNode =
                        user.data()
                                .toCollection()
                                .stream()
                                .anyMatch(
                                        n -> n.getKey()
                                                .equalsIgnoreCase(perm)
                                                && !n.getValue()
                                );

                if (hasDisabledNode) {

                    user.data()
                            .toCollection()
                            .stream()
                            .filter(
                                    n -> n.getKey()
                                            .equalsIgnoreCase(perm)
                            )
                            .forEach(
                                    user.data()::remove
                            );

                    p.sendMessage(
                            "§aMail reminders enabled§e."
                    );

                } else {

                    Node disable =
                            Node.builder(perm)
                                    .value(false)
                                    .build();

                    user.data().add(disable);

                    p.sendMessage(
                            "§eMail reminders §cdisabled§e."
                    );
                }

                lp.getUserManager()
                        .saveUser(user);

                return true;

            } catch (Exception ex) {

                p.sendMessage(
                        "§cFailed to toggle reminder via LuckPerms."
                );

                ex.printStackTrace();

                return true;
            }
        }

        // Fallback if LuckPerms is not present.
        boolean enabled = p.hasPermission(perm);

        p.addAttachment(
                plugin,
                perm,
                !enabled
        );

        p.sendMessage(
                enabled
                        ? "§eMail reminders §cdisabled§e."
                        : "§aMail reminders §aenabled§e."
        );

        return true;
    }

    /**
     * Notify the target player if online.
     *
     * Respects imperium.mail.reminder.
     */
    private void notifyNewMail(
            UUID targetUUID,
            String senderName
    ) {
        Player p =
                Bukkit.getPlayer(targetUUID);

        if (p == null) {
            return;
        }

        if (!p.hasPermission(
                "imperium.mail.reminder"
        )) {
            return;
        }

        String msg;

        if (senderName.equalsIgnoreCase("admin")) {

            msg = messages.getString(
                    "mailbox.info.received",
                    "&aYou received new mail!"
            );

        } else {

            msg = messages.getString(
                    "mailbox.info.received_player",
                    "&aYou received new mail from %sender%!"
            ).replace(
                    "%sender%",
                    senderName
            );
        }

        if (msg != null) {
            p.sendMessage(msg);
        }

        playMailSound(p);
    }

    private void playMailSound(Player p) {
        String key =
                "Mailbox.sounds.recived";

        String soundName =
                cfg.getString(
                        key,
                        "ENTITY_EXPERIENCE_ORB_PICKUP"
                );

        if (soundName == null
                || soundName.isEmpty()) {
            return;
        }

        try {

            NamespacedKey keyName =
                    NamespacedKey.minecraft(
                            soundName.toLowerCase(
                                    Locale.ROOT
                            )
                    );

            Sound sound =
                    Registry.SOUNDS.get(keyName);

            if (sound != null) {
                p.playSound(
                        p.getLocation(),
                        sound,
                        1f,
                        1f
                );
            }

        } catch (Exception ignored) {
            // Invalid sound name in config.
        }
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command cmd,
            String label,
            String[] args
    ) {
        List<String> out =
                new ArrayList<>();

        String name =
                cmd.getName().toLowerCase(
                        Locale.ROOT
                );

        /*
         * /adminmail
         */
        if (name.equals("adminmail")) {

            if (!sender.hasPermission(
                    "imperium.mail.admin"
            )) {
                return out;
            }

            if (args.length == 1) {

                out.addAll(
                        Arrays.asList(
                                "open",
                                "view",
                                "clear",
                                "all",
                                "online"
                        )
                );

                Bukkit.getOnlinePlayers()
                        .forEach(
                                p -> out.add(
                                        p.getName()
                                )
                        );
            }

            if (args.length == 2) {

                String sub =
                        args[0].toLowerCase(
                                Locale.ROOT
                        );

                if (sub.equals("open")
                        || sub.equals("view")
                        || sub.equals("clear")) {

                    Bukkit.getOnlinePlayers()
                            .forEach(
                                    p -> out.add(
                                            p.getName()
                                    )
                            );
                }
            }

            return filter(out, args);
        }

        /*
         * /mail
         */
        if (name.equals("mail")) {

            if (!sender.hasPermission(
                    "imperium.mail.player"
            ) && !sender.hasPermission(
                    "imperium.mail.admin"
            )) {
                return out;
            }

            if (args.length == 1) {

                out.add("claim");
                out.add("reminder");

                Bukkit.getOnlinePlayers()
                        .forEach(
                                p -> out.add(
                                        p.getName()
                                )
                        );
            }

            if (args.length == 2
                    && args[0].equalsIgnoreCase(
                    "claim"
            )) {

                out.add("<amount>");
            }

            return filter(out, args);
        }

        return out;
    }

    private List<String> filter(
            List<String> list,
            String[] args
    ) {
        if (args.length == 0) {
            return list;
        }

        String last =
                args[args.length - 1]
                        .toLowerCase(Locale.ROOT);

        return list.stream()
                .filter(
                        s -> s.toLowerCase(
                                Locale.ROOT
                        ).startsWith(last)
                )
                .collect(
                        Collectors.toList()
                );
    }
}