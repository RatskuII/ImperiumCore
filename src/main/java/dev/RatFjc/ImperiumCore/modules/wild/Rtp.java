package dev.RatFjc.ImperiumCore.modules.wild;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.extras.Pair;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.init.Wild;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
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

public class Rtp extends ConfigurationSaver implements TabExecutor, CommandInterface {

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


        if (args.length > 1) return badArguments(sender, "/rtp <ignoreExclusion>");
        boolean ignore = args.length == 1;


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


    protected static CompletableFuture<Pair<List<Location>, CompletableFuture<Boolean>>> randomTeleport(Player player, boolean ignoreExclusion, int tries) {
        return CompletableFuture.supplyAsync(() -> {
            double exclusion = ignoreExclusion ? 0 : exclusion();
            Location result = pickLocation(player, exclusion, maximum());
            Biome biome = result.getBlock().getBiome();
            List<@Nullable Location> attempts = new ArrayList<>();

            boolean water = BukkitUtil.isWater(biome);
            while (water && attempts.size() < tries) {
                result = pickLocation(player, exclusion, maximum());
                biome = result.getBlock().getBiome();
                water = BukkitUtil.isWater(biome);
                attempts.add(result);
            }
            if (attempts.size() >= tries && water) result = null;

            if (result == null) return new Pair<>(attempts, CompletableFuture.failedFuture(new NullPointerException()));
            return new Pair<>(attempts, player.teleportAsync(result));
        }, Wild.executor());
    }
}
