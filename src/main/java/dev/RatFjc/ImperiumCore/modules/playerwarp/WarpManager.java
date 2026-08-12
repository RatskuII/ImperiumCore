package dev.RatFjc.ImperiumCore.modules.playerwarp;

import dev.RatFjc.ImperiumCore.init.PlayerWarps;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpType;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpUser;
import dev.RatFjc.ImperiumCore.modules.playerwarp.database.DBWarpSaver;
import dev.RatFjc.ImperiumCore.modules.playerwarp.event.WarpCreateEvent;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class WarpManager {

    /**
     * Creates a basic player warp with type MISC and no description.
     * @param owner The warp owner
     * @param name The warp name
     * @param location The warp's location
     * @return A new {@link Warp}, or null if a warp with this name already exists
     * @apiNote This will return null if the creation is interrupted or canceled.
     */
    public static @Nullable Warp create(Player owner, String name, Location location) {
        for (Warp warp : warps()) {
            if (warp.name().equals(name)) {
                TextUtil.sendMessage(owner, "A warp already exists with this name.");
                return null;
            }
        }
        WarpUser warpUser = new WarpUser(owner);
        Warp warp = new Warp(warpUser, location, name, "", WarpType.MISC);
        WarpCreateEvent warpCreateEvent = new WarpCreateEvent(warp);
        if (warpCreateEvent.isCancelled()) return null;
        DBWarpSaver.saveWarp(warp);
        TextUtil.sendMessage(owner, "Successfully created a new warp named " + name);
        return warp;
    }

    /**
     * Creates a player warp with type MISC.
     * @param owner The warp owner
     * @param name The warp name
     * @param description The warp description
     * @param location The warp's location
     * @return A new {@link Warp}, or null if the creation event is canceled.
     */
    public static @Nullable Warp create(Player owner, String name, String description, Location location) {
        Warp warp = create(owner, name, location);
        if (warp == null) return null;
        warp.setDescription(description);
        TextUtil.sendMessage(owner, "Successfully created a new warp named " + name + " with description " +
                description);
        DBWarpSaver.saveWarp(warp);
        return warp;
    }

    /**
     * Gets the warp that has this name.
     * @param warpName The name
     * @return A warp, or null if no warp with this name exists
     */
    public static @Nullable Warp getWarp(String warpName) {
        Warp warp = null;
        try {
            warp = DBWarpSaver.getWarp(warpName).get();
        } catch (ExecutionException | InterruptedException e) {
            LogUtil.log("Something went wrong. The warp might be corrupt or does not exist.", new PlayerWarps(), Level.WARNING, false);
            LogUtil.log(e.getMessage());
            LogUtil.log(Arrays.toString(e.getStackTrace()));
        }
        return warp;
    }

    public static List<Warp> warps() {
        List<Warp> warps = new ArrayList<>();
        try {
            warps.addAll(DBWarpSaver.getWarps().get());
        } catch (InterruptedException | ExecutionException ignored) {

        }
        return warps;
    }

    public static void teleport(Player player, Warp warp) {
        Location location = warp.location();
        player.teleport(location);
        TextUtil.sendMessage(player, "Sending you to " + warp.name());
    }

    public static void reset(Warp warp, Location newLocation) {
        warp.setLocation(newLocation);
        DBWarpSaver.updateWarp(warp);
    }

    public static void remove(String name) {
        Warp warp = getWarp(name);
        if (warp != null) DBWarpSaver.removeWarp(warp);
        else LogUtil.log("Tried to remove a warp but couldn't find it!", new PlayerWarps(), Level.WARNING, true);
    }

    public static void setDesc(Warp warp, String description) {
        warp.setDescription(description);
        DBWarpSaver.updateWarp(warp);
    }

    /**
     * Gets the {@link WarpUser} assigned to this player. If none exists, a new one will be created.
     * @param player The player to assign to the WarpUser instance
     * @return A non-null {@link WarpUser}
     */
    public static @NotNull WarpUser elseCreateUser(OfflinePlayer player) {
        WarpUser result;
        try {
            result = DBWarpSaver.getWarpUser(player.getUniqueId()).get();
        } catch (InterruptedException | ExecutionException e) {
            result = new WarpUser(player);
        }
        if (result == null) result = new WarpUser(player);
        return result;
    }
}
