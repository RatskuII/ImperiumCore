package dev.RatFjc.ImperiumCore.modules.playerwarp.database;

import dev.RatFjc.ImperiumCore.ImperiumCore;

public interface DatabaseType {

    ImperiumCore plugin = ImperiumCore.getInstance();

    String type();

    void connection();

    void initialize();

    boolean isOpen();
}
