package dev.RatFjc.ImperiumCore.modules.invisframe.command;

import dev.RatFjc.ImperiumCore.modules.invisframe.Frame;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetFrameCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            TextUtil.sendMessage(sender, "You do not have permission to run this command.");
            return false;
        }
        // Syntax: /frame <player> <amount> [glow]
        if (args.length == 2) {
            Player player = Bukkit.getPlayer(args[0]);
            int amount = Integer.parseInt(args[1]);

            if (player == null) {
                TextUtil.sendMessage(sender, "The player provided is invalid or does not exist.");
                return false;
            }

            ItemStack result = new Frame()
                    .name("Invisible Item Frame")
                    .enchant()
                    .flags(ItemFlag.HIDE_ENCHANTS)
                    .amount(amount)
                    .build();
            player.give(result);
            return true;
        }
        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[0]);
            int amount = Integer.parseInt(args[1]);
            boolean glow = args[2].equalsIgnoreCase("glow");

            if (player == null) {
                TextUtil.sendMessage(sender, "The player provided is invalid or does not exist.");
                return false;
            }

            Frame frame = new Frame();
            if (glow) frame.glow();

            ItemStack result = frame.name("Invisible Item Frame")
                    .enchant().amount(amount)
                    .flags(ItemFlag.HIDE_ENCHANTS)
                    .build();
            player.give(result);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
