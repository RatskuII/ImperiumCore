package dev.RatFjc.ImperiumCore.init;

import com.comphenix.protocol.ProtocolLib;
import dev.RatFjc.ImperiumCore.DependentModule;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.afk.conf.TimerConfiguration;
import dev.RatFjc.ImperiumCore.modules.afk.listener.PlayerListener;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Afk extends Module implements DependentModule {
    @Override
    public String name() {
        return "AFK";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerEvent(new PlayerListener());
        fileSetup(new TimerConfiguration());
    }

    @Override
    public List<@Nullable Plugin> dependencies() {
        return List.of(
                new ProtocolLib()
        );
    }
}
