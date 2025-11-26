package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SwitchSoundAdder implements Listener {

    private static final ImperiumCore plugin = ImperiumCore.getInstance();

    @EventHandler
    public void onSwitch(SignActionEvent event) {
        if (event.isType("switcher")) {

        }
    }

    private static void play(Sound sound) {

    }

}
