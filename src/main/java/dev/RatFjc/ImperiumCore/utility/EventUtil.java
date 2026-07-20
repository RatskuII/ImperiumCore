package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class EventUtil extends Utility {

    public static void registerEvent(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public static void callEvent(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public static void registerCommand(CommandExecutor executor, String name) {
        PluginCommand pluginCommand = plugin.getCommand(name);
        if (pluginCommand != null) pluginCommand.setExecutor(executor);
    }
}
