package dev.RatFjc.ImperiumCore.modules.train.route;

import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
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

/**
 * Can be used to look up the list of destinations on a route without the need for physical
 * access to a train.
 */
public class RouteInfo implements TabExecutor, CommandInterface {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) return badArguments(sender, "/route-lookup <routeName>");
        String routeName = args[0];
        List<String> destinations = TCHook.destinations(routeName);

        TextUtil.sendMessage(sender, "These are the destinations that this route follows:");
        TextUtil.sendMessage(sender, destinations);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) return List.of();
        return TCHook.routeManager.getRouteNames();
    }
}
