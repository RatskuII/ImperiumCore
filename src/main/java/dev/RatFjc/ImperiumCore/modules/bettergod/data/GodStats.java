package dev.RatFjc.ImperiumCore.modules.bettergod.data;

import dev.RatFjc.ImperiumCore.Keys;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class GodStats {

    public static boolean isGod(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.GOD, PersistentDataType.BOOLEAN);

        return key != null && key;
    }

    public static @Nullable Context godContext(Player player) {
        if (!isGod(player)) return null;

        PersistentDataContainer container = player.getPersistentDataContainer();
        return container.get(Keys.GOD_CONTEXT, new ContextPDC());
    }
}
