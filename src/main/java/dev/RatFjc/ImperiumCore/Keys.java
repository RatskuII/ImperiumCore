package dev.RatFjc.ImperiumCore;

import org.bukkit.NamespacedKey;

public final class Keys extends Utility {

    // Key to allow night vision
    public static final NamespacedKey NV = new NamespacedKey(plugin, "NV");

    // Keys related to AFK
    public static final NamespacedKey AFK = new NamespacedKey(plugin, "AFK");
    public static final NamespacedKey POST = new NamespacedKey(plugin, "post-afk");
    public static final NamespacedKey PRE = new NamespacedKey(plugin, "pre-afk");

    public static final NamespacedKey STUN = new NamespacedKey(plugin, "stun_afk-effect");

    // Keys related to BetterGod
    public static final NamespacedKey GOD = new NamespacedKey(plugin, "god");
    public static final NamespacedKey GOD_CONTEXT = new NamespacedKey(plugin, "god-context");

    // Keys related to punishment entries
    public static final NamespacedKey MUTE = new NamespacedKey(plugin, "muted");
    public static final NamespacedKey MUTE_DURATION = new NamespacedKey(plugin, "mute-duration");

    // Keys related to PetConverter
    public static final NamespacedKey PET_UI = new NamespacedKey(plugin, "pet-ui");

    // Keys related to invisible item frames
    public static final NamespacedKey INVIS_FRAME = new NamespacedKey(plugin, "invis-frame");
    public static final NamespacedKey GLOW_FRAME = new NamespacedKey(plugin, "invis-glow");

    // Key to toggle experimental features
    public static final NamespacedKey ALLOW_EXPERIMENTAL = new NamespacedKey(plugin, "allow_experimental");
}
