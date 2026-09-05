package dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.init.FriendsAPI;
import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Intended to clear out stale friendship states. Typically, this happens when someone who got removed from a
 * friend list was offline.
 */
public class Stale implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User user = new User(event.getPlayer());
        List<String> friendUUIDs = user.getFriends().stream()
                .map(UUID::toString)
                .toList();

        for (String entry : friendUUIDs) {
            UUID uuid = DataUtil.stringToUUID(entry);
            if (uuid == null) continue;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            PersistentDataContainerView containerView = offlinePlayer.getPersistentDataContainer();

            var values = containerView.get(Keys.FRIENDS, PersistentDataType.LIST.strings());
            if (values == null) return;

            String joined = user.uuid().toString();
            if (!values.contains(joined)) {
                User removed = new User(offlinePlayer);
                Friend.removeFriend(user, removed);
                LogUtil.log("Cleared stale friend state for " + user.name() + ", " + removed.name(), new FriendsAPI(), Level.INFO, true);
            }
        }
    }
}
