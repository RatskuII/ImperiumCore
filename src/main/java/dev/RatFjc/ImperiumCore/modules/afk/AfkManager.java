package dev.RatFjc.ImperiumCore.modules.afk;

import dev.RatFjc.ImperiumCore.PluginProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AfkManager implements PluginProvider {

    public static final String kickImmunity = "imperiumcore.afk.immunity";

    public static final Map<UUID, Double> healthCache = new HashMap<>();

}
