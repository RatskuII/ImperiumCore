package dev.RatFjc.ImperiumCore.modules.ultrabans;

import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Operation;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentSource;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentType;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Tag;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.files.PunishmentSaver;
import dev.RatFjc.ImperiumCore.modules.ultrabans.event.PunishmentEvent;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

/**
 * A builder class that constructs a punishment entry, then applies it on the given target.
 * @param <T> The target type
 */
public class PunishmentBuilder<T> {

    private T target;
    private PunishmentType type;
    private PunishmentSource source;
    private String reason;
    private @Nullable Duration duration;
    private Collection<Tag> tags;

    public PunishmentBuilder<T> target(T target) {
        this.target = target;
        return this;
    }

    public PunishmentBuilder<T> type(PunishmentType type) {
        this.type = type;
        return this;
    }

    public PunishmentBuilder<T> source(PunishmentSource source) {
        this.source = source;
        return this;
    }

    public PunishmentBuilder<T> reason(String reason) {
        this.reason = reason;
        return this;
    }

    public PunishmentBuilder<T> duration(@Nullable Duration duration) {
        this.duration = duration;
        return this;
    }

    public PunishmentBuilder<T> tags(Collection<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public PunishmentBuilder<T> tags(Tag... tags) {
        this.tags = Arrays.asList(tags);
        return this;
    }

    /**
     * Constructs a new Punishment instance using data provided by the builder. The builder will also run
     * all necessary operations to carry out the punishment as needed (unless the event is canceled).
     * @return A new {@link Punishment}.
     */
    public Punishment<T> build() {

        Punishment<T> punishment = new Punishment<>(target, type, source, reason, duration, tags);
        PunishmentEvent<T> punishmentEvent = new PunishmentEvent<>(punishment);
        BukkitUtil.callEvent(punishmentEvent);
        if (punishmentEvent.isCancelled()) return punishment;
        switch (type) {
            case BAN -> Operation.ban(punishment);
            case MUTE -> Operation.mute(punishment);
            case KICK -> Operation.kick(punishment);
            case WARN -> Operation.warn(punishment);
        }

        PunishmentSaver.savePunishment(punishment);
        return punishment;
    }

    public Punishment<Player> assemble() {
        if (!(target instanceof Player player)) {
            throw new IllegalCallerException("Bad call.");
        }
        Punishment<Player> punishment = new Punishment<>(player, type, source, reason, duration, tags);
        PunishmentEvent<Player> punishmentEvent = new PunishmentEvent<>(punishment);
        BukkitUtil.callEvent(punishmentEvent);
        if (punishmentEvent.isCancelled()) return punishment;

        switch (type) {
            case BAN -> Operation.ban(punishment);
            case MUTE -> Operation.mute(punishment);
            case KICK -> Operation.kick(punishment);
            case WARN -> Operation.warn(punishment);
        }
        PunishmentSaver.savePunishment(punishment);
        return punishment;
    }
}
