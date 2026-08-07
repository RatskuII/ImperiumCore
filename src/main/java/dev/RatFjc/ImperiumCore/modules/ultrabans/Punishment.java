package dev.RatFjc.ImperiumCore.modules.ultrabans;

import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Operation;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentSource;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentType;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Tag;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a punishment entry. An entry contains basic information about the punishment of a player. It should be
 * noted that the creation of a punishment entry by itself won't apply the punishment. To apply it, use this punishment
 * instance as a parameter in {@link Operation}. Alternatively, use the {@link PunishmentBuilder} to execute the operation
 * immediately.
 * @param <T> The type that this punishment is applicable to.
 */
public class Punishment<T> {

    private final T target;
    private final PunishmentType type;
    private final PunishmentSource source;
    private UUID uuid;
    private final String reason;
    private final @Nullable Duration duration;
    private final Collection<Tag> tags;

    public Punishment(T target, PunishmentType type) {
        this.target = target;
        this.type = type;
        this.source = PunishmentSource.CONSOLE;
        this.reason = "";
        this.duration = null;
        this.tags = List.of();
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.reason = "";
        this.duration = null;
        this.tags = List.of();
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source, String reason) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.reason = reason;
        this.duration = null;
        this.tags = List.of();
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source, String reason, @Nullable Duration duration) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.reason = reason;
        this.duration = duration;
        this.tags = List.of();
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source, String reason, @Nullable Duration duration, Tag... tags) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.reason = reason;
        this.duration = duration;
        this.tags = Arrays.asList(tags);
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source, String reason, @Nullable Duration duration, Collection<Tag> tags) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.reason = reason;
        this.duration = duration;
        this.tags = tags;
    }

    public Punishment(T target, PunishmentType type, PunishmentSource source, UUID uuid, String reason, @Nullable Duration duration, Collection<Tag> tags) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.uuid = uuid;
        this.reason = reason;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * Represents the target of this entry. The name may or may not match a player.
     * @return The target
     */
    public T target() {
        return this.target;
    }

    public PunishmentType type() {
        return this.type;
    }

    /**
     * The source of this entry. Defaults to console.
     * @return The entry source
     */
    public PunishmentSource source() {
        return this.source;
    }

    /**
     * A UUID to assign to this entry. This will make it easier to store primitively.
     * @return A valid UUID
     */
    public UUID uuid() {
        if (uuid == null) return UUID.randomUUID();
        return uuid;
    }

    /**
     * The reason for this entry.
     * @return A reason
     */
     public String reason() {
         return this.reason;
     }

    /**
     * The time this entry was created.
     * @return A valid {@link LocalDateTime} representing the time of creation
     */
    public LocalDateTime time() {
        return LocalDateTime.now();
    }

    /**
     * The duration of this entry, if applicable
     * @return A valid duration, or empty to represent a permanent entry
     */
    public Optional<Duration> duration() {
        return Optional.ofNullable(duration);
    }

    /**
     * Any additional tags to add to this entry. Can be left empty.
     * @return A collection of tags, or empty if none are present
     */
    public Collection<Tag> tags() {
        return this.tags;
    }

}
