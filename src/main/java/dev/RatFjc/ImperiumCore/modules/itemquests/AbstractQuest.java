package dev.RatFjc.ImperiumCore.modules.itemquests;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents the general structure for a quest.
 */
public abstract class AbstractQuest implements Listener {

    private String name;
    private List<String> description;
    private final String type;
    private final int required;
    private Reward reward;

    private @Nullable World world;
    private @Nullable Biome region;
    private List<String> permissions;

    protected AbstractQuest(String name, List<String> description, String type, int required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.reward = Reward.empty();

        this.world = null;
        this.region = null;
        this.permissions = List.of();

        this.required = required;
    }

    protected AbstractQuest(String name, List<String> description, String type, int required, Reward reward) {
        this(name, description, type, required);
        this.reward = reward;
    }

    protected AbstractQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world) {
        this(name, description, type, required, reward);
        this.world = world;
    }

    protected AbstractQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world, @Nullable Biome region) {
        this(name, description, type, required, reward, world);
        this.region = region;
    }

    protected AbstractQuest(String name, List<String> description, String type, int required, Reward reward, @Nullable World world, @Nullable Biome region, List<String> permissions) {
        this(name, description, type, required, reward, world, region);
        this.permissions = permissions;
    }

    public static AbstractQuest create(String name, List<String> description, String type, int required, Reward reward, @Nullable World world, @Nullable Biome region, List<String> permissions) {
        return new AbstractQuest(name, description, type, required, reward, world, region, permissions) {};
    }

    public String name() {
        return this.name;
    }

    public List<String> getDescription() {
        return this.description;
    }

    public String type() {
        return this.type;
    }

    public int required() {
        return this.required;
    }

    public Reward reward() {
        return this.reward;
    }

    public @Nullable World world() {
        return this.world;
    }

    public @Nullable Biome region() {
        return this.region;
    }

    public List<String> permissions() {
        return this.permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void appendDescription(String descriptionLine) {
        this.description.add(descriptionLine);
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public void updateReward(@Nullable List<ItemStack> items, @Nullable List<String> commands) {
        this.reward.addItems(items);
        this.reward.addCommands(commands);
    }

}
