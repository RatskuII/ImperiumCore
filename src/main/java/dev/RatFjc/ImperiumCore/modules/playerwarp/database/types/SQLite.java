package dev.RatFjc.ImperiumCore.modules.playerwarp.database.types;

import dev.RatFjc.ImperiumCore.init.PlayerWarps;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Rating;
import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.Cache;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpType;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpUser;
import dev.RatFjc.ImperiumCore.modules.playerwarp.database.Database;
import dev.RatFjc.ImperiumCore.extras.HeadDatabase;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SQLite implements Database, HeadDatabase {

    private final Cache<WarpUser> userCache = Cache.create();
    private final Cache<String> nameCache = Cache.create();

    private Connection connection;

    @Override
    public String type() {
        return "SQLite";
    }

    @Override
    public void connection() {
        String url = String.format("jdbc:sqlite:%s/warps.db", plugin.getDataFolder());
        try {
            this.connection = DriverManager.getConnection(url);
            LogUtil.log("Successfully started a new local connection", new PlayerWarps(), Level.INFO, true);
        } catch (SQLException e) {
            dbLogErr(e);
            this.connection = null;
        }
    }

    @Override
    public boolean isOpen() {
        boolean open;
        try {
            if (connection == null) return false;
            open = !connection.isClosed();
        } catch (SQLException e) {
            LogUtil.log("The database is closed!", new PlayerWarps(), Level.WARNING, false);
            open = false;
        }
        return open;
    }

    private void execute(String sql, Object... objects) {
        if (!isOpen()) return;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object object : objects) stmt.setObject(i++, object);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            dbLogErr(e);
        }
    }

    /**
     * Will create a new table if it doesn't already exist.
     * @apiNote Star ratings and star reason is currently being left null/empty since I'm redoing how
     * they interact with player warps. They should be ignored entirely if possible.
     */
    @Override
    public void initialize() {
        if (!isOpen()) return;
        String warpTable = "CREATE TABLE IF NOT EXISTS warps (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userID TEXT," +
                "warpID TEXT," +
                "world TEXT," +
                "x DOUBLE," +
                "y DOUBLE," +
                "z DOUBLE," +
                "yaw FLOAT," +
                "pitch FLOAT," +
                "name TEXT," +
                "description TEXT," +
                "type TEXT DEFAULT 'MISC'," +
                "UNIQUE (name)," +
                "UNIQUE (warpID))";
        try (PreparedStatement stmt = connection.prepareStatement(warpTable)) {
            stmt.execute();
            int updates = stmt.getUpdateCount();
            LogUtil.log("Table was created with " + updates + " elements affected.");
        } catch (SQLException e) {
            dbLogErr(e);
        }
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userID TEXT," +
                "username TEXT," +
                "max INT," +
                "UNIQUE (userID))";
        try (PreparedStatement stmt = connection.prepareStatement(userTable)) {
            stmt.execute();
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void close() {
        if (!isOpen()) return;
        try {
            connection.close();
            LogUtil.log("Local database connection closed.", new PlayerWarps(), Level.INFO, true);
        } catch (SQLException e) {
            dbLogErr(e);
        }
    }

    @Override
    public Warp warp(String name) {
        if (!isOpen()) return null;
        String sql = "SELECT userID, warpID, world, x, y, z, yaw, pitch, description, type FROM warps WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                WarpUser owner = new WarpUser(DataUtil.stringToUUID("userID"));
                World world = Bukkit.getWorld(resultSet.getString("world"));
                if (world == null) {
                    LogUtil.log("Couldn't find a valid world for this warp.", new PlayerWarps(), Level.WARNING, true);
                    continue;
                }
                double x = resultSet.getDouble("x"),
                        y = resultSet.getDouble("y"),
                        z = resultSet.getDouble("z");
                float yaw = resultSet.getFloat("yaw"),
                        pitch = resultSet.getFloat("pitch");
                String description = resultSet.getString("description");
                WarpType warpType = WarpType.get(resultSet.getString("type"));
                UUID warpID = DataUtil.stringToUUID(resultSet.getString("warpID"));
                if (warpID == null) {
                    LogUtil.log("The UUID associated with this warp is invalid or does not exist", new PlayerWarps(), Level.WARNING, true);
                    continue;
                }
                Location resultingLocation = new Location(world, x, y, z, yaw, pitch);
                return new Warp(owner, resultingLocation, name, description, warpType, warpID);
            }
        } catch (SQLException e) {
            dbLogErr(e);
        }
        return null;
    }

    @Override
    public WarpUser owner(UUID warpID) {
        if (!isOpen()) return null;
        if (userCache.available()) {
            WarpUser warpUser = userCache.get(warpID);
            if (warpUser != null) return warpUser;
        }
        String sql = "SELECT userID FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpID.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) return new WarpUser(resultSet.getString("userID"));
        } catch (SQLException e) {
            dbLogErr(e);
        }
        return null;
    }

    @Override
    public Location location(UUID warpID) {
        String sql = "SELECT world, x, y, z, yaw, pitch FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpID.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                // World
                String worldString = resultSet.getString("world");
                World world = Bukkit.getWorld(worldString);
                if (world == null) return null;

                // Coords
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");

                // Face
                float yaw = resultSet.getFloat("yaw");
                float pitch = resultSet.getFloat("pitch");

                return new Location(world, x, y, z, yaw, pitch);
            }
        } catch (SQLException e) {
            dbLogErr(e);
            return null;
        }
        return null;
    }

    @Override
    public String name(UUID warpID) {
        if (!isOpen()) return null;
        if (nameCache.available()) {
            LogUtil.log("Cache is available! Searching for a match...", new PlayerWarps(), Level.INFO, true);
            String name = nameCache.get(warpID);
            if (name != null) return name;
        }
        String sql = "SELECT name FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpID.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
        return null;
    }

    @Override
    public String description(UUID warpID) {
        if (!isOpen()) return null;
        String sql = "SELECT description FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpID.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) return resultSet.getString("description");
        } catch (SQLException e) {
            dbLogErr(e);
        }
        return null;
    }

    @Override
    public WarpType warpType(UUID warpID) {
        if (!isOpen()) return null;
        String sql = "SELECT type FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpID.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) return WarpType.get(resultSet.getString("type"));
        } catch (SQLException e) {
            dbLogErr(e);
        }
        return null;
    }

    // Keeping this empty since I want to change some properties about this
    @Override
    public @Nullable Rating rating(UUID warpID) {
        return null;
    }

    @Override
    public List<Warp> warps(WarpUser warpUser) {
        if (!isOpen()) return List.of();
        List<Warp> warps = new ArrayList<>();
        String sql = "SELECT warpID FROM warps WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpUser.userID().toString());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                UUID uuid = DataUtil.stringToUUID(resultSet.getString("warpID"));
                if (uuid == null) continue;

                String name = name(uuid);
                String description = description(uuid);
                Location location = location(uuid);
                WarpType warpType = warpType(uuid);

                Warp result = new Warp(warpUser, location, name, description, warpType, uuid);
                warps.add(result);
            }
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
        return warps;
    }

    public List<Warp> warps() {
        if (!isOpen()) return List.of();
        List<Warp> warps = new ArrayList<>();
        String sql = "SELECT * FROM warps";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                UUID uuid = DataUtil.stringToUUID(resultSet.getString("warpID"));
                if (uuid == null) continue;

                WarpUser warpUser = owner(uuid);
                String name = name(uuid);
                String description = description(uuid);
                Location location = location(uuid);
                WarpType warpType = warpType(uuid);

                Warp warp = new Warp(warpUser, location, name, description, warpType, uuid);
                warps.add(warp);
            }
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
        return warps;
    }

    @Override
    public void setOwner(WarpUser owner, UUID warpID) {
        if (!isOpen()) return;
        String sql = "UPDATE warps SET userID = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, owner.user().getUniqueId().toString());
            stmt.setString(2, warpID.toString());
            stmt.executeUpdate();

            userCache.update(warpID, owner);
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void setLocation(Location location, UUID warpID) {
        if (!isOpen()) return;
        String sql = "UPDATE warps SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String world = location.getWorld().getName();
            stmt.setString(1, world);

            double x = location.x();
            stmt.setDouble(2, x);

            double y = location.y();
            stmt.setDouble(3, y);

            double z = location.z();
            stmt.setDouble(4, z);

            float yaw = location.getYaw();
            stmt.setFloat(5, yaw);

            float pitch = location.getPitch();
            stmt.setFloat(6, pitch);

            String id = warpID.toString();
            stmt.setString(7, id);

            stmt.executeUpdate();
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void setName(String name, UUID warpID) {
        if (!isOpen()) return;
        String sql = "UPDATE warps SET name = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, warpID.toString());
            stmt.executeUpdate();
            nameCache.update(warpID, name);
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void setDescription(String description, UUID warpID) {
        if (!isOpen()) return;
        String sql = "UPDATE warps SET description = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setString(2, warpID.toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void setWarpType(WarpType warpType, UUID warpID) {
        if (!isOpen()) return;
        String sql = "UPDATE warps SET type = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpType.name());
            stmt.setString(2, warpID.toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    @Override
    public void setRating(@Nullable Rating rating, UUID warpID) {

    }

    @Override
    public boolean save(Warp warp) {
        if (!isOpen()) return false;
        String sql = "INSERT INTO warps (userID, warpID, world, x, y, z, yaw, pitch, name, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String ownedId = warp.owner().userID().toString();
            stmt.setString(1, ownedId);

            stmt.setString(2, warp.uuid().toString());

            World world = warp.location().getWorld();
            if (world == null) return false;
            stmt.setString(3, world.getName());
            stmt.setDouble(4, warp.location().x());
            stmt.setDouble(5, warp.location().y());
            stmt.setDouble(6, warp.location().z());
            stmt.setFloat(7, warp.location().getYaw());
            stmt.setFloat(8, warp.location().getPitch());

            stmt.setString(9, warp.name());
            stmt.setString(10, warp.getDescription());

            userCache.update(warp.uuid(), warp.owner());
            nameCache.update(warp.uuid(), warp.name());
            int updates = stmt.executeUpdate();
            LogUtil.log("Actions completed: " + updates, new PlayerWarps(), Level.INFO, true);
            return updates > 0;
        } catch (Exception exception) {
            dbLogErr(exception);
        }
        return false;
    }

    @Override
    public boolean update(Warp warp) {
        if (!isOpen()) return false;
        String sql = "UPDATE warps SET userID = ?, world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?, name = ?, description = ? WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Owner
            String ownerId = warp.owner().userID().toString();
            stmt.setString(1, ownerId);

            // Location
            String worldName = warp.location().getWorld().getName();
            stmt.setString(2, worldName);

            double x = warp.location().x();
            stmt.setDouble(3, x);
            double y = warp.location().y();
            stmt.setDouble(4, y);
            double z = warp.location().z();
            stmt.setDouble(5, z);

            float yaw = warp.location().getYaw();
            stmt.setFloat(6, yaw);
            float pitch = warp.location().getPitch();
            stmt.setFloat(7, pitch);

            // Name
            stmt.setString(8, warp.name());
            // Description
            stmt.setString(9, warp.getDescription());

            stmt.setString(10, warp.uuid().toString());

            userCache.update(warp.uuid(), warp.owner());
            nameCache.update(warp.uuid(), warp.name());
            int updates = stmt.executeUpdate();
            LogUtil.log("Successfully saved warp " + warp.name() + " to the database.", new PlayerWarps(), Level.INFO, true);
            return updates > 0;
        } catch (Exception exception) {
            dbLogErr(exception);
        }
        return false;
    }

    @Override
    public void remove(Warp warp) {
        if (!isOpen()) return;
        String sql = "DELETE FROM warps WHERE warpID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warp.uuid().toString());
            int updates = stmt.executeUpdate();
            nameCache.remove(warp.uuid());
            userCache.remove(warp.uuid());

            LogUtil.log("Deletion occurred, " + updates + " rows affected.", new PlayerWarps(), Level.INFO, true);

        } catch (SQLException exception) {
            dbLogErr(exception);
        }
    }

    // ** STUFF TO GET WARP USER **

    public boolean save(WarpUser warpUser) {
        if (!isOpen()) return false;
        String sql = "INSERT INTO users (userID, username, max) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, warpUser.userID().toString());
            stmt.setString(2, warpUser.name());
            stmt.setInt(3, warpUser.getLimit());

            int updates = stmt.executeUpdate();
            LogUtil.log("WarpUser save successful: " + updates + " rows affected.");
            return updates > 0;
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
        return false;
    }

    public WarpUser retrieve(UUID userID) {
        if (!isOpen()) return null;
        String sql = "SELECT username, max FROM users WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userID.toString());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userID);
                int limit = resultSet.getInt("max");
                WarpUser warpUser = new WarpUser(offlinePlayer);
                warpUser.setLimit(limit);
                return warpUser;
            }
        } catch (SQLException exception) {
            dbLogErr(exception);
        }
        return null;
    }
}
