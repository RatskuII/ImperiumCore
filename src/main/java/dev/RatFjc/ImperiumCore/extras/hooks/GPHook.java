package dev.RatFjc.ImperiumCore.extras.hooks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Vector;

public class GPHook {

    private static final GriefPrevention instance = GriefPrevention.instance;
    private static final DataStore data = instance.dataStore;

    // Not thread safe
    public static Claim isInClaim(Entity entity) {
        Location location = entity.getLocation();
        return data.getClaimAt(location, true, null);
    }

    public static Claim isInClaim(Location point) {
        return data.getClaimAt(point, true, null);
    }

    public static Vector<Claim> claims(Player player) {
        PlayerData playerData = data.getPlayerData(player.getUniqueId());
        return playerData.getClaims();
    }

    public static int getClaimBlocks(Player player) {
        PlayerData playerData = data.getPlayerData(player.getUniqueId());
        return playerData.getRemainingClaimBlocks();
    }

    public static int accumulatedClaimBlocks(Player player) {
        PlayerData playerData = data.getPlayerData(player.getUniqueId());
        return playerData.getAccruedClaimBlocks();
    }
}
