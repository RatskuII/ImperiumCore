package dev.RatFjc.ImperiumCore.extras;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ExperimentController {

    public static void allowExperiments(Player player, boolean allow) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        container.set(Keys.ALLOW_EXPERIMENTAL, PersistentDataType.BOOLEAN, allow);
        if (allow) TextUtil.sendMessage(player, "Experimental features are now enabled.");
        else TextUtil.sendMessage(player, "Experimental features are now disabled.");
    }

    public static boolean areExperimentsAllowed(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        var key = container.get(Keys.ALLOW_EXPERIMENTAL, PersistentDataType.BOOLEAN);

        return key != null && key;
    }
}
