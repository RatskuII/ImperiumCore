package dev.RatFjc.ImperiumCore.modules.wild;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.hooks.GPHook;
import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.extras.pairing.Trio;
import dev.RatFjc.ImperiumCore.init.Wild;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Rtp extends ConfigurationSaver implements TabExecutor, CommandInterface, PluginProvider {

    private static final File file = new File(plugin.getDataFolder(), "rtp.yml");
    private static final FileConfiguration config = build(file);

    private static final Random random = new Random();

    @Override
    protected void set() {
        if (config == null) {
            nullFail("Config is unavailable.");
            return;
        }
        config.set("exclude", 8000);
        config.set("maximum", 50000);

        save(file, config, null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return playerOnly(sender);
        if (args.length > 2) return badArguments(sender, "/rtp <ignoreExclusion>");

        randomTeleport(player, false, 10);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("ignoreExclusion");
        return List.of();
    }

    public static double exclusion() {
        if (config != null) return config.getDouble("exclude", 0);
        return 0;
    }

    public static double maximum() {
        if (config != null) return config.getDouble("maximum", -1);
        return -1;
    }

    private static Location pickLocation(Player player, double exclusion, double max) {
        World world = player.getWorld();
        if (max <= 0) max = (world.getWorldBorder().getSize() / 2) - 1000;

        if (exclusion < 0) exclusion = 0;
        Pair<Double, Double> exclude = new Pair<>(exclusion, -exclusion);

        double[] doubles = new double[2];
        for (int i = 0; i < doubles.length; i++) {
            double next = random.nextDouble(-max, max);
            if (next <= exclude.key()) next = random.nextDouble(-max, max);
            if (next >= exclude.value()) next = random.nextDouble(-max, max);
            doubles[i] = next;
        }

        double x = doubles[0], z = doubles[1], y = world.getHighestBlockYAt((int) x, (int) z) + 1;
        return new Location(world, x, y, z);
    }

    protected static CompletableFuture<Location> pickLocationAsync(Player player, double exclusion, double max) {
        final World world = player.getWorld();

        if (max <= 0) max = (world.getWorldBorder().getSize() / 2) - 1000;
        final double finalMax = max;

        if (exclusion < 0) exclusion = 0;
        final Pair<Double, Double> excludeZone = new Pair<>(-exclusion, exclusion);

        return CompletableFuture.supplyAsync(() -> {
            double x = random.nextDouble(
                    Math.min(-finalMax, excludeZone.key()),
                    Math.max(finalMax, excludeZone.value())
            );
            double z = random.nextDouble(
                    Math.min(-finalMax, excludeZone.key()),
                    Math.max(finalMax, excludeZone.value())
            );
            Location chunkPos = new Location(world, x, 0, z);
            world.getChunkAtAsync(chunkPos).thenAcceptAsync(chunk -> {
                Block block = world.getHighestBlockAt((int) x, (int) z);
                if (block.isLiquid() || block.isPassable()) return;
                chunkPos.setY(block.getY() + 1);
            });
            return chunkPos;
        }, Wild.executor());
    }


    /**
     * Asynchronously teleports the specified player to a pseudorandom location.
     * @param player The player being teleported
     * @param ignoreExclusion Whether exclusion rules should be ignored. True for yes, false for no
     * @param tries How many times the plugin should attempt to find a location
     * @return A {@link Trio}, which holds a list of locations attempted, a future that contains a selected location, and a future that contains the result of the teleportation.
     * The location future may be null if no valid location was chosen. The async teleport future may be null if a subsequent operation fails.
     */
    public static Trio<@NotNull List<Location>, @Nullable CompletableFuture<Location>, @Nullable CompletableFuture<Boolean>> randomTeleport(Player player, boolean ignoreExclusion, int tries) {

        double exclusion = ignoreExclusion ? 0 : exclusion();
        CompletableFuture<Location> pickedLocation = pickLocationAsync(player, exclusion, maximum());

        final List<Location> attempts = new ArrayList<>();

        // Try to pick a location first
        CompletableFuture<Location> prereqs = CompletableFuture.supplyAsync(() -> {
            Location result = DataUtil.getOrNull(pickedLocation, null);
            if (result == null) return null;

            final BlockState blockState = result.getBlock().getState(true);
            Biome biome = blockState.getBlock().getBiome();

            boolean water = BukkitUtil.isWater(biome);
            boolean claim = GPHook.isInClaim(result) != null;
            while ((water || claim) && attempts.size() < tries) {
                result = DataUtil.getOrNull(pickLocationAsync(player, exclusion, maximum()), null);
                if (result == null) continue;

                biome = result.getBlock().getBiome();
                water = BukkitUtil.isWater(biome);
                claim = GPHook.isInClaim(result) != null;
                attempts.add(result);
            }
            if (attempts.size() > tries && (water || claim)) result = null;

            return result;
        }, Wild.executor());

        final Location location = DataUtil.getOrNull(prereqs, null);
        if (location == null) {
            TextUtil.sendMessage(player, "Made " + tries + " attempts to RTP. Failure.", "Locations: " + attempts);
            return new Trio<>(attempts, prereqs, null);
        }

        final CompletableFuture<Boolean> asyncTp = player.teleportAsync(location);
        return new Trio<>(attempts, prereqs, asyncTp);
    }
}
