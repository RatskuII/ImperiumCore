package dev.RatFjc.ImperiumCore.modules.ultrabans.data;

/**
 * Represents the source of a punishment entry.
 */
public enum PunishmentSource {

    /**
     * The source was from a player, typically from executing a command.
     */
    PLAYER,

    /**
     * The source was from the console.
     */
    CONSOLE,

    /**
     * The source was from an automated script or function.
     */
    AUTO,

    /**
     * The source is unknown.
     */
    UNKNOWN;

    public static PunishmentSource get(String name) {
        for (PunishmentSource source : PunishmentSource.values()) {
            if (source.name().equalsIgnoreCase(name)) return source;
        }
        return UNKNOWN;
    }
}
