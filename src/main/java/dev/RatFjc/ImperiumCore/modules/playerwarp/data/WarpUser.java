package dev.RatFjc.ImperiumCore.modules.playerwarp.data;

import org.bukkit.OfflinePlayer;

// Not in use right now, will use later to set up limits and permissions
public class WarpUser {

    private OfflinePlayer offlinePlayer;
    private int limit = 3;

    public WarpUser(OfflinePlayer player) {
        this.offlinePlayer = player;
    }
}
