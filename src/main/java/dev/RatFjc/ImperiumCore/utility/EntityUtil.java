package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.player.PlayerMoveEvent;

public class EntityUtil extends Utility {

    public static boolean isTamed(LivingEntity entity) {
        if (!(entity instanceof Tameable tameable)) return false;
        return tameable.isTamed();
    }

    public static boolean sitting(LivingEntity entity) {
        if (!(entity instanceof Sittable sittable)) return false;
        return sittable.isSitting();
    }

    public static boolean isActualBlockMovement(PlayerMoveEvent event) {
        return !(event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ());
    }

    public static boolean isActualBlockMovement(EntityMoveEvent event) {
        return !(event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ());
    }
}
