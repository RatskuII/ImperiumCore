package dev.RatFjc.ImperiumCore.modules.itemquests.impl;

import dev.RatFjc.ImperiumCore.modules.itemquests.AbstractQuest;
import dev.RatFjc.ImperiumCore.modules.itemquests.Reward;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalQuest extends AbstractQuest {

    protected final List<OfflinePlayer> participants = new ArrayList<>();

    protected GlobalQuest(String name, List<String> description, String type, int required) {
        super(name, description, type, required);
    }

    protected GlobalQuest(String name, List<String> description, String type, int required, Reward reward) {
        super(name, description, type, required, reward);
    }

    protected GlobalQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world) {
        super(name, description, type, required, reward, world);
    }

    protected GlobalQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world, @Nullable Biome region) {
        super(name, description, type, required, reward, world, region);
    }

    protected GlobalQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world, @Nullable Biome region, List<String> permissions) {
        super(name, description, type, required, reward, world, region, permissions);
    }

}
