package dev.RatFjc.ImperiumCore.modules.ultrabans.data.files;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.init.UltraBans;
import dev.RatFjc.ImperiumCore.modules.ultrabans.Punishment;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentSource;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.PunishmentType;
import dev.RatFjc.ImperiumCore.modules.ultrabans.data.Tag;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class PunishmentSaver extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "punishment-entries.yml");

    private static final FileConfiguration fileConfiguration = build(file);

    /**
     * Asynchronously saves the provided entry to the file.
     * @param punishment The punishment instance to save
     * @return A {@link CompletableFuture}, which when complete, will return whether the operation was successful.
     * @param <T> The target type
     */
    public static <T> CompletableFuture<Boolean> savePunishment(Punishment<T> punishment) {
        if (fileConfiguration == null) return nullFail("The configuration is unavailable.");
        return CompletableFuture.supplyAsync(() -> {

            String target = punishment.target().toString();
            if (punishment.target() instanceof Player player) {
                target = player.getName();
            }
            if (punishment.target() instanceof String string) {
                target = string;
            }

            String type = punishment.type().name();
            String source = punishment.source().name();
            String reason = punishment.reason();
            String localTime = punishment.time().toString();


            String duration = "INF";
            if (punishment.duration().isPresent()) duration = punishment.duration().get().toString();

            Collection<Tag> tags = punishment.tags();

            String uuid = punishment.uuid().toString();

            String header = "";
            switch (punishment.type()) {
                case BAN -> header = "ban.";
                case MUTE -> header = "mute.";
                case WARN -> header = "warn.";
                case KICK -> header = "kick.";
            }

            String path = header + target + "." + uuid;
            fileConfiguration.set(path + ".type", type);
            fileConfiguration.set(path + ".source", source);
            fileConfiguration.set(path + ".reason", reason);
            fileConfiguration.set(path + ".time", localTime);
            fileConfiguration.set(path + ".duration", duration);
            fileConfiguration.set(path + ".tags", tags.stream()
                    .map(Tag::tag)
                    .toList());

            save(file, fileConfiguration, UltraBans.executor());
            ConfigurationSection section = fileConfiguration.getConfigurationSection(path);
            return section != null;
        }, UltraBans.executor());
    }

    /**
     * Asynchronously checks whether the provided target has an active entry.
     * @param target The target to check
     * @param type The type of punishment to look for
     * @return A {@link CompletableFuture}, which when complete, returns whether this target has a valid entry.
     */
    public static CompletableFuture<Boolean> hasPunishment(String target, PunishmentType type) {
        if (fileConfiguration == null) return null;
        return CompletableFuture.supplyAsync(() -> {
            String path;
            switch (type) {
                case BAN -> path = "ban." + target;
                case KICK -> path = "kick." + target;
                case WARN -> path = "warn." + target;
                case MUTE -> path = "mute." + target;
                default -> path = "";
            }
            ConfigurationSection playerSection = fileConfiguration.getConfigurationSection(path + "." + target);
            if (playerSection == null) return false;

            for (String uuidKey : playerSection.getKeys(false)) {
                ConfigurationSection section = fileConfiguration.getConfigurationSection(uuidKey);
                if (section == null) continue;

                String time = section.getString(path + ".time");
                String duration = section.getString(path + ".duration");

                if (time == null || duration == null) continue;
                LocalDateTime createdTime = LocalDateTime.parse(time);
                Duration validDuration;
                if (duration.equals("INF")) validDuration = Duration.ZERO;
                else validDuration = Duration.parse(duration);

                LocalDateTime expiry = createdTime.plus(validDuration);
                Duration remaining = Duration.between(LocalDateTime.now(), expiry);
                if (remaining.isNegative()) remaining = Duration.ZERO;

                // If the duration is finished, the entry has expired and should be skipped
                if (remaining.isZero() || remaining.isNegative()) continue;

                return true;
            }
            return false;
        }, UltraBans.executor());

    }


    public static CompletableFuture<@Nullable Punishment<String>> getPunishment(String target, PunishmentType type) {
        return CompletableFuture.supplyAsync(() -> {
            String path;
            switch (type) {
                case BAN -> path = "ban." + target;
                case KICK -> path = "kick." + target;
                case WARN -> path = "warn." + target;
                case MUTE -> path = "mute." + target;
                default -> path = "";
            }


            ConfigurationSection playerSection = fileConfiguration.getConfigurationSection(path + "." + target);
            if (playerSection == null) return null;
            for (String uuidKey : playerSection.getKeys(false)) {
                ConfigurationSection section = fileConfiguration.getConfigurationSection(uuidKey);
                if (section == null) continue;

                String source = section.getString(path + ".source");
                String reason = section.getString(path + ".reason");
                String time = section.getString(path + ".time");
                String duration = section.getString(path + ".duration");
                List<String> tags = section.getStringList(path + ".tags");

                PunishmentSource punishmentSource = PunishmentSource.get(source);

                if (time == null || duration == null) continue;
                LocalDateTime createdTime = LocalDateTime.parse(time);
                Duration validDuration;
                if (duration.equals("INF")) validDuration = Duration.ZERO;
                else validDuration = Duration.parse(duration);

                LocalDateTime expiry = createdTime.plus(validDuration);
                Duration remaining = Duration.between(LocalDateTime.now(), expiry);
                if (remaining.isNegative()) remaining = Duration.ZERO;

                // If the duration is finished, the entry has expired and should be skipped
                if (remaining.isZero() || remaining.isNegative()) continue;
                List<Tag> newTags = tags.stream()
                        .filter(Objects::nonNull)
                        .map(Tag::get)
                        .toList();
                UUID uuid = UUID.fromString(uuidKey);
                return new Punishment<>(target, type, punishmentSource, uuid, reason, remaining, newTags);
            }

            return null;
        }, UltraBans.executor());
    }


    @Override
    protected void set() {

    }
}
