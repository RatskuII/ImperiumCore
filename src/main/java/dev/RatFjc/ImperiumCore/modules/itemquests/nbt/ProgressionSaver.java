package dev.RatFjc.ImperiumCore.modules.itemquests.nbt;

import dev.RatFjc.ImperiumCore.modules.itemquests.progress.ProgressionSnapshot;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ProgressionSaver implements PersistentDataType<byte[], ProgressionSnapshot> {

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ProgressionSnapshot> getComplexType() {
        return ProgressionSnapshot.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ProgressionSnapshot complex, @NotNull PersistentDataAdapterContext context) {
        return ProgressionSnapshot.serialize(complex);
    }

    @Override
    public @NotNull ProgressionSnapshot fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        return ProgressionSnapshot.deserialize(primitive);
    }
}
