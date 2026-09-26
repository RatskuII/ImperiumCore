package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;

public class EntityUtil extends Utility {

    public static boolean isTamed(LivingEntity entity) {
        if (!(entity instanceof Tameable tameable)) return false;
        return tameable.isTamed();
    }

    public static boolean sitting(LivingEntity entity) {
        if (!(entity instanceof Sittable sittable)) return false;
        return sittable.isSitting();
    }
}
