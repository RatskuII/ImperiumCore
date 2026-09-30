package dev.RatFjc.ImperiumCore.modules.lastdeath.event;

import dev.RatFjc.ImperiumCore.modules.lastdeath.DeathStore;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathLocationUpdater implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        OfflinePlayer player = event.getPlayer();
        Location location = player.getLastDeathLocation();
        if (location == null) location = player.getLocation();
        if (location == null) return;

        DeathStore.cacheLocation(player, location);
    }
}
