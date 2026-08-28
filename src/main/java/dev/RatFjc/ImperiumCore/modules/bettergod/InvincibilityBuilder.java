package dev.RatFjc.ImperiumCore.modules.bettergod;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.modules.bettergod.data.Context;
import dev.RatFjc.ImperiumCore.modules.bettergod.data.ContextPDC;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * Represents a builder that determines the state of invincibility for a player. Invincibility states are
 * NOT handled via {@link Player#setInvulnerable(boolean)}. Instead, a separate listener determines whether the
 * damage should be canceled.
 */
public class InvincibilityBuilder implements PluginProvider {

    private final Player player;

    private final PersistentDataContainer container;

    private @Nullable String message;

    /**
     * Who's the player being managed here?
     * @param player The player
     */
    public InvincibilityBuilder(Player player) {
        this.player = player;
        this.container = player.getPersistentDataContainer();
    }

    /**
     * Sets the player fully invincible.
     * @return This builder, for chaining
     */
    public InvincibilityBuilder setInvincible() {
        container.set(Keys.GOD, PersistentDataType.BOOLEAN, true);
        container.set(Keys.GOD_CONTEXT, new ContextPDC(), Context.FULL);

        return this;
    }

    /**
     * Sets the player invincible.
     * @param context Context to determine which damage should be blocked
     * @return This builder, for chaining
     */
    public InvincibilityBuilder setInvincible(Context context) {
        container.set(Keys.GOD, PersistentDataType.BOOLEAN, true);
        container.set(Keys.GOD_CONTEXT, new ContextPDC(), context);

        return this;
    }

    /**
     * How long should the invincibility last?
     * @param duration A duration of time
     * @return This builder, for chaining
     */
    public InvincibilityBuilder setDuration(Duration duration) {
        plugin.getServer().getScheduler().runTaskLater(plugin,
                task -> removeInvincibility()
                        .message("Your temporary invincibility has expired (" + duration.toSeconds() + " seconds).")
                        .build(),
                duration.toMillis() / 50);
        return this;
    }

    /**
     * Should the player be notified that their invincibility state has changed?
     * @param message The message to send to the player
     * @return This builder, for chaining
     */
    public InvincibilityBuilder message(String message) {
        this.message = message;

        return this;
    }

    /**
     * Removes invincibility from the player.
     * @return This builder, for chaining
     */
    public InvincibilityBuilder removeInvincibility() {
        container.set(Keys.GOD, PersistentDataType.BOOLEAN, false);
        container.remove(Keys.GOD_CONTEXT);

        return this;
    }

    /**
     * Executes the results of the builder.
     */
    public void build() {
        if (message != null) TextUtil.sendMessage(player, message);
    }
}
