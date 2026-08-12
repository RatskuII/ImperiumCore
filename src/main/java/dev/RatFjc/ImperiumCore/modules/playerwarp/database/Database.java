package dev.RatFjc.ImperiumCore.modules.playerwarp.database;

import dev.RatFjc.ImperiumCore.init.PlayerWarps;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Rating;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpType;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpUser;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public interface Database {

    // Initial setup
    void initialize();

    void close();

    // ** GET **;

    Warp warp(String name);

    /**
     * Gets the {@link WarpUser} linked to this warp.
     * @param warpID The UUID of the warp
     * @return A user, or null if none was found
     */
    WarpUser owner(UUID warpID);

    /**
     * Gets the location of this warp.
     * @param warpID The UUID of the warp
     * @return A location, or null if the warp does not exist
     */
    Location location(UUID warpID);

    /**
     * Gets the name assigned to this warp.
     * @param warpID The UUID of the warp
     * @return A name, or null if the warp does not exist
     */
    String name(UUID warpID);

    String description(UUID warpID);

    WarpType warpType(UUID warpID);

    @Nullable Rating rating(UUID warpID);

    /**
     * Gets a list of warps that this user owns.
     * @param warpUser The user to check.
     * @return A list of valid warps
     */
    List<Warp> warps(WarpUser warpUser);

    // ** SET **
    void setOwner(WarpUser owner, UUID warpID);

    void setLocation(Location location, UUID warpID);

    void setName(String name, UUID warpID);

    void setDescription(String description, UUID warpID);

    void setWarpType(WarpType warpType, UUID warpID);

    void setRating(@Nullable Rating rating, UUID warpID);

    boolean save(Warp warp);

    boolean update(Warp warp);

    void remove(Warp warp);

    default void dbLogErr(Exception exception) {
        LogUtil.log("Something went wrong while trying to access the database: " + exception, new PlayerWarps(), Level.SEVERE, false);
        LogUtil.log(Arrays.toString(exception.getStackTrace()));
    }
}
