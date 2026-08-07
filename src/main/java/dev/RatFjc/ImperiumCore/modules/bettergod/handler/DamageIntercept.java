package dev.RatFjc.ImperiumCore.modules.bettergod.handler;

import dev.RatFjc.ImperiumCore.modules.bettergod.data.Context;
import dev.RatFjc.ImperiumCore.modules.bettergod.data.GodStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageIntercept implements Listener {

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!GodStats.isGod(player)) return;

        Context context = GodStats.godContext(player);
        switch (context) {
            case FULL -> event.setCancelled(true);
            case COMBAT -> {
                if (event instanceof EntityDamageByEntityEvent) event.setCancelled(true);
            }
            case ENVIRONMENT -> {
                if (event instanceof EntityDamageByBlockEvent || isEnvSource(event))
                    event.setCancelled(true);
            }
            case VANILLA -> {
                if (!isCustom(event)) event.setCancelled(true);
            }
            case null, default -> {}
        }
    }

    private boolean isEnvSource(EntityDamageEvent event) {
        switch (event.getCause()) {
            case FALL, FIRE, FIRE_TICK, FREEZE, SUFFOCATION,
                 MELTING, LAVA, DROWNING, BLOCK_EXPLOSION,
                 ENTITY_EXPLOSION, LIGHTNING, MAGIC, FALLING_BLOCK,
                 FLY_INTO_WALL, CRAMMING, HOT_FLOOR, CAMPFIRE -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isCustom(EntityDamageEvent event) {
        // Entity damager = event.getDamageSource().getDirectEntity();

        //todo Install mythic

        // if (damager != null) return Mythic.isMythicMob(damager);

        return event.getCause() == EntityDamageEvent.DamageCause.CUSTOM;
    }
}
