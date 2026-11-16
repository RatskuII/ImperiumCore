package dev.RatFjc.ImperiumCore.modules.claimfly;

import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.Vector;

public class FlyThread extends FlyManagement implements Listener {

    private static final Vector<UUID> pool = new Vector<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!pool.contains(uuid)) pool.add(uuid);
    }

    public synchronized void tick() {
        Threader.tick(() -> {
            for (UUID uuid : pool) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;

                FlyState flyState = flyStatus(player);
                if (flyState == FlyState.FLYING_CLAIM) {
                    // If the player exits the claim, stop allowing flight
                    if (!checkClaimOnly(player)) flyState = FlyState.NOT_FLYING;
                }
                // In-case the player loses permission somehow
                if (flyState == FlyState.FLYING) if (!checkGlobal(player)) flyState = FlyState.NOT_FLYING;

                if (flyState == FlyState.NOT_FLYING) player.setAllowFlight(false);
                setFly(player, flyState);
            }
        }, 20);
    }


}
