package dev.RatFjc.ImperiumCore.modules.mailbox;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Lightweight DTO representing a mail item for GUI. */
public class MailItem {

    private final long id;
    private final String sentBy; // uuid, "admin", or "plugin:name"
    private final long timestamp;
    private final ItemStack item;

    public MailItem(long id, String sentBy, long timestamp, ItemStack item) {
        this.id = id;
        this.sentBy = sentBy;
        this.timestamp = timestamp;
        this.item = item;
    }

    public long getId() { return id; }
    public String getSentBy() { return sentBy; }
    public long getTimestamp() { return timestamp; }
    public ItemStack getItem() { return item; }

    // convert DB records to MailItem list
    public static List<MailItem> fromRecords(List<MailDatabase.Record> recs) {
        List<MailItem> out = new ArrayList<>();
        for (MailDatabase.Record r : recs) out.add(new MailItem(r.id, r.sentBy, r.timestamp, r.item));
        return out;
    }
}
