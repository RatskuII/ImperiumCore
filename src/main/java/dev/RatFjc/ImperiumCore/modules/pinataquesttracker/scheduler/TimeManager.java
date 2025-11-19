package dev.RatFjc.ImperiumCore.modules.pinataquesttracker.scheduler;

public abstract class TimeManager {

    //Ticks
    private static long timer_end = 0;
    private static final long CD = 10;

    /**
     * Check if the cooldown has completed
     * @return True if the cooldown was completed, false otherwise
     */
    public static boolean checkCooldown() {
        return System.currentTimeMillis() >= timer_end;
    }

    public static void setCooldown(long cooldown) {
        timer_end = System.currentTimeMillis() + cooldown;
    }

    public static void reset() {
        setCooldown(CD);
    }

    public static long getRemainingTime() {
        long remaining = timer_end - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }

}
