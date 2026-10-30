package dev.RatFjc.ImperiumCore.modules.lastdeath.command;

import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Kill commands/death events from other plugins cause the death location to desync. To resolve this, we'll handle the command ourselves.
 */
public class KillCmd implements TabExecutor, CommandInterface, PluginProvider {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) return noPermission(sender);
        if (!(sender instanceof Player)) {
            if (args.length == 0) return playerOnly(sender);
            if (args.length == 1) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) return badArguments(sender, "The player is offline or does not exist.");
                BukkitUtil.dispatchCommand("minecraft:kill" + player.getName());
                return true;
            }
        }
        if (!(sender instanceof Player player)) return playerOnly(sender);
        if (args.length == 0) BukkitUtil.dispatchCommand("minecraft:kill" + player.getName());
        if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) return badArguments(sender, "The player is offline or does not exist.");
            BukkitUtil.dispatchCommand("minecraft:kill" + player.getName());
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return PlayerUtil.getOnlinePlayerList();
        return List.of();
    }

    public static class Suicide extends KillCmd {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
            if (args.length > 0) return badArguments(sender);
            if (!(sender instanceof Player player)) return playerOnly(sender);
            player.setHealth(0);
            return true;
        }
    }


}
