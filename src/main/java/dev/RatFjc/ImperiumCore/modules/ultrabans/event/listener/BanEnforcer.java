package dev.RatFjc.ImperiumCore.modules.ultrabans.event.listener;

import dev.RatFjc.ImperiumCore.extras.multithreading.events.CoreAsyncListener;
import dev.RatFjc.ImperiumCore.init.UltraBans;
import dev.RatFjc.ImperiumCore.modules.ultrabans.Punishment;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Operation;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentType;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.files.PunishmentSaver;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Level;

public class BanEnforcer implements CoreAsyncListener<AsyncPlayerPreLoginEvent, UltraBans> {

    @Override
    public UltraBans module() {
        return new UltraBans();
    }

    @Override
    public void onEvent(AsyncPlayerPreLoginEvent event, Executor worker) {
        String affected = event.getName();
        Operation.isBanned(affected).thenAcceptAsync(deny -> {
            if (deny) {
                Punishment<String> punishment;
                try {
                    punishment = PunishmentSaver.getPunishment(affected, PunishmentType.BAN).get();
                } catch (InterruptedException | ExecutionException exception) {
                    LogUtil.log("A worker was interrupted from its task", new UltraBans(), Level.WARNING, false);
                    LogUtil.log(exception.getMessage());
                    return;
                }
                if (punishment != null) {
                    event.disallow(
                            AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                            TextUtil.nbt(punishment.reason())
                    );
                }
            }
            else event.allow();
        }, worker);
    }

    @EventHandler
    @Override
    public void handler(AsyncPlayerPreLoginEvent event) {
        CoreAsyncListener.super.handler(event);
    }
}
