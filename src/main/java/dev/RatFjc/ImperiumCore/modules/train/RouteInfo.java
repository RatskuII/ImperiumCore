package dev.RatFjc.ImperiumCore.modules.train;

import dev.RatFjc.ImperiumCore.extras.hooks.TCHook;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Syntax: /route-lookup <routeName>
// Should return a list of destinations. List is empty if the route doesn't exist.
public class RouteInfo implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) {
            TextUtil.sendMessage(sender, "Invalid arguments.");
            return false;
        }
        String routeName = args[0];
        List<String> destinations = TCHook.destinations(routeName);

        TextUtil.sendMessage(sender, "These are the destinations that this route follows:");
        TextUtil.sendMessages(sender, destinations);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) return List.of();
        return TCHook.routeManager.getRouteNames();
    }
}
