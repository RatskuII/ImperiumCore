package dev.RatFjc.ImperiumCore.modules.mailbox;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MailDatabase {

    private final String url;
    private final int pageSize;
    private final int maxPages;

    public MailDatabase(File dbFile, int pageSize, int maxPages) throws SQLException {
        this.url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        this.pageSize = pageSize;
        this.maxPages = maxPages;
        init();
    }

    private void init() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS mailbox (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player TEXT NOT NULL,
                    sent_by TEXT,
                    timestamp INTEGER NOT NULL,
                    item BLOB NOT NULL
                );
                """);
        }
    }

    private byte[] serialize(ItemStack item) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos)) {
            oos.writeObject(item);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ItemStack deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             BukkitObjectInputStream ois = new BukkitObjectInputStream(bais)) {
            return (ItemStack) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveItem(String playerUuid, String sentBy, ItemStack item) {
        byte[] blob = serialize(item);
        if (blob == null) return false;
        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO mailbox(player, sent_by, timestamp, item) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, playerUuid);
                ps.setString(2, sentBy);
                ps.setLong(3, System.currentTimeMillis());
                ps.setBytes(4, blob);
                ps.executeUpdate();
            }
            enforceLimitPerPlayer(conn, playerUuid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Record> loadPage(String playerUuid, int page, String sortMode) {
        int offset = (page - 1) * pageSize;
        String orderBy;
        if ("oldest".equalsIgnoreCase(sortMode)) orderBy = "timestamp ASC";
        else if ("sender".equalsIgnoreCase(sortMode)) orderBy = "sent_by ASC, timestamp DESC";
        else orderBy = "timestamp DESC";

        List<Record> out = new ArrayList<>();
        String sql = "SELECT id, sent_by, timestamp, item FROM mailbox WHERE player = ? ORDER BY " + orderBy + " LIMIT ? OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playerUuid);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String sentBy = rs.getString("sent_by");
                long ts = rs.getLong("timestamp");
                byte[] data = rs.getBytes("item");
                ItemStack item = deserialize(data);
                out.add(new Record(id, sentBy, ts, item));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public int countItems(String playerUuid) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM mailbox WHERE player = ?")) {
            ps.setString(1, playerUuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteById(long id) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM mailbox WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearPlayer(String playerUuid) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM mailbox WHERE player = ?")) {
            ps.setString(1, playerUuid);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearAll() {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM mailbox")) {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void enforceLimitPerPlayer(Connection conn, String playerUuid) throws SQLException {
        int maxItems = pageSize * maxPages;
        int count = 0;
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM mailbox WHERE player = ?")) {
            ps.setString(1, playerUuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        }
        int overflow = count - maxItems;
        if (overflow <= 0) return;
        try (PreparedStatement del = conn.prepareStatement(
                "DELETE FROM mailbox WHERE id IN (SELECT id FROM mailbox WHERE player = ? ORDER BY timestamp ASC LIMIT ?)")) {
            del.setString(1, playerUuid);
            del.setInt(2, overflow);
            del.executeUpdate();
        }
    }

    public static class Record {
        public final long id;
        public final String sentBy;
        public final long timestamp;
        public final ItemStack item;
        public Record(long id, String sentBy, long timestamp, ItemStack item) {
            this.id = id;
            this.sentBy = sentBy;
            this.timestamp = timestamp;
            this.item = item;
        }
    }
}
