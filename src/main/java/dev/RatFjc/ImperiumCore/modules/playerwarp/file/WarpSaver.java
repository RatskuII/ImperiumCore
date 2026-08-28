package dev.RatFjc.ImperiumCore.modules.playerwarp.file;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.init.PlayerWarps;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Rating;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpType;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpUser;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Deprecated
public class WarpSaver extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "warps.yml");

    private static final FileConfiguration fileConfiguration = build(file);

    public static CompletableFuture<Boolean> saveWarp(Warp warp) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");
        return CompletableFuture.supplyAsync(() -> {

            // Structure will be type -> player -> warp name -> warp information
            String type = warp.getWarpType().name();

            String playerName = warp.owner().user().getName();

            String path = "warps." + type + "." + playerName + "." + warp.name();

            fileConfiguration.set(path + ".description", warp.getDescription());

            Location location = warp.location();
            fileConfiguration.set(path + ".location", List.of(
                    String.valueOf(location.x()),
                    String.valueOf(location.y()),
                    String.valueOf(location.z()),
                    location.getWorld().getName()
            ));

            String id = warp.uuid().toString();
            fileConfiguration.set(path + ".uuid", id);

            Rating rating = warp.rating();
            if (rating != null) {
                fileConfiguration.set(path + ".ratingStars", rating.getStars());
                fileConfiguration.set(path + ".ratingReason", rating.getReason());
            }
            save(file, fileConfiguration, PlayerWarps.executor());
            ConfigurationSection section = fileConfiguration.getConfigurationSection(path);
            return section != null;
        }, PlayerWarps.executor());
    }

    public static CompletableFuture<@Nullable Warp> getWarp(String name) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");

        return CompletableFuture.supplyAsync(() -> {
            String path = "warps"; // root path (warps)

            ConfigurationSection types = fileConfiguration.getConfigurationSection(path);
            if (types == null) return null;

            for (String type : types.getKeys(false)) {
                ConfigurationSection players = fileConfiguration.getConfigurationSection(type);
                if (players == null) continue;

                for (String player : players.getKeys(false)) {
                    ConfigurationSection names = fileConfiguration.getConfigurationSection(player);
                    if (names == null) continue;

                    for (String warpName : names.getKeys(false)) {
                        if (!Objects.equals(warpName, name)) continue;

                        ConfigurationSection warpData = fileConfiguration.getConfigurationSection(warpName);
                        if (warpData == null) continue;

                        // Description
                        String description = warpData.getString("description", "");

                        // Location
                        List<String> coordinates = warpData.getStringList("location");
                        World world = Bukkit.getWorld(coordinates.get(3));
                        if (world == null) continue;
                        double x = DataUtil.parseDouble(coordinates.get(0));
                        double y = DataUtil.parseDouble(coordinates.get(1));
                        double z = DataUtil.parseDouble(coordinates.get(0));
                        Location resultLocation = new Location(world, x, y, z);

                        // UUID
                        String id = warpData.getString("uuid", "");
                        UUID uuid = DataUtil.stringToUUID(id);
                        if (uuid == null) uuid = UUID.randomUUID();

                        // Rating
                        Rating rating = null;
                        int star = warpData.getInt("ratingStars", 0);
                        String rateReason = warpData.getString("ratingReason", "");
                        if (star != 0) rating = new Rating(star, rateReason);

                        // Warp type
                        WarpType warpType = WarpType.get(type);

                        WarpUser owner = new WarpUser(player);
                        Warp warp = new Warp(owner, resultLocation, warpName, description, warpType, uuid);
                        warp.setRating(rating);
                        return warp;
                    }
                }
            }
            return null;
        }, PlayerWarps.executor());
    }

    public static CompletableFuture<List<Warp>> getWarps() {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");

        return CompletableFuture.supplyAsync(() -> {
            List<Warp> warps = new ArrayList<>();

            String path = "warps";
            ConfigurationSection root = fileConfiguration.getConfigurationSection(path);
            if (root == null) return List.of();

            for (String type : root.getKeys(false)) {
                ConfigurationSection types = fileConfiguration.getConfigurationSection(type);
                if (types == null) continue;

                for (String playerName : types.getKeys(false)) {
                    ConfigurationSection warpNames = fileConfiguration.getConfigurationSection(playerName);
                    if (warpNames == null) continue;

                    for (String warpName : warpNames.getKeys(false)) {
                        ConfigurationSection warpData = fileConfiguration.getConfigurationSection(warpName);
                        if (warpData == null) continue;

                        // Player
                        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

                        // Description
                        String description = warpData.getString("description", "");

                        // Location
                        List<String> coordinates = warpData.getStringList("location");
                        World world = Bukkit.getWorld(coordinates.get(3));
                        if (world == null) continue;
                        double x = DataUtil.parseDouble(coordinates.get(0));
                        double y = DataUtil.parseDouble(coordinates.get(1));
                        double z = DataUtil.parseDouble(coordinates.get(2));
                        Location resultingLocation = new Location(world, x, y, z);

                        // UUID
                        String id = warpData.getString("uuid");
                        UUID uuid = DataUtil.stringToUUID(id);
                        if (uuid == null) uuid = UUID.randomUUID();

                        // Rating
                        Rating rating = null;
                        int starRating = warpData.getInt("ratingStars");
                        String rateReason = warpData.getString("ratingReason", "");
                        if (starRating != 0)  rating = new Rating(starRating, rateReason);

                        // WarpType
                        WarpType warpType = WarpType.get(type);

                        WarpUser warpUser = new WarpUser(player);
                        Warp warp = new Warp(warpUser, resultingLocation, warpName, description, warpType, uuid);
                        warp.setRating(rating);
                        warps.add(warp);
                    }

                }
            }
            return warps;
        }, PlayerWarps.executor());
    }

    public static CompletableFuture<List<Warp>> getWarps(WarpUser player) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");

        return CompletableFuture.supplyAsync(() -> {
            List<Warp> warps = new ArrayList<>();

            String path = "warps";
            ConfigurationSection root = fileConfiguration.getConfigurationSection(path);
            if (root == null) return List.of();

            for (String type : root.getKeys(false)) {
                ConfigurationSection types = fileConfiguration.getConfigurationSection(type);
                if (types == null) continue;

                for (String playerName : types.getKeys(false)) {
                    if (!playerName.equals(player.user().getName())) continue;

                    ConfigurationSection warpNames = fileConfiguration.getConfigurationSection(playerName);
                    if (warpNames == null) continue;

                    for (String warpName : warpNames.getKeys(false)) {
                        ConfigurationSection warpData = fileConfiguration.getConfigurationSection(warpName);
                        if (warpData == null) continue;

                        // Description
                        String description = warpData.getString("description", "");

                        // Location
                        List<String> coordinates = warpData.getStringList("location");
                        World world = Bukkit.getWorld(coordinates.get(3));
                        if (world == null) continue;
                        double x = DataUtil.parseDouble(coordinates.get(0));
                        double y = DataUtil.parseDouble(coordinates.get(1));
                        double z = DataUtil.parseDouble(coordinates.get(2));
                        Location resultingLocation = new Location(world, x, y, z);

                        // UUID
                        String id = warpData.getString("uuid");
                        UUID uuid = DataUtil.stringToUUID(id);
                        if (uuid == null) uuid = UUID.randomUUID();

                        // Rating
                        Rating rating = null;
                        int starRating = warpData.getInt("ratingStars");
                        String rateReason = warpData.getString("ratingReason", "");
                        if (starRating != 0)  rating = new Rating(starRating, rateReason);

                        // WarpType
                        WarpType warpType = WarpType.get(type);

                        Warp warp = new Warp(player, resultingLocation, warpName, description, warpType, uuid);
                        warp.setRating(rating);
                        warps.add(warp);
                    }

                }
            }
            return warps;
        }, PlayerWarps.executor());
    }

    public static CompletableFuture<List<Warp>> getWarps(WarpUser player, WarpType warpType) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");

        return CompletableFuture.supplyAsync(() -> {
            String type = warpType.name();
            String path = "warps." + type + "." + player.user().getName();

            ConfigurationSection section = fileConfiguration.getConfigurationSection(path);
            if (section == null) return List.of();

            List<Warp> warpList = new ArrayList<>();
            // Warp name (iteration object)
            for (String warpName : section.getKeys(false)) {

                // Description
                String description = section.getString(path + ".description", "");

                // Location
                List<String> coordinates = section.getStringList(path + ".location");
                World world = Bukkit.getWorld(coordinates.get(3));
                if (world == null) continue;
                double x = Double.parseDouble(coordinates.get(0));
                double y = Double.parseDouble(coordinates.get(1));
                double z = Double.parseDouble(coordinates.get(2));
                Location parsedLocation = new Location(world, x, y, z);

                // UUID
                // Will generate a new UUID if the saved one is corrupt
                String id = section.getString(path + ".uuid", "");
                UUID uuid = DataUtil.stringToUUID(id);
                if (uuid == null) uuid = UUID.randomUUID();

                // Rating
                Rating rating = null;
                int starRating = section.getInt(path + ".ratingStars", 0);
                String rateReason = section.getString(path + ".ratingReason", "");
                if (starRating != 0) rating = new Rating(starRating, rateReason);

                Warp warp = new Warp(player, parsedLocation, warpName, description, warpType, uuid);
                warp.setRating(rating);
                warpList.add(warp);
            }
            return warpList;
        }, PlayerWarps.executor());
    }

    public static CompletableFuture<Void> removeWarp(String name) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");

        return CompletableFuture.runAsync(() -> {
            String path = "warps"; // root path (warps)

            ConfigurationSection types = fileConfiguration.getConfigurationSection(path);
            if (types == null) return;

            for (String type : types.getKeys(false)) {
                ConfigurationSection players = fileConfiguration.getConfigurationSection(type);
                if (players == null) continue;

                for (String player : players.getKeys(false)) {
                    ConfigurationSection names = fileConfiguration.getConfigurationSection(player);
                    if (names == null) continue;

                    for (String warpName : names.getKeys(false)) {
                        if (!Objects.equals(warpName, name)) continue;

                        ConfigurationSection warpData = fileConfiguration.getConfigurationSection(warpName);
                        if (warpData == null) continue;

                        fileConfiguration.set(name, null);
                    }
                }
            }
        }, PlayerWarps.executor());
    }

    @Override
    protected void set() {

    }
}
