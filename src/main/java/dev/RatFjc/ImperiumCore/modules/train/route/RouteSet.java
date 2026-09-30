package dev.RatFjc.ImperiumCore.modules.train.route;

import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.pathfinding.PathProvider;
import com.bergerkiller.bukkit.tc.pathfinding.PathWorld;
import com.bergerkiller.bukkit.tc.pathfinding.RouteManager;
import dev.RatFjc.ImperiumCore.extras.Streamer;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.extras.hooks.TCHook;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Can be used to update a route without having to physically access the train. Keep in mind that specifying a
 * route name that does not exist will create a new route with the destinations provided. This
 */
public class RouteSet implements TabExecutor, CommandInterface {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // Syntax is /route-set <routeName> <destinations...>
        if (args.length < 2) return badArguments(sender, "/route-set <routeName> <destinations...>");

        final String routeName = args[0];
        final List<String> destinations = new ArrayList<>(Arrays.asList(args).subList(0, args.length + 2));

        TCHook.routeManager.storeRoute(routeName, destinations);
        TextUtil.sendMessage(sender, "Successfully updated the route to use the following destinations:");
        destinations.forEach(destination -> TextUtil.sendMessage(sender, destination));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return TCHook.routeManager.getRouteNames();
        if (args.length >= 2) {
            Collection<PathWorld> pathWorlds;
            if (sender instanceof Player player) {
                World world = player.getWorld();
                pathWorlds = Collections.singleton(TCHook.pathProvider.getWorld(world));
            } else pathWorlds = TCHook.pathProvider.getWorlds();

            List<PathNode> results = new ArrayList<>();
            pathWorlds.forEach(pathWorld -> results.addAll(pathWorld.getNodes()));

            return results.stream().map(PathNode::toString).toList();
        }
        return List.of();
    }
}
