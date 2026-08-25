package dev.RatFjc.ImperiumCore.extras.hooks;

import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.pathfinding.RouteManager;

import java.util.List;

public class TCHook {

    private static final TrainCarts trainCarts = TrainCarts.plugin;

    public static RouteManager routeManager = trainCarts.getRouteManager();

    public static List<String> destinations(String route) {
        if (route == null) return List.of();
        return routeManager.findRoute(route);
    }
}
