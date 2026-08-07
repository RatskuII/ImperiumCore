package dev.RatFjc.ImperiumCore.modules.playerwarp.data;

/**
 * Represents a category of types a warp can be.
 */
public enum WarpType {

    /**
     * The warp features some kind of shop or transactional element.
     */
    SHOP,

    /**
     * The warp features a build or construct.
     */
    ARCHITECTURE,

    /**
     * The warp is primarily dedicated for combat.
     */
    ARENA,

    /**
     * The warp doesn't fit into any other category. How unique.
     */
    MISC;

    public static WarpType get(String type) {
        for (WarpType warpType : WarpType.values()) {
            if (warpType.name().equalsIgnoreCase(type)) return warpType;
        }
        return MISC;
    }
}
