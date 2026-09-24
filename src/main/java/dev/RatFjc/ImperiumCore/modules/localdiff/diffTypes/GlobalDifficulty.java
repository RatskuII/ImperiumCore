package dev.RatFjc.ImperiumCore.modules.localdiff.diffTypes;

import dev.RatFjc.ImperiumCore.modules.localdiff.Difficulty;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import org.bukkit.World;

public class GlobalDifficulty extends Difficulty {

    /**
     * Initial scale increase
     */
    private static double increaseScale = 0.01;

    @Override
    public double affectedRange() {
        return -1;
    }

    @Override
    public double rating() {
        double diff = 1;

        World world = BukkitUtil.getWorld("world");
        if (world == null) throw new NullPointerException("The world provided cannot be null.");

        var bukkitDiff = world.getDifficulty();
        switch (bukkitDiff) {
            case PEACEFUL -> diff += 1;
            case EASY -> diff += 2;
            case NORMAL -> diff += 3;
            case HARD -> diff += 4;
        }

        int players = PlayerUtil.getOnlinePlayerList().size();
        diff += players * 0.75;

        return DataUtil.truncate(diff);
    }

    public static double getIncreaseScale() {
        return increaseScale;
    }

    public static double accumulate() {
        int players = PlayerUtil.getOnlinePlayerList().size();
        for (int i = 0; i < players; i++) increaseScale++;
        return increaseScale;
    }
}
