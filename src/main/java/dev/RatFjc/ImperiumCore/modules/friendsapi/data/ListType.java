package dev.RatFjc.ImperiumCore.modules.friendsapi.data;

import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface ListType<P, C> extends PersistentDataType<P, C> {

    ListPersistentDataType<String, String> STRING = ListType.LIST.strings();
    ListPersistentDataType<String, UUID> IDENTIFIERS = ListPersistentDataType.LIST.listTypeFrom(new UUIDHolder());

    class UUIDHolder implements PersistentDataType<String, UUID> {

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<UUID> getComplexType() {
            return UUID.class;
        }

        @Override
        public @NonNull String toPrimitive(@NonNull UUID complex, @NotNull PersistentDataAdapterContext context) {
            return complex.toString();
        }

        @Override
        public @NonNull UUID fromPrimitive(@NonNull String primitive, @NotNull PersistentDataAdapterContext context) {
            UUID result = DataUtil.stringToUUID(primitive);
            if (result == null) result = UUID.randomUUID();
            return result;
        }
    }
}
