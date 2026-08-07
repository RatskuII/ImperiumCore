package dev.RatFjc.ImperiumCore.modules.ultrabans.command;

import dev.RatFjc.ImperiumCore.modules.ultrabans.PunishmentBuilder;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentSource;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentType;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Tag;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class BanCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            TextUtil.sendMessage(sender, "No permission.");
            return false;
        }

        // Syntax /ban <player> <reason> <duration> [tags]

        Player player;
        String reason;
        Duration duration;
        Collection<Tag> tags;

        if (args.length == 3) {
           player = Bukkit.getPlayer(args[0]);
           reason = args[1];
           duration = DataUtil.parseDuration(args[2]);

            if (player != null) {
                PunishmentBuilder<Player> builder = new PunishmentBuilder<>();
                builder
                        .target(player)
                        .type(PunishmentType.BAN)
                        .source(PunishmentSource.PLAYER)
                        .reason(reason)
                        .duration(duration)
                        .build();
            } else {
                PunishmentBuilder<String> builder = new PunishmentBuilder<>();
                builder
                        .target(args[0])
                        .type(PunishmentType.BAN)
                        .source(PunishmentSource.PLAYER)
                        .reason(reason)
                        .duration(duration)
                        .build();
            }
            TextUtil.announce(args[0] + " was banned by " + sender.getName() + " for: " + reason);
        }
        if (args.length == 4) {
            player = Bukkit.getPlayer(args[0]);
            reason = args[1];
            duration = DataUtil.parseDuration(args[2]);
            tags = Tag.parse(args[3]);

            if (player != null) {
                PunishmentBuilder<Player> builder = new PunishmentBuilder<>();
                builder
                        .target(player)
                        .type(PunishmentType.BAN)
                        .source(PunishmentSource.PLAYER)
                        .reason(reason)
                        .duration(duration)
                        .tags(tags)
                        .build();
            } else {
                PunishmentBuilder<String> builder = new PunishmentBuilder<>();
                builder
                        .target(args[0])
                        .type(PunishmentType.BAN)
                        .source(PunishmentSource.PLAYER)
                        .reason(reason)
                        .duration(duration)
                        .tags(tags)
                        .build();
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
