package dev.RatFjc.ImperiumCore.modules.regions.hardcoded;

import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * The hardcoded secondary region. This is still relatively easy to deal with, but it's not a paradise like the central
 * zone.
 */
public class Region1 extends Region {
    @Override
    public World world() {
        return Bukkit.getWorld("world");
    }

    @Override
    public RegionProperty properties() {
        RegionProperty regionProperty = new RegionProperty();
        return regionProperty
                .setName("second")
                .setMobScale(0.15f)
                .setMobSpawnRate(9)
                .setLocalDiffOffset(0.25)
                .setMobStrength(1.04f);
    }

    @Override
    public Pair<Integer, Integer> bounds() {
        return new Pair<>(8100, 15000);
    }


}
