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
    private static final Map<Location, Long> timers2 = new HashMap<>();

    private static final Sound switchSound = Sound.sound()
            .type(Key.key("minecraft:block.iron_door.close"))
            .source(Sound.Source.BLOCK)
            .volume(3F) // this might be kinda loud xd
            .pitch(0.5F)
            .build();

    private static final Sound switchSound2 = Sound.sound()
            .type(Key.key("minecraft:block.iron.place"))
            .source(Sound.Source.BLOCK)
            .volume(3F)
            .pitch(0.5F)
            .build();

    @EventHandler
    public void onSwitch(SignActionEvent event) {
        Location rail = event.getRailLocation();
        if (rail == null) rail = event.getLocation();

        // don't play anything if there's no one to hear it
        if (PlayerUtil.getNearbyPlayers(rail, 48).isEmpty()) return;

        if (event.isType("switcher")) {
            long current = System.currentTimeMillis();
            long previous = timers.getOrDefault(rail, 0L);
            long previous2 = timers2.getOrDefault(rail, 0L);

            // 300 ms
            if (current - previous >= 300) {
                timers.put(rail, current);
                play(rail, switchSound);
            }
            // 600 ms
            if (current - previous2 >= 600) {
                timers2.put(rail, current);
                play(rail, switchSound2);
            }
        }
    }

    private static void play(Location location, Sound sound) {
        plugin.getServer().playSound(sound, location.x(), location.y(), location.z());
    }

}
