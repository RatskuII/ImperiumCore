package dev.RatFjc.ImperiumCore.modules.nv;

import dev.RatFjc.ImperiumCore.file.Keys;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NVController {

    public static void setNightVision(Player player, boolean nv) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        container.set(Keys.NV, PersistentDataType.BOOLEAN, nv);
        if (nv) {
            PotionEffect effect = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 3);
            player.addPotionEffect(effect);

            TextUtil.sendMessage(player, "Successfully applied night vision.");
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            TextUtil.sendMessage(player, "Successfully removed night vision.");
        }
    }

    /**
     * Checks if a night vision effect is present.
     * @param player The player
     * @return Whether night vision is applied
     * @apiNote If night vision is applied on the player, this will still return false if the source of that
     * night vision is not this plugin.
     */
    public static boolean hasNightVision(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        var key = container.get(Keys.NV, PersistentDataType.BOOLEAN);
        return key != null && key && player.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }


}
