package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.modules.pinatacounter.Counter;
import dev.RatFjc.ImperiumCore.modules.pinatacounter.CounterSaver;
import dev.RatFjc.ImperiumCore.utility.EventUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PinataQuestCounter extends Module implements AsyncModule<ScheduledExecutorService> {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String name() {
        return "PinataQuestCounter";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        EventUtil.registerEvent(new Counter());
        instance.saveResource("pinata.yml", false);
        CounterSaver.set();
    }

    @Override
    public Module module() {
        return this;
    }

    @Override
    public ScheduledExecutorService worker() {
        return EXECUTOR;
    }

    public static Executor executor() {
        return EXECUTOR;
    }
}
