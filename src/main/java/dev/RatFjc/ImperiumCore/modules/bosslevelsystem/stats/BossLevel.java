package dev.RatFjc.ImperiumCore.modules.bosslevelsystem.stats;

/**
 * Represents a boss level, which also holds the minimum xp required to
 * reach a certain level.
 */
public enum BossLevel {

    STARTER(750),
    SECONDARY(1650),
    INTERMEDIATE(4500),
    SKILLED(8790),
    VETERAN(15730),
    MASTER(32560),
    GODLIKE(68350);

    private final double minXP;

    BossLevel(double minXP) {
        this.minXP = minXP;
    }

    public static BossLevel get(byte level) {
        for (BossLevel bossLevel : BossLevel.values()) {
            int ordinal = bossLevel.ordinal() + 1;
            if ((byte) ordinal == level) return bossLevel;
        }
        return STARTER;
    }

    public double getMinXP() {
        return this.minXP;
    }

    public byte getLevel() {
        return (byte) (ordinal() + 1);
    }
}
