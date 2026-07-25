package dev.RatFjc.ImperiumCore.modules.bettergod.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ContextPDC implements PersistentDataType<byte[], Context> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Context> getComplexType() {
        return Context.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Context complex, @NotNull PersistentDataAdapterContext context) {
        return new byte[]{(byte) complex.ordinal()};
    }

    @Override
    public @NotNull Context fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        return Context.getFromOrdinal(primitive[0]);
    }
}
