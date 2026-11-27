package dev.RatFjc.ImperiumCore.modules.regions.config;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.extras.Reloadable;
import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.init.Regions;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import dev.RatFjc.ImperiumCore.modules.regions.hardcoded.Region0;
import dev.RatFjc.ImperiumCore.modules.regions.hardcoded.Region1;
import dev.RatFjc.ImperiumCore.modules.regions.hardcoded.Region2;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a configuration file where custom zones can be defined. Zones are technically allowed to overlap
 * but this is not recommended.
 */
public class RegionBuilder extends ConfigurationSaver implements Reloadable {

    private static final File file = new File(plugin.getDataFolder(), "regions.yml");
    private static FileConfiguration config = build(file);

    @Override
    public File file() {
        return file;
    }

    @Override
    protected void set() {
        if (config != null) config.setComments("regions.", List.of("This file controls available region zones.", "Don't modify this unless you know what you're doing."));
        regionEntry(new Region0());
        regionEntry(new Region1());
        regionEntry(new Region2());
    }

    @Override
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the specified region into the file
     * @param region The region to be saved
     * @return Whether the operation was successful
     */
    public static synchronized boolean regionEntry(Region region) {
        if (config == null) {
            nullFail("Configuration unavailable");
            return false;
        }
        RegionProperty property = region.properties();

        String path = "regions." + property.name() + ".";

        config.set(path + "world", region.world().getName());
        config.set(path + "lowerBound", region.bounds().key());
        config.set(path + "upperBound", region.bounds().value());
        config.set(path + "uuid", region.uuid().toString());

        config.set(path + "mobSpawnRate", property.mobSpawnRate());
        config.set(path + "mobScale", property.mobScale());
        config.set(path + "rainCheckRate", property.rainCheckRate());
        config.set(path + "unnaturalMobs", property.unnaturalMobs());
        config.set(path + "mobStrength", property.mobStrength());
        config.set(path + "localDiffOffset", property.localDiffOffset());
        config.set(path + "acidity", property.acidity());

        Boolean result = DataUtil.getOrNull(save(file, config, null), false);
        return result != null && result;
    }

    /**
     * Retrieves a region from the file, specified by name
     * @param name The name of the region to specify
     * @return The saved region, or null if no region by this name exists
     */
    public static synchronized @Nullable Region fromFile(String name) {
        String path = "regions." + name + ".";
        if (config == null) {
            nullFail("Could not retrieve region: config unavailable");
            return null;
        }
        if (config.getConfigurationSection(path) == null) return null;

        String worldName = config.getString(path + "world", "world");
        World world = Bukkit.getWorld(worldName);

        int lowerBound = config.getInt(path + "lowerBound");
        int upperBound = config.getInt(path + "upperBound");
        Pair<Integer, Integer> bounds = new Pair<>(lowerBound, upperBound);

        UUID uuid = DataUtil.stringToUUID(config.getString(path + "uuid"));
        if (uuid == null) return null;

        long mobSpawnRate = config.getLong(path + "mobSpawnRate");
        float mobScale = (float) config.getDouble(path + "mobScale");
        float rainCheckRate = (float) config.getDouble(path + "rainCheckRate");
        boolean unnaturalMobs = config.getBoolean(path + "unnaturalMobs");
        float mobStrength = (float) config.getDouble(path + "mobStrength");
        double localDiffOffset = config.getDouble(path + "localDiffOffset");
        float acidity = (float) config.getDouble(path + "acidity");

        RegionProperty property = new RegionProperty(name, mobSpawnRate, mobScale, rainCheckRate, unnaturalMobs, mobStrength, localDiffOffset, acidity, null);
        // Index is null for now cause I haven't made it yet lol

        return Region.of(world, property, bounds, uuid);
    }

