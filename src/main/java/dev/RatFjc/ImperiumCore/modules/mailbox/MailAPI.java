package dev.RatFjc.ImperiumCore.modules.mailbox;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Simple API for other plugins to send/get mail.
 */
public class MailAPI {

    private final MailDatabase db;
    private final MailCommands commands;

    public MailAPI(MailDatabase db, MailCommands commands) {
        this.db = db;
        this.commands = commands;
    }

    // send system/admin/plugin mail (does not remove items from a player)
    public boolean sendSystemMail(UUID targetUuid, ItemStack item, String sourceName) {
        return db.saveItem(targetUuid.toString(), "plugin:" + sourceName, item);
    }

    public boolean sendAdminMail(UUID targetUuid, ItemStack item) {
        return db.saveItem(targetUuid.toString(), "admin", item);
    }

    // a convenience for other plugins to simulate player-sent mail (does not remove player's items)
    // If other plugin wants removal, it should handle the player's inventory.
    public boolean sendPlayerMail(UUID senderUuid, UUID targetUuid, ItemStack item) {
        return db.saveItem(targetUuid.toString(), senderUuid.toString(), item);
    }

    public List<MailItem> getMail(UUID playerUuid, SortingMode mode, int page) {
        var recs = db.loadPage(playerUuid.toString(), page, modeToString(mode));
        return MailItem.fromRecords(recs);
    }

    private String modeToString(SortingMode m) {
        return switch (m) {
            case OLDEST -> "oldest";
            case SENDER -> "sender";
            default -> "newest";
        };
    }
}