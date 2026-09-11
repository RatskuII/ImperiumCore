package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.command;

import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.BossProfile;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProfileLookup implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                TextUtil.sendMessage(sender, "You need to be a player to run this command.");
                return false;
            }
            BossProfile bossProfile = new BossProfile(player);
            TextUtil.sendMessage(player, "You have " + bossProfile.xp() + " boss XP");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

    public static class Builder {

    }
}
