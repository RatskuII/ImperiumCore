package dev.RatFjc.ImperiumCore.modules.bettergod.command;

import dev.RatFjc.ImperiumCore.extras.ExperimentController;
import dev.RatFjc.ImperiumCore.modules.bettergod.InvincibilityBuilder;
import dev.RatFjc.ImperiumCore.modules.bettergod.data.Context;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class GodCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // Syntax: /god <player> [context] [duration]
        // Note that the duration is currently in seconds only

        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                TextUtil.sendMessage(sender, "You need to specify a player.");
                return false;
            }
            if (!ExperimentController.areExperimentsAllowed(player)) {
                TextUtil.sendMessage(player, "To use this command, turn on experimental features.");
                TextUtil.sendMessage(player, "/allow-experiments");
            }

            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .setInvincible()
                    .message("You are now invincible!")
                    .build();

            return true;
        }

        if (args.length == 1) {
            if (!sender.isOp()) {
                TextUtil.sendMessage(sender, "No permission.");
                return false;
            }
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                TextUtil.sendMessage(sender, "The player specified does not exist.");
                return false;
            }
            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .setInvincible()
                    .build();
            TextUtil.sendMessage(sender, "You have set player " + player.getName() + " invincible");
            return true;
        }

        if (args.length == 2) {
            if (!sender.isOp()) {
                TextUtil.sendMessage(sender, "No permission");
                return false;
            }
            Player player = Bukkit.getPlayer(args[0]);
            Context context = Context.fromString(args[1]);
            if (player == null) {
                TextUtil.sendMessage(sender, "The player specified does not exist.");
                return false;
            }
            if (context == null) {
                TextUtil.sendMessage(sender, "The context provided was invalid.");
                return false;
            }
            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .setInvincible(context)
                    .message("You have set player " + player.getName() + " invincible with context: " + context.name())
                    .build();
            return true;
        }

        if (args.length == 3) {
            if (!sender.isOp()) {
                TextUtil.sendMessage(sender, "No permission.");
                return false;
            }
            Player player = Bukkit.getPlayer(args[0]);
            Context context = Context.fromString(args[1]);
            Duration duration;

            if (player == null) {
                TextUtil.sendMessage(sender, "The player specified does not exist.");
                return false;
            }
            if (context == null) {
                TextUtil.sendMessage(sender, "The context provided was invalid.");
                return false;
            }
            try {
                duration = Duration.ofSeconds(Long.parseLong(args[2]));
            } catch (NumberFormatException e) {
                TextUtil.sendMessage(sender, "The duration provided was invalid. Expected a number.");
                return false;
            }
            InvincibilityBuilder builder = new InvincibilityBuilder(player);
            builder
                    .setInvincible(context)
                    .setDuration(duration)
                    .build();
            TextUtil.sendMessage(sender, "You have set player " + player.getName() + " invincible with context " + context.name() + " for " + args[2] + " seconds");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) return List.of();
        if (args.length == 1) return PlayerUtil.getOnlinePlayerList();
        if (args.length == 2) return Arrays.stream(Context.values())
                .map(Enum::name)
                .toList();
        if (args.length == 3) return List.of("10", "30", "45", "60", "120", "240");
        return List.of();
    }
}
