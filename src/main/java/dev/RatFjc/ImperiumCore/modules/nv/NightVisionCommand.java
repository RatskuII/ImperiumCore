package dev.RatFjc.ImperiumCore.modules.nv;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NightVisionCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "Only players can run this command.");
            return false;
        }

        if (args.length > 1) {
            TextUtil.sendMessage(player, "Invalid arguments.");
            return false;
        }

        if (args.length == 0) {
            boolean nv = NVController.hasNightVision(player);
            NVController.setNightVision(player, !nv);

            return true;
        }
        String toggle = args[0];
        switch (toggle) {
            case "on" -> {
                NVController.setNightVision(player, true);
                return true;
            }
            case "off" -> {
                NVController.setNightVision(player, false);
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("on", "off");
        else return List.of();
    }
}
