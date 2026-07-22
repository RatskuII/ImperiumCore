package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class SwitchSoundAdder implements Listener {

    private static final ImperiumCore plugin = ImperiumCore.getInstance();

    private static final Map<Location, Long> timers = new HashMap<>();

    private static final Sound switchSound = Sound.sound()
            .type(Key.key("minecraft:block.iron_door.close"))
            .source(Sound.Source.BLOCK)
            .volume(0.75F)
            .pitch(0.15F)
            .build();

    @EventHandler
    public void onSwitch(SignActionEvent event) {
        Location rail = event.getRailLocation();
        if (rail == null) rail = event.getLocation();

        if (PlayerUtil.getNearbyPlayers(rail, 30).isEmpty()) return;

        if (event.isType("switcher")) {
            long current = System.currentTimeMillis();
            long previous = timers.getOrDefault(rail, 0L);

            if (current - previous >= 300) {
                timers.put(rail, current);
                play(rail, switchSound);
            }
        }
    }

    private static void play(Location location, Sound sound) {
        plugin.getServer().playSound(sound, location.x(), location.y(), location.z());
    }

}
