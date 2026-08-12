package dev.RatFjc.ImperiumCore.modules.playerwarp.command;

import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.WarpManager;
import dev.RatFjc.ImperiumCore.modules.playerwarp.database.DBWarpSaver;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class PWCommand implements TabExecutor {

    private static final String invalid = "The warp provided is invalid or does not exist.";
    private static final String playerOnly = "You must be a player to run this command.";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) return false;

        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                TextUtil.sendMessage(sender, playerOnly);
                return false;
            }

            Warp warp = WarpManager.getWarp(args[0]);
            if (warp == null) {
                TextUtil.sendMessage(player, invalid);
                return false;
            }
            WarpManager.teleport(player, warp);
        }

        if (args.length == 2) {

            // Create pw (2 args allow for "create" arg and pw name. location provided by player pos)
            if (args[0].equals("create")) {
                if (!(sender instanceof Player player)) {
                    TextUtil.sendMessage(sender, playerOnly);
                    return false;
                }
                String name = args[1];
                Location location = player.getLocation();

                WarpManager.create(player, name, location);
                return true;
            }

            // Reset pw location (2 args allow for "reset" arg and pw name. location provided by player pos)
            if (args[0].equals("reset")) {
                if (!(sender instanceof Player player)) {
                    TextUtil.sendMessage(sender, playerOnly);
                    return false;
                }
                String name = args[1];
                Location location = player.getLocation();

                Warp warp;
                try {
                    warp = DBWarpSaver.getWarp(name).get();
                } catch (ExecutionException | InterruptedException e) {
                    TextUtil.sendMessage(player, invalid);
                    return false;
                }
                if (warp == null) {
                    TextUtil.sendMessage(player, invalid);
                    return false;
                }
                WarpManager.reset(warp, location);
            }

            // Remove pw (2 args allow for "remove" arg and pw name)
            if (args[0].equals("remove")) {
                String name = args[1];
                WarpManager.remove(name);
            }
        }

        if (args.length == 3) {

            // Create pw (3 args allow for "create" arg, pw name, and a description)
            if (args[0].equals("create")) {
                if (!(sender instanceof Player player)) {
                    TextUtil.sendMessage(sender, playerOnly);
                    return false;
                }

                String name = args[1];
                String description = args[2];
                Location location = player.getLocation();

                WarpManager.create(player, name, description, location);
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            List<String> strings = new ArrayList<>();
            WarpManager.warps().stream()
                    .filter(Objects::nonNull)
                    .map(Warp::name)
                    .forEach(strings::add);
            strings.add("create");
            strings.add("reset");
            strings.add("remove");
            return strings;
        }

        if (args.length == 2) {
            return WarpManager.warps().stream()
                    .filter(Objects::nonNull)
                    .map(Warp::name)
                    .toList();
        }
        return List.of();
    }
}
