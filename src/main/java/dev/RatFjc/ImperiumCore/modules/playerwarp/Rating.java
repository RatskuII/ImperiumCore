package dev.RatFjc.ImperiumCore.modules.playerwarp;

import org.jetbrains.annotations.Range;

/**
 * Represents a rating of a warp.
 */
public class Rating {

    private int stars;
    private String reason = "";

    public Rating(int stars, String reason) {
        this.stars = stars;
        this.reason = reason;
    }

    public Rating(int stars) {
        this.stars = stars;
    }

    @Range(from = 0, to = 5)
    public int getStars() {
        return this.stars;
    }

    public void setStars(int stars) {
        if (stars > 5) throw new IllegalArgumentException("Star rating cannot be greater than 5");
        if (stars < 0) throw new IllegalArgumentException("Star rating cannot be negative.");
        this.stars = stars;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
