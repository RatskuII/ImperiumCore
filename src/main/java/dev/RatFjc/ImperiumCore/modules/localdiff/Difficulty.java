package dev.RatFjc.ImperiumCore.modules.localdiff;

import org.bukkit.entity.Player;

import java.util.Optional;

public abstract class Difficulty {

    protected Player player;

    protected Difficulty() {
        this.player = null;
    }

    protected Difficulty(Player player) {
        this.player = player;
    }

    /**
     * How far should the effects of the difficulty increase stretch to?
     * @return A valid range
     */
    public abstract double affectedRange();

    /**
     * Determines the difficulty rating.
     * @return A rating
     */
    public abstract double rating();
}
