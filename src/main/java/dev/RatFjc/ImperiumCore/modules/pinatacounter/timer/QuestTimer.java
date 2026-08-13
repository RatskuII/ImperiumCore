package dev.RatFjc.ImperiumCore.modules.pinatacounter.timer;

public class QuestTimer {

    private static long end = 0L;
    private static final long CD = 10L;

    public static boolean checkCD() {
        return System.currentTimeMillis() >= end;
    }

    public static void setCD(long cd) {
        end = System.currentTimeMillis() + cd;
    }

    public static void reset() {
        setCD(CD);
    }
}
