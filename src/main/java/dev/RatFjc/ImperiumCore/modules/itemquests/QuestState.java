package dev.RatFjc.ImperiumCore.modules.itemquests;

public enum QuestState {

    SEALED,
    ACTIVE,
    COMPLETE,
    INVALID;

    public static QuestState getFromOrdinal(int ordinal) {
        switch (ordinal) {
            case 0 -> {return SEALED;}
            case 1 -> {return ACTIVE;}
            case 2 -> {return COMPLETE;}
            default -> {return INVALID;}
        }
    }

}
