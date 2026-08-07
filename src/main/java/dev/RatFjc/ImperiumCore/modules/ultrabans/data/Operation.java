package dev.RatFjc.ImperiumCore.modules.ultrabans.data;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.ultrabans.Punishment;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.files.PunishmentSaver;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Additional operations related to punishments
 */
public class Operation {

    public static <T> void ban(Punishment<T> entry) {
        PunishmentSaver.savePunishment(entry);
        if (entry.target() instanceof Player player) {

            Component message = TextUtil.nbt("You have been banned: " + entry.reason());
            player.kick(message, PlayerKickEvent.Cause.BANNED);
        }
    }

    public static <T> void mute(Punishment<T> entry) {
        PunishmentSaver.savePunishment(entry);
        if (entry.target() instanceof Player player) {
            if (entry.duration().isPresent()) mute(player, entry.duration().get());
            else mute(player);
        }
    }

    public static <T> void kick(Punishment<T> entry) {
        PunishmentSaver.savePunishment(entry);
        if (entry.target() instanceof Player player) kick(player, entry.reason());
    }

    public static <T> void warn(Punishment<T> entry) {
        PunishmentSaver.savePunishment(entry);
        if (entry.target() instanceof String string) {
            TextUtil.announce(string + " has been warned: " + entry.reason());
        }
        if (entry.target() instanceof Player player) {
            TextUtil.announce(player.getName() + " has been warned: " + entry.reason());
        }
    }

    public static CompletableFuture<Boolean> isBanned(String target) {
        return PunishmentSaver.hasPunishment(target, PunishmentType.BAN);
    }

    public static CompletableFuture<Boolean> isMuted(String target) {
        return PunishmentSaver.hasPunishment(target, PunishmentType.MUTE);
    }

    private static void kick(Player player, String reason) {
        Component component = TextUtil.nbt(reason);
        player.kick(component);
    }

    private static void mute(Player player, Duration duration) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        container.set(Keys.MUTE, PersistentDataType.BOOLEAN, true);
        container.set(Keys.MUTE_DURATION, PersistentDataType.LONG, duration.toMillis());
    }

    private static void mute(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        container.set(Keys.MUTE, PersistentDataType.BOOLEAN, true);
        container.set(Keys.MUTE_DURATION, PersistentDataType.LONG, -1L);
    }

    private static boolean isMuted(Player player) {
        if (player == null) return false;
        PersistentDataContainer container = player.getPersistentDataContainer();
        var value = container.get(Keys.MUTE, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

}
