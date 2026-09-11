package dev.RatFjc.ImperiumCore.modules.bosslevelsystem;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.event.LevelUpEvent;
import dev.RatFjc.ImperiumCore.modules.bosslevelsystem.stats.BossLevel;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a boss profile, which wraps an {@link OfflinePlayer} with a xp value.
 */
public class BossProfile {

    private final OfflinePlayer player;

    private double xp;

    public BossProfile(String player) {
        this.player = Bukkit.getOfflinePlayer(player);

        var value = container().get(Keys.BOSS_XP, PersistentDataType.DOUBLE);
        if (value == null) this.xp = 0;
        else this.xp = value;
    }

    public BossProfile(OfflinePlayer player) {
        this.player = player;

        var value = container().get(Keys.BOSS_XP, PersistentDataType.DOUBLE);
        if (value == null) this.xp = 0;
        else this.xp = value;
    }

    public BossProfile(OfflinePlayer player, double xp) {
        this(player);
        this.xp = xp;
    }

    /**
     * Gets a {@link Player} representation of the user.
     * @return A valid Player object, or null if the player does not exist or is offline
     */
    public @Nullable Player asPlayer() {
        return player.getPlayer();
    }

    /**
     * Gets a data container, if it's available.
     * @return A persistent data container
     * @throws NullPointerException if the player is offline or does not exist
     */
    public PersistentDataContainer container() {
        Player player1 = asPlayer();
        if (player1 == null) throw new NullPointerException("The owner of this container does not exist, or is offline.");

        return player1.getPersistentDataContainer();
    }

    /**
     * Gets the amount of boss xp this profile has.
     * @return The xp
     */
    public double xp() {
        return this.xp;
    }

    public void setXP(double xp) {
        container().set(Keys.BOSS_XP, PersistentDataType.DOUBLE, xp);
        var initialLevel = container().get(Keys.BOSS_LVL, PersistentDataType.BYTE);
        List<BossLevel> iterator = List.of(BossLevel.values());
        for (BossLevel level : iterator.reversed()) {
            double minimum = level.getMinXP();
            if (initialLevel != null) if (level.getLevel() <= initialLevel) continue;
            if (xp < minimum) continue;
            if (xp >= minimum) {
                container().set(Keys.BOSS_LVL, PersistentDataType.BYTE, level.getLevel());
                byte updated = level.getLevel();

                BossLevel result = BossLevel.get(updated);
                if (result == null) continue;
                LevelUpEvent levelUpEvent = new LevelUpEvent(asPlayer(), result,
                        player.getName() + " has leveled up to " + result.name());
                BukkitUtil.callEvent(levelUpEvent);
            }
        }

        this.xp = xp;
    }

    public void add(double xp) {
        setXP(xp() + xp);
    }

    /**
     * Gets the current {@link BossLevel} this profile has reached.
     * @return A valid BossLevel, or null if no level has been reached yet, typically because they haven't
     * reached {@link BossLevel#STARTER} yet.
     */
    public @Nullable BossLevel getLevel() {
        for (BossLevel level : BossLevel.values()) {
            if (this.xp >= level.getMinXP()) return level;
        }
        return null;
    }

    public double nextLevelUp() {
        if (getLevel() == null) {
            return BossLevel.STARTER.getMinXP() - this.xp;
        }

        return getLevel().getMinXP() - this.xp;
    }


}
