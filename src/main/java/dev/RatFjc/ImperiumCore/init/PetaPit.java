package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;

public class PetaPit extends Module {

    @Override
    public String name() {
        return "PetaPit";
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public void load(ImperiumCore instance) {

    }

}
