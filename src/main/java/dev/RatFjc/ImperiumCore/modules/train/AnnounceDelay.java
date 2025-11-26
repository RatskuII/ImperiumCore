package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents the main event handler for the delayed announcements sign
 * (radio)
 */
public class AnnounceDelay extends SignAction implements Listener {

    private final ImperiumCore plugin = ImperiumCore.getInstance();

    /**
     * The message that will be sent to the player.
     */
    static @Nullable String message;
    /**
     * The set of tasks responsible for delivering the text to the player.
     */
    static ScheduledTask[] tasks = null;
    private static final int MAX = 20;

    @Override
    public boolean match(SignActionEvent signActionEvent) {
        return signActionEvent.isType("radio");
    }

    @Override
    public void execute(SignActionEvent executor) {
        if (executor.isTrainSign() && executor.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) {
            sendMessageDelayed(executor, executor.getGroup());
        }
    }

    @Override
    public boolean build(SignChangeActionEvent builder) {
        String delay = builder.getLine(2);
        long value;
        try {
            value = (Long.parseLong(delay) >= 0) ? Long.parseLong(delay) : 0;
        } catch (NumberFormatException exception) {
            builder.getPlayer().sendMessage(Component.text("Your delay value must be a valid number/long."));
            printErr();
            return false;
        }

        builder.getPlayer().sendMessage(Component.text("You set a delay value of " + value));
        return SignBuildOptions.create()
                .setPermission(Permission.BUILD_ANNOUNCER)
                .setName("train announcer (delayed)")
                .setDescription("send a message to players in a train after a specified time has passed.")
                .setTraincartsWIKIHelp("TrainCarts/Signs/Announce")
                .handle(builder);
    }

    /**
     * Sends a delayed message to players in a train.
     * @param event The sign to read data from. This is where you define the delay, in seconds.
     * @param group The MinecartGroup being targeted. This is your train.
     */
    private void sendMessageDelayed(SignActionEvent event, MinecartGroup group) {
        long delay = Long.parseLong(event.getLine(2));
        message = event.getLine(3);

        group.forEach(vehicle -> {
            List<Entity> passenger = vehicle.getEntity().getPassengers();
            passenger.stream()
                    .filter(target -> target instanceof Player)
                    .filter(target -> vehicle.getEntity().getPlayerPassengers().contains(target))
                    .map(target -> (Player) target)
                    .forEach(player -> {
                        if (message == null) return;
                        tasks[0] = player.getScheduler().runDelayed(plugin, task -> player.sendMessage(message), null, delay * 20L);
                    });
        });
    }

    private void printErr() {
        this.plugin.getLogger().warning("Something went wrong while attempting to construct this sign.");
    }
}
