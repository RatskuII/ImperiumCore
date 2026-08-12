package dev.RatFjc.ImperiumCore.modules.playerwarp.data;

import dev.RatFjc.ImperiumCore.modules.playerwarp.database.DBWarpSaver;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class WarpUser {

    private final OfflinePlayer offlinePlayer;
    private int limit = 3;

    public WarpUser(OfflinePlayer player) {
        this.offlinePlayer = player;
        DBWarpSaver.saveUser(this);
    }

    public WarpUser(String player) {
        this.offlinePlayer = Bukkit.getOfflinePlayer(player);
    }

    public WarpUser(UUID uuid) {
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
    }

    public OfflinePlayer user() {
        return this.offlinePlayer;
    }

    public String name() {
        return this.offlinePlayer.getName();
    }

    public UUID userID() {
        return this.offlinePlayer.getUniqueId();
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
