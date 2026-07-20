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

    // Key to toggle experimental features
    public static final NamespacedKey ALLOW_EXPERIMENTAL = new NamespacedKey(plugin, "allow_experimental");
}
