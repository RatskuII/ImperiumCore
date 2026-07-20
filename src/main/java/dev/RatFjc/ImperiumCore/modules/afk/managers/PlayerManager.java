package dev.RatFjc.ImperiumCore.modules.afk.managers;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.afk.AfkManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerManager extends AfkManager {

    public static void afkEffects(Player player, boolean isAfk) {
        AttributeModifier modifier = new AttributeModifier(Keys.STUN, -100, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, -1, 2);

        player.setInvulnerable(isAfk);
        AttributeInstance instance = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (isAfk) {
            if (instance != null) instance.addModifier(modifier);

            player.addPotionEffect(effect);
        } else {
            if (instance != null) if (instance.getModifiers().contains(modifier)) instance.removeModifier(modifier);

            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
}
