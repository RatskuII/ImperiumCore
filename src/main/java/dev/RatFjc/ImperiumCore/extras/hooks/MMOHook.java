package dev.RatFjc.ImperiumCore.extras.hooks;

import com.gmail.nossr50.api.ExperienceAPI;
import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MMOHook {

    public static CompletableFuture<Integer> totalPower(Player player) {
        return CompletableFuture.supplyAsync(() -> ExperienceAPI.getPowerLevel(player), Threader.corePool());
    }
}
