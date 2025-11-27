package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.file.FileBuilder;
import dev.RatFjc.ImperiumCore.init.interfaces.Module;

public class PinataQuestCounter implements Module {

    @Override
    public String name() {
        return "PinataQuestCounter";
    }

    @Override
    public void load(ImperiumCore instance) {
        FileBuilder builder = instance.getFileBuilder();
    }
}