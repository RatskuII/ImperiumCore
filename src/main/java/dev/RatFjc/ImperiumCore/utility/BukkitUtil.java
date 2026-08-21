package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BukkitUtil extends Utility {

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

    public static @Nullable World getWorld(String worldName) {
        if (worldName == null) return null;
        return Bukkit.getWorld(worldName);
    }

    @SuppressWarnings("all")
    public static @Nullable Biome getBiome(String value) {
        if (value == null) return null;
        RegistryAccess registry = RegistryAccess.registryAccess();
        Registry<Biome> biomeRegistry = registry.getRegistry(RegistryKey.BIOME);
        String input = "minecraft:" + value.toUpperCase();
        Key biomeKey = Key.key(input);
        return biomeRegistry.get(biomeKey);
    }
}
