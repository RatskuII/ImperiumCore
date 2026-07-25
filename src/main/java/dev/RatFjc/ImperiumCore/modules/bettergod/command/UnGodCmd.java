package dev.RatFjc.ImperiumCore.modules.bettergod.command;

import dev.RatFjc.ImperiumCore.modules.bettergod.InvincibilityBuilder;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnGodCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                TextUtil.sendMessage(sender, "You must specify a player.");
                return false;
            }

            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .removeInvincibility()
                    .message("You are no longer invincible!")
                    .build();
            return true;
        }

        if (args.length == 1) {
            if (!sender.isOp()) {
                TextUtil.sendMessage(sender, "No permission.");
            }
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                TextUtil.sendMessage(sender, "The player specified does not exist.");
                return false;
            }

            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .removeInvincibility()
                    .build();
            TextUtil.sendMessage(sender, "Invincibility removed from player " + player.getName());
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return PlayerUtil.getOnlinePlayerList();
        else return List.of();
    }
}
