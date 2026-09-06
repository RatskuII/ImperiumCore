package dev.RatFjc.ImperiumCore.modules.friendsapi;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.friendsapi.data.ListType;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A wrapper class that contains a player and an associated list of friends.
 * @see Friend
 */
public record User(OfflinePlayer player) {

    public static User fromName(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        return new User(offlinePlayer);
    }

    /**
     * Gets the UUID of this user.
     * @return A non-null UUID
     */
    public UUID uuid() {
        return this.player.getUniqueId();
    }

    /**
     * Gets the name of this user.
     * @return The user's name, or an empty string if the user isn't initialized
     */
    public String name() {
        if (this.player.getName() == null) return "";
        return this.player.getName();
    }

    public @Nullable Player asPlayer() {
        return this.player.getPlayer();
    }

    /**
     * Gets a mutable container for this user.
     * @return A valid {@link PersistentDataContainer}, or null if the user is offline or not initialized.
     */
    public @Nullable PersistentDataContainer container() {
        Player onlinePlayer = this.player().getPlayer();
        if (onlinePlayer == null) return null;

        return onlinePlayer.getPersistentDataContainer();
    }

    public @NotNull List<@NotNull UUID> getFriends() {
        PersistentDataContainerView container = player.getPersistentDataContainer();
        List<String> list = container.get(Keys.FRIENDS, ListType.STRING);
        if (list == null) return List.of();

        List<UUID> uuids = new ArrayList<>();
        for (String string : list) {
            UUID uuid = DataUtil.stringToUUID(string);
            if (uuid == null) continue;

            uuids.add(uuid);
        }
        return uuids;
    }

    public boolean equals(User user) {
        return user != null && this.uuid().equals(user.uuid());
    }
}
