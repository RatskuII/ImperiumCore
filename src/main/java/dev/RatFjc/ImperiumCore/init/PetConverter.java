package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.petconverter.command.PetGUICommand;
import dev.RatFjc.ImperiumCore.modules.petconverter.listener.PetExchangerInventoryListener;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

public class PetConverter extends Module {
    @Override
    public String name() {
        return "PetConverter";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerEvent(new PetExchangerInventoryListener());
        EventUtil.registerCommand(new PetGUICommand(), "pet-exchanger");
    }
}
