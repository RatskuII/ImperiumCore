package dev.RatFjc.ImperiumCore.modules.bettergod.data;

public enum Context {

    FULL,
    VANILLA,
    ENVIRONMENT,
    COMBAT;

    public static Context getFromOrdinal(int ordinal) {
        for (Context context : Context.values()) {
            if (context.ordinal() == ordinal) return context;
        }
        return VANILLA;
    }

    public static Context fromString(String data) {
        for (Context context : Context.values()) {
            if (context.name().equals(data.toUpperCase())) return context;
        }
        return null;
    }
}
