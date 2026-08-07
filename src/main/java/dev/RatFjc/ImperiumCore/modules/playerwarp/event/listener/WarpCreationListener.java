package dev.RatFjc.ImperiumCore.modules.playerwarp.event.listener;

import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.event.WarpCreateEvent;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarpCreationListener implements Listener {

    @EventHandler
    public void onCreation(WarpCreateEvent event) {
        Warp warp = event.getWarp();
        OfflinePlayer player = warp.owner();

        LogUtil.log("A new warp was created with owner " + player.getName() + ", description " + warp.getDescription() +
                ", location " + warp.location().toString());
    }

}
