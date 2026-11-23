package dev.RatFjc.ImperiumCore.modules.regions;

import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an assortment of properties that define the behavior of a region.
 */
public class RegionProperty {

    @NotNull String name = ""; // Just an arbitrary name to assign

    long mobSpawnRate; // How often mobs can spawn in this region relative to the server set value

    float mobScale = 1; // Baby variants will invert this value: they become smaller as this number grows larger

    float rainCheckRate; // Additional pass that may change the frequency of rainfall
    // Just realized i cant really do this because weather states are global

    boolean unnaturalMobs; // Whether to allow mobs that don't normally spawn in the environment to spawn

    float mobStrength; // How powerful the mobs spawned in this region are

    double localDiffOffset; // How much the region should offset local difficulty - more dangerous regions will raise this value
    //todo ill need to figure out how to get the localdiff values to imperiumcore. ive already decided i wont migrate localdiff so ill need another solution
    // maybe papi?

    float acidity = 0; // Certain blocks become toxic to the player and deals damage when they try to use it - higher values means more damage and a larger assortment of elements that can hurt you
    Map<Material, Float> acidityIndex = new HashMap<>(); // Which materials can be acidic, and how high must acidity be for them to take effect.
    // Note that the regional acidity value is additive to the material acidity. Once a value is reached, values over it will simply be added. I know it's confusing but just trust it

    public RegionProperty() {}

    public RegionProperty(@NonNull String name, long mobSpawnRate, float mobScale, float rainCheckRate, boolean unnaturalMobs, float mobStrength, double localDiffOffset) {
        this.name = name;
        this.mobSpawnRate = mobSpawnRate;
        this.mobScale = mobScale;
        this.rainCheckRate = rainCheckRate;
        this.unnaturalMobs = unnaturalMobs;
        this.mobStrength = mobStrength;
        this.localDiffOffset = localDiffOffset;
    }

    public RegionProperty(@NonNull String name, long mobSpawnRate, float mobScale, float rainCheckRate, boolean unnaturalMobs, float mobStrength, double localDiffOffset, float acidity, Map<Material, Float> acidityIndex) {
        this.name = name;
        this.mobSpawnRate = mobSpawnRate;
        this.mobScale = mobScale;
        this.rainCheckRate = rainCheckRate;
        this.unnaturalMobs = unnaturalMobs;
        this.mobStrength = mobStrength;
        this.localDiffOffset = localDiffOffset;
        this.acidity = acidity;
        this.acidityIndex = acidityIndex;
    }

    public @NotNull String name() {
        return this.name;
    }

    public long mobSpawnRate() {
        return this.mobSpawnRate;
    }

    public float mobScale() {
        return this.mobScale;
    }

    public float rainCheckRate() {
        return this.rainCheckRate;
    }

    public boolean unnaturalMobs() {
        return this.unnaturalMobs;
    }

    public float mobStrength() {
        return this.mobStrength;
    }

    public double localDiffOffset() {
        return this.localDiffOffset;
    }

    public float acidity() {
        return this.acidity;
    }

    public Map<Material, Float> acidityIndex() {
        return DataUtil.sortByValue(this.acidityIndex);
    }

    public RegionProperty setName(String name) {
        this.name = name;
        return this;
    }

    public RegionProperty setMobSpawnRate(long mobSpawnRate) {
        this.mobSpawnRate = mobSpawnRate;
        return this;
    }

    public RegionProperty setMobScale(float mobScale) {
        this.mobScale = mobScale;
        return this;
    }

    public RegionProperty setRainCheckRate(float rainCheckRate) {
        this.rainCheckRate = rainCheckRate;
        return this;
    }

    public RegionProperty setUnnaturalMobs(boolean unnaturalMobs) {
        this.unnaturalMobs = unnaturalMobs;
        return this;
    }

    public RegionProperty setMobStrength(float mobStrength) {
        this.mobStrength = mobStrength;
        return this;
    }

    public RegionProperty setLocalDiffOffset(double localDiffOffset) {
        this.localDiffOffset = localDiffOffset;
        return this;
    }

    public RegionProperty setAcidity(float acidity) {
        this.acidity = acidity;
        return this;
    }

    public RegionProperty setAcidityIndex(Map<Material, Float> index) {
        this.acidityIndex = index;
        return this;
    }

    public RegionProperty addAcidityIndex(Material material, float value) {
        this.acidityIndex.put(material, value);
        return this;
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof RegionProperty compared)) return false;

        return name.equals(compared.name) && mobScale == compared.mobScale
                && unnaturalMobs == compared.unnaturalMobs && mobStrength == compared.mobStrength &&
                localDiffOffset == compared.localDiffOffset;
    }


}
