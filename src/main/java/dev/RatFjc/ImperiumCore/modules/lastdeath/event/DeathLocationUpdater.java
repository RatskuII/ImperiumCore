package dev.RatFjc.ImperiumCore.modules.lastdeath.event;

import dev.RatFjc.ImperiumCore.init.LastDeath;
import dev.RatFjc.ImperiumCore.modules.lastdeath.DeathStore;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.logging.Level;

public class DeathLocationUpdater implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        OfflinePlayer player = event.getPlayer();
        Location location = player.getLastDeathLocation();
        if (location != null) {
            LogUtil.debug("Death event recorded. Location: " + location, new LastDeath());
            DeathStore.cacheLocation(player, location);
        }
    }
}
