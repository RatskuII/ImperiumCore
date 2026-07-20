package dev.RatFjc.ImperiumCore.modules.joinleave;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        event.joinMessage(
                TextUtil.color("+", TextColor.color(0, 200, 0))
                        .append(TextUtil.color(" " + player.getName(), TextColor.color(100, 100, 100)))
        );
    }

}
