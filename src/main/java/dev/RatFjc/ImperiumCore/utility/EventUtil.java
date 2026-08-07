package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

    public static void dispatchCommand(CommandSender sender, String command) {
        Bukkit.dispatchCommand(sender, command);
    }

    public static void dispatchCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
