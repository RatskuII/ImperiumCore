package dev.RatFjc.ImperiumCore.modules.itemquests.file;

import dev.RatFjc.ImperiumCore.extras.PairList;
import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.Reward;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.ItemUtil;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QuestLoader {

    private static final PairList<Integer, AbstractQuest> loadedQuests = new PairList<>();

    public static void loadFromFile(ConfigurationSection section, int index) {
        if (section == null) throw new NullPointerException();

        // Metadata
        String name = section.getString("name");
        List<String> description = section.getStringList("description");
        final String type = section.getString("type");
        final int required = section.getInt("total");
        @Nullable World world = BukkitUtil.getWorld(section.getString("world"));
        @Nullable Biome region = BukkitUtil.getBiome(section.getString("region"));

        // Rewards
        Reward reward = Reward.empty();
        ConfigurationSection rewardPaths = section.getConfigurationSection("reward");
        if (rewardPaths != null) {
            List<ItemStack> items = rewardPaths.getStringList("items").stream()
                    .filter(Objects::nonNull)
                    .map(obj -> ItemUtil.itemFromString(obj, DataUtil.parseData(obj).value()))
                    .toList();
            List<String> commands = rewardPaths.getStringList("commands");
            reward = new Reward(items, commands);
        }

        List<String> permissions = section.getStringList(".permissions");
        AbstractQuest quest = AbstractQuest.create(name, description, type, required, reward, world, region, permissions);
        loadedQuests.add(index, quest);
    }

    public static PairList<Integer, AbstractQuest> cachedQuests() {
        return loadedQuests;
    }

    public static AbstractQuest pullFromIndex(int index) {
        return loadedQuests.get(index);
    }
}
