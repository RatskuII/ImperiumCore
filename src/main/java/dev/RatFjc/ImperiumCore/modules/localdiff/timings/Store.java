package dev.RatFjc.ImperiumCore.modules.localdiff.timings;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.localdiff.diffTypes.GlobalDifficulty;
import dev.RatFjc.ImperiumCore.utility.PDCUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.persistence.PersistentDataType;

public class Store {

    private static final GlobalDifficulty globalDifficulty = new GlobalDifficulty();

    public static double globalDiff = globalDifficulty.rating();

    public static double localDiff(OfflinePlayer player) {
        var obj = PDCUtil.getView(player, Keys.LOCAL_DIFF, PersistentDataType.DOUBLE);
        if (obj != null) return obj;

        return -1;
    }
}
