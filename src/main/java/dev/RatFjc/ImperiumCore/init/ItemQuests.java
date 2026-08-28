package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.itemquests.file.QuestEntries;

public class ItemQuests extends Module {
    @Override
    public String name() {
        return "ItemQuests";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    protected void load(ImperiumCore instance) {
        fileSetup(new QuestEntries());
    }
}
