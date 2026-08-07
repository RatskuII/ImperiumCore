package dev.RatFjc.ImperiumCore.modules.ultrabans.event.listener;

import dev.RatFjc.ImperiumCore.extras.multithreading.events.CoreAsyncListener;
import dev.RatFjc.ImperiumCore.init.UltraBans;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Operation;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.Executor;

public class MuteEnforcer implements CoreAsyncListener<AsyncChatEvent, UltraBans> {
    @Override
    public UltraBans module() {
        return null;
    }

    @Override
    public void onEvent(AsyncChatEvent event, Executor worker) {
        Player player = event.getPlayer();
        String affected = player.getName();
        if (event.isAsynchronous()) {
            Operation.isMuted(affected).thenAcceptAsync(silence -> {
                if (!silence) return;
                event.setCancelled(true);
                TextUtil.sendMessage(player, "You cannot do this while muted.");
            }, worker);
        }
    }

    @EventHandler
    @Override
    public void handler(AsyncChatEvent event) {
        CoreAsyncListener.super.handler(event);
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        String affected = event.getPlayer().getName();
        Operation.isMuted(affected).thenAccept(event::setCancelled);
    }
}
