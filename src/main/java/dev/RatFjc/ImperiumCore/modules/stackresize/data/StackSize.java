package dev.RatFjc.ImperiumCore.modules.stackresize.data;

import dev.RatFjc.ImperiumCore.extras.Pair;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StackSize {

    private static final Map<Material, Integer> stackSizes = new HashMap<>();

    public static Map<Material, Integer> setDefaults() {
        Arrays.stream(Material.values()).forEach(material -> stackSizes.put(material, material.getMaxStackSize()));
        return stackSizes;
    }

    public static Map<Material, Integer> set(Material material, int size) {
        stackSizes.put(material, size);
        return stackSizes;
    }

    public static Map<Material, Integer> set(Pair<Material, Integer> materialIntegerPair) {
        stackSizes.put(materialIntegerPair.key(), materialIntegerPair.value());
        return stackSizes;
    }

    public static Map<Material, Integer> update(Map<Material, Integer> materialIntegerMap) {
        stackSizes.putAll(materialIntegerMap);
        return stackSizes;
    }

    public static boolean changed(Material material) {
        return material.getMaxStackSize() != stackSizes.get(material);
    }

    public static boolean dangerous(Material material) {
        return material.getMaxStackSize() > stackSizes.get(material);
    }
}