    /**
     * Loads all regions saved in the file.
     * @return A future that contains all regions saved in the file
     */
    public static synchronized CompletableFuture<List<Region>> loadAll() {
        if (config == null) return nullFail("Could not load all regions in file: config unavailable");

        // root
        String path = "regions.";

        return CompletableFuture.supplyAsync(() -> {
            List<Region> out = new ArrayList<>();
            ConfigurationSection names = config.getConfigurationSection(path);
            if (names == null) return out;

            // regions/name
            for (String name : names.getKeys(false)) {
                ConfigurationSection properties = names.getConfigurationSection(name);
                if (properties == null) continue;

                // regions/name/properties
                World world = regionWorld(properties);
                RegionProperty regionProperty = properties(name, properties);
                Pair<Integer, Integer> bounds = bounds(properties);
                UUID uuid = uuidFromRegion(properties);

                Region result = Region.of(world, regionProperty, bounds, uuid);
                out.add(result);
            }
            return out;
        }, Regions.executor());
    }

    /**
     * Retrieves a region from the file, specified by location
     * @param location The location to match
     * @return A saved region that contains these coordinates, or null if no match was found
     */
    public static synchronized @Nullable Region fromPosition(Location location)  {
        if (location == null) return null;
        String path = "regions."; // root
        if (config == null) {
            nullFail("Could not retrieve region via position: config unavailable");
            return null;
        }
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return null;

        // regions/name
        for (String name : section.getKeys(false)) {
            ConfigurationSection properties = section.getConfigurationSection(name);
            if (properties == null) continue;
            // regions/name/property

            World world = Bukkit.getWorld(properties.getString("world", "world"));
            int lower = properties.getInt("lowerBound");
            int upper = properties.getInt("upperBound");

            // Make sure to resolve negative values
            int x = Math.abs(location.getBlockX());
            int z = Math.abs(location.getBlockZ());

            // make sure this is the region we want before retrieving more data
            if (location.getWorld().equals(world) && x <= upper && z <= upper && x >= lower && z >= lower) {
                RegionProperty regionProperty = properties(name, properties);
                Pair<Integer, Integer> bounds = new Pair<>(lower, upper);
                UUID uuid = uuidFromRegion(properties);
                return Region.of(world, regionProperty, bounds, uuid);
            }

        }
        return null;
    }

    // SHORTCUT METHODS
    // Make sure you are in path: /regions/name/ before calling these methods

    private static @Nullable World regionWorld(ConfigurationSection properties) {
        return Bukkit.getWorld(properties.getString("world", "world"));
    }

    private static RegionProperty properties(String name, ConfigurationSection properties) {
        long mobSpawnRate = properties.getLong("mobSpawnRate");
        float mobScale = (float) properties.getDouble("mobScale");
        float rainCheckRate = (float) properties.getDouble("rainCheckRate");
        boolean unnaturalMobs = properties.getBoolean("unnaturalMobs");
        float mobStrength = (float) properties.getDouble("mobStrength");
        double localDiffOffset = properties.getDouble("localDiffOffset");
        float acidity = (float) properties.getDouble("acidity");

        return new RegionProperty(name, mobSpawnRate, mobScale, rainCheckRate, unnaturalMobs, mobStrength, localDiffOffset, acidity, null);
    }

    private static Pair<Integer, Integer> bounds(ConfigurationSection properties) {
        int lower = properties.getInt("lowerBound");
        int upper = properties.getInt("upperBound");
        return new Pair<>(lower, upper);
    }

    public static @Nullable UUID uuidFromRegion(ConfigurationSection properties) {
        return DataUtil.stringToUUID(properties.getString("uuid"));
    }


    // UNNATURAL MOBS LIST

    public static List<? extends Class<? extends LivingEntity>> mobSelection() {
        String path = "unnaturals.";
        List<String> results = config.getStringList(path);
        if (results.isEmpty()) return List.of();

        return results.stream()
                .map(String::toUpperCase)
                .map(EntityType::valueOf)
                .map(EntityType::getEntityClass)
                .filter(Objects::nonNull)
                .filter(LivingEntity.class::isAssignableFrom)
                .map(obj -> (Class<? extends LivingEntity>) obj)
                .toList();
    }
}
