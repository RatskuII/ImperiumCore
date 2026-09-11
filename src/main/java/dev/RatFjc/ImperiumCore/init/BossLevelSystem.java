package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.DependentModule;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BossLevelSystem extends Module implements DependentModule {
    @Override
    public String name() {
        return "BossLevelSystem";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {

    }

    @Override
    public List<@Nullable Plugin> dependencies() {
        return List.of(
                MythicBukkit.inst()
        );
    }
}
