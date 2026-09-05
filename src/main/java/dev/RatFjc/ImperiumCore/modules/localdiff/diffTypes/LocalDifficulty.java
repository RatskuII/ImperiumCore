package dev.RatFjc.ImperiumCore.modules.localdiff.diffTypes;

import dev.RatFjc.ImperiumCore.extras.hooks.MMOHook;
import dev.RatFjc.ImperiumCore.init.LocalDiff;
import dev.RatFjc.ImperiumCore.modules.localdiff.Difficulty;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class LocalDifficulty extends Difficulty {

    protected LocalDifficulty(Player player) {
        super(player);
    }

    @Override
    public double affectedRange() {
        return 0;
    }

    @Override
    public double rating() {
        if (!player.isOnline()) return 0;
        int experienceValue = Math.max(player.getLevel(), 100) / 10; // Max diff from this is +10
        int mmoPower;
        try {
            mmoPower = MMOHook.totalPower(player).get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LogUtil.log("MCMMO api timed out!", new LocalDiff(), Level.WARNING, false);
            mmoPower = 0;
        }
        mmoPower = mmoPower / 1000;
        mmoPower = Math.max(mmoPower, 15); // Max diff from this is +15

        double playtime = (double) player.getStatistic(Statistic.RECORD_PLAYED) / 10;
        double result = experienceValue + mmoPower + playtime;
        result = DataUtil.truncate(result);

        return result;
    }
}
