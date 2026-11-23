package dev.RatFjc.ImperiumCore.modules.regions.hardcoded;

import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * A hardcoded central region. This is generally the safest zone on the map. Minimal mob spawns, reduced difficulty, and
 * no nasty properties to worry about.
 */
public class Region0 extends Region {
    @Override
    public World world() {
        return Bukkit.getWorld("world");
    }

    @Override
    public RegionProperty properties() {
        RegionProperty regionProperty = new RegionProperty();
        return regionProperty
                .setName("central")
                .setMobSpawnRate(-17)
                .setLocalDiffOffset(-2.5);
    }

    @Override
    public Pair<Integer, Integer> bounds() {
        return new Pair<>(0, 8000);
    }
}
