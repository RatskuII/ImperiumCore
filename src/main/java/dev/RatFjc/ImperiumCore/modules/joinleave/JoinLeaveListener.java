package dev.RatFjc.ImperiumCore.modules.joinleave;

import dev.RatFjc.ImperiumCore.extras.ExperimentController;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    // Low priority to allow other plugins to takeover if necessary
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!ExperimentController.areExperimentsAllowed(player)) return;
        event.joinMessage(
                TextUtil.color("+", TextColor.color(0, 200, 0))
                        .append(TextUtil.color(" " + player.getName(), TextColor.color(100, 100, 100)))
        );
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!ExperimentController.areExperimentsAllowed(player)) return;
        event.quitMessage(
                TextUtil.color("-", TextColor.color(200, 0 ,0))
                        .append(TextUtil.color(" " + player.getName(), TextColor.color(100, 100, 100)))
        );
    }

}
