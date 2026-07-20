package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class SwitchSoundAdder implements Listener {

    private static final ImperiumCore plugin = ImperiumCore.getInstance();

    private static BukkitTask task;

    @EventHandler
    public void onSwitch(SignActionEvent event) {
        if (event.isType("switcher")) {

            if (task == null) {
                play(event.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1);
            }
        }
    }

    private static BukkitTask play(Location location, Sound sound, long duration) {
        task = plugin.getServer().getScheduler()
                .runTaskTimer(plugin, () -> {
                    location.getWorld().playSound(location, sound, 0.5F, 0.5F);
                }, 0, duration);

        return task;
    }

}
