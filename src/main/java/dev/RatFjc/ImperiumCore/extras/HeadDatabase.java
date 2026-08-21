package dev.RatFjc.ImperiumCore.extras;

import dev.RatFjc.ImperiumCore.ImperiumCore;

public interface HeadDatabase {

    ImperiumCore plugin = ImperiumCore.getInstance();

    String type();

    void connection();

    void initialize();

    void close();

    boolean isOpen();
}
