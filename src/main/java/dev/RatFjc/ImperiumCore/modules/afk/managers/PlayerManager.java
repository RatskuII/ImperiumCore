package dev.RatFjc.ImperiumCore.modules.afk.managers;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;

public class PlayerManager extends AfkManager {

    public static void afkEffects(Player player, boolean isAfk) {
        AttributeModifier modifier = new AttributeModifier(Keys.STUN, -100, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);

        player.setInvulnerable(isAfk);

        AttributeInstance instance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (isAfk) {
            if (instance != null) instance.addModifier(modifier);
        } else {
            if (instance != null) if (instance.getModifiers().contains(modifier)) instance.removeModifier(modifier);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set group.afk " + isAfk);
    }
}
