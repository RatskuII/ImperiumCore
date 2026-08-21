package dev.RatFjc.ImperiumCore.modules.itemquests.progress;

import dev.RatFjc.ImperiumCore.modules.itemquests.QuestState;
import dev.RatFjc.ImperiumCore.modules.itemquests.constuct.QuestItem;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ProgressionSnapshot extends Progression {

    // Typically, the uuid should match the questItem this progression is associated with.
    // But I still need to declare it somewhere
    private UUID uuid = UUID.randomUUID();

    protected ProgressionSnapshot(float progress, float total, QuestState state) {
        super(progress, total, state);
    }

    protected ProgressionSnapshot(float progress, float total) {
        super(progress, total);
    }

    protected ProgressionSnapshot(float total) {
        super(total);
    }

    protected ProgressionSnapshot(Progression progression, UUID uuid) {
        super(progression.getProgress(), progression.getTotal(), progression.getQuestState());
        this.uuid = uuid;
    }

    protected ProgressionSnapshot(float progress, float total, QuestState state, UUID uuid) {
        super(progress, total, state);
        this.uuid = uuid;
    }

    public final UUID uuid() {
        return this.uuid;
    }

    /**
     * Creates a new, immutable snapshot of the provided {@link Progression} and associates it with a UUID from
     * the provided {@link QuestItem}.
     * @param progression The progression state
     * @param questItem The quest item
     * @return A non-null snapshot of the {@link Progression}
     * @apiNote Changes made to this snapshot may not reflect the actual progression.
     */
    public static ProgressionSnapshot snapshot(Progression progression, QuestItem questItem) {
        UUID uuid = questItem.uuid();
        return new ProgressionSnapshot(progression, uuid);
    }

    public static byte[] serialize(ProgressionSnapshot snapshot) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[32]);
        buffer.putFloat(snapshot.getProgress());
        buffer.putFloat(snapshot.getTotal());
        buffer.putInt(snapshot.getQuestState().ordinal());
        return buffer.array();
    }

    public static ProgressionSnapshot deserialize(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        float progress = buffer.getFloat();
        float total = buffer.getFloat();
        QuestState state = QuestState.getFromOrdinal(buffer.getInt());
        return new ProgressionSnapshot(progress, total, state);
    }
}
