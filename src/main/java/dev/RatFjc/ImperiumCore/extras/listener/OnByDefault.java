package dev.RatFjc.ImperiumCore.extras.listener;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.extras.ExperimentController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnByDefault implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();

        var key = container.get(Keys.ALLOW_EXPERIMENTAL, PersistentDataType.BOOLEAN);
        if (key == null) ExperimentController.allowExperiments(player, true);
    }
}
