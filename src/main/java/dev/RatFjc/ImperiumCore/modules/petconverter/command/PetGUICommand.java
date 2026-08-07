package dev.RatFjc.ImperiumCore.modules.petconverter.command;

import dev.RatFjc.ImperiumCore.modules.petconverter.InvBuilder;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class PetGUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "Only players can run this command.");
            return false;
        }

        Inventory inventory = new InvBuilder()
                .create(player, 18, "Pet Exchanger")
                .createUI()
                .build();
        player.openInventory(inventory);
        return true;
    }
}
