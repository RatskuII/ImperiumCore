package dev.RatFjc.ImperiumCore.modules.mailbox;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class MailboxGUI implements Listener {

    private final MailDatabase db;
    private final FileConfiguration cfg;
    private final FileConfiguration messages;
    private final FileConfiguration guiCfg;
    private final ImperiumCore plugin;

    private final Map<UUID, String> viewSort = new HashMap<>();
    private final Map<UUID, Integer> currentPage = new HashMap<>();

    private static final int GUI_SIZE = 54;
    private final int PAGE_SIZE;
    private final int MAX_PAGES;

    public MailboxGUI(
            MailDatabase db,
            ImperiumCore plugin,
            FileConfiguration cfg,
            FileConfiguration messages,
            FileConfiguration guiCfg
    ) {
        this.db = db;
        this.cfg = cfg;
        this.messages = messages;
        this.guiCfg = guiCfg;
        this.plugin = plugin;

        this.PAGE_SIZE = cfg.getInt("Mailbox.mail.page-size", 45);
        this.MAX_PAGES = cfg.getInt("Mailbox.mail.max-pages", 50);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private enum SortingMode {
        NEWEST,
        OLDEST,
        SENDER;

        public SortingMode next() {
            return switch (this) {
                case NEWEST -> OLDEST;
                case OLDEST -> SENDER;
                case SENDER -> NEWEST;
            };
        }
    }

    /**
     * Stores the mailbox owner directly inside the inventory.
     * This means clicks do NOT depend on maps that can be cleared
     * by InventoryCloseEvent.
     */
    private static class MailboxHolder implements InventoryHolder {

        private final UUID mailboxOwner;
        private final boolean adminView;
        private final int page;
        private final String sortMode;

        private Inventory inventory;

        public MailboxHolder(
                UUID mailboxOwner,
                boolean adminView,
                int page,
                String sortMode
        ) {
            this.mailboxOwner = mailboxOwner;
            this.adminView = adminView;
            this.page = page;
            this.sortMode = sortMode;
        }

        public UUID getMailboxOwner() {
            return mailboxOwner;
        }

        public boolean isAdminView() {
            return adminView;
        }

        public int getPage() {
            return page;
        }

        public String getSortMode() {
            return sortMode;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }


    public void open(Player p, int page) {

        String sortMode = viewSort.getOrDefault(p.getUniqueId(), "newest");

        open(p, page, sortMode);
    }


    /**
     * Opens a mailbox with a specific page and sort.
     */
    public void open(Player p, int page, String sortMode) {

        if (page < 1) {
            page = 1;
        }

        if (page > MAX_PAGES) {
            page = MAX_PAGES;
        }

        if (sortMode == null) {
            sortMode = "newest";
        }

        UUID mailboxOwner = p.getUniqueId();

        /*
         * Keep sort preference for future opens,
         * but DO NOT rely on currentPage for GUI navigation.
         */
        viewSort.put(p.getUniqueId(), sortMode);

        MailboxHolder holder = new MailboxHolder(mailboxOwner, false, page, sortMode);

        String title = color(guiCfg.getString("gui.title", "&8Mailbox") + " Page " + page);

        Inventory inv = Bukkit.createInventory(holder, GUI_SIZE, title);

        holder.setInventory(inv);

        fillMailbox(inv, mailboxOwner, page, sortMode);

        p.openInventory(inv);
    }


    public void openForAdmin(Player admin, UUID targetUUID, int page) {

        if (page < 1) {
            page = 1;
        }

        if (page > MAX_PAGES) {
            page = MAX_PAGES;
        }

        UUID adminUUID = admin.getUniqueId();

        String sortMode = viewSort.getOrDefault(adminUUID, "newest");

        if (sortMode == null) {
            sortMode = "newest";
        }

        viewSort.put(adminUUID, sortMode);

        /*
         * IMPORTANT:
         *
         * Page and sort are now stored directly
         * in the inventory holder.
         */
        MailboxHolder holder = new MailboxHolder(targetUUID, true, page, sortMode);

        String baseTitle = guiCfg.getString("gui.title", "&8Mailbox");

        String viewedName = Bukkit.getOfflinePlayer(targetUUID).getName();

        if (viewedName == null) {
            viewedName = targetUUID.toString();
        }

        String title = color(baseTitle + " &7(Viewing: &e" + viewedName + "&7) Page " + page);

        Inventory inv = Bukkit.createInventory(holder, GUI_SIZE, title);

        holder.setInventory(inv);

        fillMailbox(inv, targetUUID, page, sortMode);

        admin.openInventory(inv);
    }


    /**
     * Fills the GUI with the mailbox belonging to mailboxOwner.
     */
    private void fillMailbox(Inventory inv, UUID mailboxOwner, int page, String sortMode) {

        List<MailDatabase.Record> records = db.loadPage(mailboxOwner.toString(), page, sortMode);

        for (
                int i = 0;
                i < records.size() && i < PAGE_SIZE;
                i++
        ) {

            MailDatabase.Record r = records.get(i);

            ItemStack item = r.item.clone();

            ItemMeta meta = item.getItemMeta();

            if (meta != null) {

                List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                String sentLine;

                if (r.sentBy == null) {

                    sentLine = cfg.getString("Mailbox.lore.system-sent", "&7Sent by: &eSystem");

                } else if ("admin".equalsIgnoreCase(r.sentBy))
                {
                    sentLine = cfg.getString("Mailbox.lore.admin-sent", "&7Sent by: &cAdmin");

                } else if (r.sentBy.startsWith("plugin:")) {

                    sentLine = cfg.getString("Mailbox.lore.system-sent", "&7Sent by: &e%plugin%").replace("%plugin%", r.sentBy.substring(7));
                } else {
                    sentLine = cfg.getString("Mailbox.lore.player-sent", "&7Sent by: &a%player%").replace("%player%", r.sentBy);
                }

                String dateFmt = cfg.getString("Mailbox.lore.date-format", "yyyy-MM-dd");

                String date = new SimpleDateFormat(dateFmt).format(new Date(r.timestamp));

                lore.add(color(sentLine));
                lore.add(color("&7Date: &f" + date));

                meta.setLore(lore);

                item.setItemMeta(meta);
            }

            inv.setItem(i, item);
        }

        inv.setItem(guiCfg.getInt("gui.previous-page.slot", 45),
                createNav(Material.valueOf(guiCfg.getString("gui.previous-page.material", "ARROW")),
                        guiCfg.getString("gui.previous-page.name", "&cPrevious Page")));

        inv.setItem(guiCfg.getInt("gui.claim-all-button.slot", 46),
                createNav(Material.valueOf(guiCfg.getString("gui.claim-all-button.material", "CHEST")),
                        guiCfg.getString("gui.claim-all-button.name", "&aClaim All")));

        inv.setItem(guiCfg.getInt("gui.sorting-button.slot", 47),
                createNav(Material.valueOf(guiCfg.getString("gui.sorting-button.material", "PAPER")),
                        guiCfg.getString("gui.sorting-button.names." + sortMode, "&aSort: Newest")));

        inv.setItem(guiCfg.getInt("gui.page-indicator.slot", 49),
                createNav(Material.valueOf(guiCfg.getString("gui.page-indicator.material", "PAPER")),
                        guiCfg.getString("gui.page-indicator.name", "&7Page: " + page + "/" + MAX_PAGES)));

        inv.setItem(guiCfg.getInt("gui.next-page.slot", 53),
                createNav(Material.valueOf(guiCfg.getString("gui.next-page.material", "ARROW")),
                        guiCfg.getString("gui.next-page.name", "&aNext Page")));
    }

    private ItemStack createNav(Material mat, String name) {

        ItemStack it = new ItemStack(mat);

        ItemMeta mm = it.getItemMeta();

        if (mm != null) {
            mm.setDisplayName(color(name));
            it.setItemMeta(mm);
        }

        return it;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private void playSound(Player p, String soundName) {

        if (soundName == null || soundName.isBlank()) {
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase(Locale.ROOT));

            p.playSound(p.getLocation(), sound, SoundCategory.MASTER, 1.0f, 1.0f);

        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid mailbox sound: " + soundName);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        Inventory top = e.getView().getTopInventory();

        if (!(top.getHolder() instanceof MailboxHolder holder)) {
            return;
        }
        e.setCancelled(true);

        /*
         * Get EVERYTHING from the GUI itself.
         *
         * This is the important fix.
         */
        UUID mailboxOwner = holder.getMailboxOwner();

        boolean isAdminView = holder.isAdminView();

        int page = holder.getPage();

        String sortMode = holder.getSortMode();

        int raw = e.getRawSlot();

        /*
         * Ignore clicks outside the top inventory.
         */
        if (raw < 0 || raw >= top.getSize()) {
            return;
        }


        /*
         * =========================
         * PREVIOUS PAGE
         * =========================
         */
        if (raw == guiCfg.getInt("gui.previous-page.slot", 45)) {

            int newPage = Math.max(1, page - 1);

            if (isAdminView) {

                /*
                 * mailboxOwner remains the
                 * viewed player's UUID.
                 */
                openForAdmin(p, mailboxOwner, newPage);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));

            } else {

                open(p, newPage, sortMode);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));
            }

            return;
        }


        /*
         * =========================
         * NEXT PAGE
         * =========================
         */
        if (raw == guiCfg.getInt("gui.next-page.slot", 53)) {

            int newPage = Math.min(MAX_PAGES, page + 1);

            if (isAdminView) {

                openForAdmin(p, mailboxOwner, newPage);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));

            } else {

                open(p, newPage, sortMode);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));
            }

            return;
        }


        /*
         * =========================
         * SORTING
         * =========================
         */
        if (raw == guiCfg.getInt("gui.sorting-button.slot", 47)) {

            SortingMode current;

            try {

                current = SortingMode.valueOf(sortMode.toUpperCase(Locale.ROOT));

            } catch (IllegalArgumentException ex) {

                current = SortingMode.NEWEST;
            }

            SortingMode next = current.next();

            String nextSort = next.name().toLowerCase(Locale.ROOT);

            viewSort.put(p.getUniqueId(), nextSort);

            /*
             * IMPORTANT:
             *
             * Keep the SAME page while changing sort.
             *
             * For admin view, mailboxOwner is still
             * the target player's UUID.
             */
            if (isAdminView) {

                openForAdmin(p, mailboxOwner, page);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));

            } else {

                open(p, page, nextSort);
                playSound(p, cfg.getString("Mailbox.sounds.previous_page", "UI_BOOK_OPEN"));
            }

            return;
        }


        /*
         * =========================
         * CLAIM ALL
         * =========================
         */
        if (raw ==
                guiCfg.getInt("gui.claim-all-button.slot", 46)) {

            claimAll(p, mailboxOwner, page, sortMode, isAdminView);

            return;
        }


        /*
         * =========================
         * INDIVIDUAL MAIL
         * =========================
         */
        if (raw >= 0 && raw < PAGE_SIZE) {

            List<MailDatabase.Record> records = db.loadPage(mailboxOwner.toString(), page, sortMode);

            if (raw >= records.size()) {
                return;
            }

            MailDatabase.Record rec = records.get(raw);

            /*
             * This is the all-or-nothing stack check.
             */
            boolean success = tryGiveAndRemove(p, rec);

            if (!success) {

                p.sendMessage(color(messages.getString(
                        "mailbox.errors.inventory_full",
                        "&cYour inventory does not have enough space for the entire item stack.")));

                playSound(p, cfg.getString(
                        "Mailbox.sounds.claim-failed",
                        "BLOCK_NOTE_BLOCK_BASS"));

                return;
            }

            playSound(p, cfg.getString(
                    "Mailbox.sounds.claim-success",
                    "ENTITY_EXPERIENCE_ORB_PICKUP"));

            p.sendMessage(color(
                    messages.getString(
                            "mailbox.info.claimed",
                            "&aItem claimed.")));

            /*
             * Reopen the SAME mailbox,
             * SAME page and SAME sorting.
             */
            if (isAdminView) {

                openForAdmin(p, mailboxOwner, page);

            } else {

                open(p, page, sortMode);
            }
        }
    }

    private boolean canFitEntireStack(Player p, ItemStack item) {

        if (item == null || item.getType().isAir()) {
            return false;
        }
        int remaining = item.getAmount();
        int maxStackSize = item.getMaxStackSize();

        /*
         * First use space in existing compatible stacks.
         */
        for (ItemStack slot : p.getInventory().getStorageContents()) {

            if (slot == null || slot.getType().isAir()) {
                continue;
            }

            if (!slot.isSimilar(item)) {
                continue;
            }

            int available =
                    Math.min(maxStackSize, slot.getMaxStackSize()) - slot.getAmount();

            if (available > 0) {
                remaining -= available;

                if (remaining <= 0) {
                    return true;
                }
            }
        }

        /*
         * Then use completely empty inventory slots.
         *
         * Each empty slot can hold up to the item's max stack size.
         */
        for (ItemStack slot : p.getInventory().getStorageContents()) {
            if (slot == null || slot.getType().isAir()) {
                remaining -= maxStackSize;

                if (remaining <= 0) {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean tryGiveAndRemove(
            Player p,
            MailDatabase.Record rec
    ) {

        if (rec == null || rec.item == null) {
            return false;
        }

        ItemStack item = rec.item.clone();

        /*
         * IMPORTANT:
         *
         * Check the COMPLETE stack before modifying
         * the player's inventory.
         */
        if (!canFitEntireStack(p, item)) {
            return false;
        }

        /*
         * At this point we know the entire stack fits.
         *
         * addItem() should therefore have no leftovers.
         */
        Map<Integer, ItemStack> leftover =
                p.getInventory().addItem(item);

        /*
         * Safety check.
         *
         * If Bukkit somehow reports leftovers despite our
         * capacity check, DO NOT delete the database record.
         */
        if (!leftover.isEmpty()) {

            /*
             * This should normally never happen because
             * canFitEntireStack() already verified capacity.
             *
             * We deliberately do NOT delete the mail.
             */

            return false;
        }

        /*
         * Only delete the mail AFTER the complete item
         * has successfully entered the player's inventory.
         */
        db.deleteById(rec.id);

        return true;
    }


    public void claimAll(
            Player p,
            UUID mailboxOwner,
            int page,
            String sortMode,
            boolean isAdminView
    ) {

        List<MailDatabase.Record> recs =
                db.loadPage(mailboxOwner.toString(), page, sortMode);

        int claimed = 0;

        for (MailDatabase.Record r : recs) {

            /*
             * Do NOT use addItem() directly here.
             *
             * tryGiveAndRemove() first verifies that the
             * COMPLETE stack fits.
             */
            if (!tryGiveAndRemove(p, r)) {

                playSound(p, cfg.getString("Mailbox.sounds.claim-failed", "BLOCK_NOTE_BLOCK_BASS"));

                p.sendMessage(color(messages.getString("mailbox.errors.inventory_full", "&cYour inventory does not have enough space for the entire item stack.")));

                /*
                 * Stop here.
                 *
                 * The current item remains in the mailbox.
                 * No partial stack was claimed.
                 */
                break;
            }

            claimed++;
        }

        if (claimed > 0) {

            playSound(p, cfg.getString("Mailbox.sounds.claim-success", "ENTITY_EXPERIENCE_ORB_PICKUP"));

            p.sendMessage(color(messages.getString("mailbox.info.claimed", "&aYou claimed %amount% items.").replace("%amount%", String.valueOf(claimed))));

        } else {

            /*
             * If nothing was claimed, do not send the normal
             * "No items" message if the mailbox actually had
             * an item that simply didn't fit.
             */
            if (recs.isEmpty()) {

                p.sendMessage(color(messages.getString("mailbox.info.none", "&7No items to claim.")));
            }
        }

        /*
         * Re-open the same mailbox.
         */
        if (isAdminView) {

            openForAdmin(p, mailboxOwner, page);

        } else {

            open(p, page, sortMode);
        }
    }


    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        if (!(e.getPlayer() instanceof Player p)) {
            return;
        }

        Inventory inventory = e.getView().getTopInventory();

        /*
         * Only handle our mailbox inventories.
         */
        if (!(inventory.getHolder() instanceof MailboxHolder)) {
            return;
        }

        UUID uuid = p.getUniqueId();

        /*
         * The inventory was genuinely closed.
         * Clear the player's temporary GUI state.
         */
        currentPage.remove(uuid);
        viewSort.remove(uuid);
    }

}

