package dev.RatFjc.ImperiumCore.modules.hexsigns;

import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModernSignCommand extends AbstractTagHolder implements TabExecutor, CommandInterface {

    private static final String signPerm = "imperium.modernsign.edit";
    private static final String syntax = "/modernsign <set/clear> <number> <text>";

    private final Map<Integer, String> signText = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return playerOnly(sender);

        if (!player.hasPermission(signPerm)) return noPermission(sender);
        if (args.length < 3) return badArguments(sender, syntax);

        final Block block = player.getTargetBlock(Set.of(
                Material.AIR,
                Material.WATER
        ), 16);
        if (!DataUtil.equalsAny(block.getType(),
                Material.OAK_SIGN,
                Material.BIRCH_SIGN,
                Material.DARK_OAK_SIGN,
                Material.ACACIA_SIGN,
                Material.CHERRY_SIGN,
                Material.PALE_OAK_SIGN,
                Material.MANGROVE_SIGN,
                Material.JUNGLE_SIGN)) {
            TextUtil.sendMessage(sender, "The object selected is not valid.");
            return false;
        }
        BlockState blockState = block.getState(true);
        Sign sign = (Sign) blockState;
        SignSide signSide = sign.getTargetSide(player);

        signText.put(1, TextUtil.fromMiniMessage(signSide.line(1)));
        signText.put(2, TextUtil.fromMiniMessage(signSide.line(2)));
        signText.put(3, TextUtil.fromMiniMessage(signSide.line(3)));
        signText.put(4, TextUtil.fromMiniMessage(signSide.line(4)));

        Pair<Boolean, Boolean> status = new Pair<>(
                args[0].equalsIgnoreCase("set"),
                args[0].equalsIgnoreCase("clear")
        );
        if (!status.key() && !status.value()) return badArguments(sender, "Valid options are [set, clear]", syntax);

        int num = DataUtil.parseInt(args[1]);
        if (num == 0) return badArguments(sender, syntax);

        if (status.value()) {
            signSide.line(num, Component.empty());
            return true;
        }

        // If the status is anything other than "clear", the plugin will assume "set" was used and proceed as usual

        String out = "";
        for (int i = 2; i < args.length; i++) {
            out = out.concat(" " + args[i]);
        }

        Component text = TextUtil.toMiniMessage(out, resolvers);
        signSide.line(num, text);
        blockState.update();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("set", "clear");
        if (args.length == 2) return List.of("1", "2", "3", "4");
        if (args.length >= 3) {
            int num = DataUtil.parseInt(args[1]);
            if (num == 0) return List.of();
            String result = signText.get(num);
            if (result != null) return List.of(result);
        }
        return List.of();
    }

    //todo Put in checks for claims and other restrictions
}
