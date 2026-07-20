package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class PlayerUtil extends Utility {

    public static List<Player> getNearbyPlayers(Location location, double radius) {
        return List.copyOf(location.getNearbyPlayers(radius));
    }

    public static PersistentDataContainer container(Player player) {
        return player.getPersistentDataContainer();
    }
}
