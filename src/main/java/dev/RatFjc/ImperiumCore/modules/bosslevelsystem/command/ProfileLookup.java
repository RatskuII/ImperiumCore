package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.command;

import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.BossProfile;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.stats.BossLevel;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
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
            if (bossProfile.getLevel() == BossLevel.GODLIKE) {
                TextUtil.sendMessage(player, "You have " + bossProfile.xp() + " boss XP, and you have reached the maximum level.");
                return true;
            }
            TextUtil.sendMessage(player, "You have " + bossProfile.xp() + " boss XP.", "You need " + bossProfile.nextLevelUp() + " XP to reach the next level.");
            return true;
        }
        if (args.length == 1) {
            BossProfile bossProfile = new BossProfile(args[0]);
            Player player = bossProfile.asPlayer();
            if (player == null) {
                TextUtil.sendMessage(sender, "The player provided is offline or does not exist.");
                return false;
            }

            if (bossProfile.getLevel() == BossLevel.GODLIKE) {
                TextUtil.sendMessage(sender, player.getName() + " has " + bossProfile.xp() + " boss XP, and has reached the maximum level.");
                return true;
            }
            TextUtil.sendMessage(sender,  player.getName() + " has " + bossProfile.xp() + " boss XP and needs " + bossProfile.nextLevelUp() + " XP to reach the next level.");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return PlayerUtil.getOnlinePlayerList();
        return List.of();
    }
}
