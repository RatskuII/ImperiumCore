package dev.RatFjc.ImperiumCore.modules.lastdeath.command;

import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.modules.lastdeath.DeathStore;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LastDeathCmd implements TabExecutor, CommandInterface {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return playerOnly(sender);
        if (args.length != 0) return badArguments(sender, "/lastdeath");
        Location location = DeathStore.getLastDeath(player);
        if (location == null) {
            TextUtil.sendMessage(player, "No location to return to.");
            return false;
        }
        player.teleportAsync(location);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
