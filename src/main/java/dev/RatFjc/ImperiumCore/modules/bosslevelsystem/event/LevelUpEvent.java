package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.event;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.BossProfile;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.stats.BossLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player levels up their {@link BossLevel}.
 */
public class LevelUpEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final BossLevel oldLevel;
    private BossLevel newLevel;

    private final @Nullable String message;

    public LevelUpEvent(Player player, BossLevel oldLevel, @Nullable String message) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.message = message;

        BossProfile bossProfile = new BossProfile(player);
        for (BossLevel level : BossLevel.values()) {
            byte numericLvl = level.getLevel();
            var data = bossProfile.container().get(Keys.BOSS_LVL, PersistentDataType.BYTE);
            if (data != null && numericLvl == data) {
                this.newLevel = level;
                break;
            }
        }
    }

    public Player player() {
        return this.player;
    }

    public BossLevel old() {
        return this.oldLevel;
    }

    public BossLevel updated() {
        return this.newLevel;
    }

    public @Nullable String message() {
        return this.message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
