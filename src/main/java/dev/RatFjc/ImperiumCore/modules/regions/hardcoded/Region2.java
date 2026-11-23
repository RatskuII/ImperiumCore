package dev.RatFjc.ImperiumCore.modules.regions.hardcoded;

import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * The hardcoded tertiary zone, which provides some increased difficulty and unique properties.
 */
public class Region2 extends Region {
    @Override
    public World world() {
        return Bukkit.getWorld("world");
    }

    @Override
    public RegionProperty properties() {
        RegionProperty regionProperty = new RegionProperty();
        return regionProperty
                .setName("third")
                .setLocalDiffOffset(3.08)
                .setMobScale(1.5f)
                .setMobSpawnRate(33)
                .setRainCheckRate(0.17f)
                .setUnnaturalMobs(true);
    }

    @Override
    public Pair<Integer, Integer> bounds() {
        return new Pair<>(15000, 25000);
    }
}
