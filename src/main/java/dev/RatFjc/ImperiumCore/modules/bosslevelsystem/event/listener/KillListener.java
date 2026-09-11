package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.event.listener;

import dev.RatFjc.ImperiumCore.extras.hooks.MythicHook;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.BossProfile;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillListener implements Listener {

    @EventHandler
    public void onBossKill(MythicMobDeathEvent event) {
        if (!MythicHook.enabled()) return;
        LivingEntity killer = event.getKiller();
        ActiveMob killed = event.getMob();

        if (!(killer instanceof Player player)) return;

        double maxHP = killed.getEntity().getMaxHealth();
        double add = maxHP / 50;

        BossProfile bossProfile = new BossProfile(player);
        bossProfile.add(add);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity killed = event.getEntity();
        Entity killer = event.getDamageSource().getDirectEntity();

        if (!(killer instanceof Player player)) return;
        if (!(killed instanceof Enemy)) return;

        var base = killed.getAttribute(Attribute.MAX_HEALTH);
        if (base == null) return;

        double health = base.getValue();
        double add = health / 10;
        BossProfile bossProfile = new BossProfile(player);
        bossProfile.add(add);
    }
}
