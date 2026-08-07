package dev.RatFjc.ImperiumCore.modules.ultrabans.data;

/**
 * Represents the type of punishment.
 */
public enum PunishmentType {

    /**
     * Blacklists a player from the server, either temporarily or permanently.
     */
    BAN,

    /**
     * Prevents a player from sending messages or commands in the server.
     */
    MUTE,

    /**
     * Issues a warning to a player.
     */
    WARN,

    /**
     * Forcefully disconnects a player from the server.
     */
    KICK;
}
