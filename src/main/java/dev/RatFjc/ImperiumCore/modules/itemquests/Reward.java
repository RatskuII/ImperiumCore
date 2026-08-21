package dev.RatFjc.ImperiumCore.modules.itemquests;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    private List<ItemStack> items;
    private List<String> commands;

    public Reward(List<ItemStack> items, List<String> commands) {
        this.items = items;
        this.commands = commands;

        if (items == null) this.items = new ArrayList<>();
        if (commands == null) this.commands = new ArrayList<>();
    }

    public Reward(List<ItemStack> items) {
        this(items, null);
    }

    protected Reward() {
        this(null, null);
    }

    public static Reward empty() {
        return new Reward();
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void addCommands(List<String> commands) {
        this.commands.addAll(commands);
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void addItems(List<ItemStack> items) {
        this.items.addAll(items);
    }

    public void addItem(ItemStack itemStack) {
        this.items.add(itemStack);
    }
}
