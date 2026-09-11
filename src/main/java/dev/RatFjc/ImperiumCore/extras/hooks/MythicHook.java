package dev.RatFjc.ImperiumCore.extras.hooks;

import io.lumine.mythic.bukkit.MythicBukkit;

public class MythicHook {

    private static final MythicBukkit mythic = MythicBukkit.inst();

    public static boolean enabled() {
        return mythic != null && mythic.isEnabled();
    }
}
