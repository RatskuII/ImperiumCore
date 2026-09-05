package dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager;

import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a friend request.
 */
public class FriendRequest implements PluginProvider {

    private final Player sender;
    private final Player receiver;

    private final User s;
    private final User r;

    private AtomicReference<Result> atomicResult = new AtomicReference<>();
    private boolean requestSent = false;

    private long timeout;
    private BukkitTask task;

    public FriendRequest(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.timeout = 60;

        this.s = new User(sender);
        this.r = new User(receiver);
    }

    public FriendRequest(Player sender, Player receiver, long timeout) {
        this(sender, receiver);
        if (timeout <= 0) timeout = 60;
        if (timeout > 360) timeout = 360;
        this.timeout = timeout;
    }

    public void sendRequest() {
        if (!receiver.isOnline()) return;
        TextUtil.sendMessage(sender, "A friend request was sent to " + receiver.getName());

        requestSent = true;
        task = plugin.getServer().getScheduler().runTaskLater(
                plugin, () -> handleRequest(Result.TIMED_OUT), timeout * 20
        );
        Component out = Component
                .text("You have received a friend request from " + sender.getName() + ".")
                .append(
                        Component
                                .text("[Accept]")
                                .color(NamedTextColor.GREEN)
                                .clickEvent(ClickEvent.callback(fn -> handleRequest(Result.ACCEPTED), builder -> builder
                                        .lifetime(Duration.ofSeconds(timeout))
                                        .uses(1)))
                )
                .append(
                        Component
                                .text("[Deny]")
                                .color(NamedTextColor.RED)
                                .clickEvent(ClickEvent.callback(fn -> handleRequest(Result.REJECTED), builder -> builder
                                        .lifetime(Duration.ofSeconds(timeout))
                                        .uses(1)))
                );
        TextUtil.sendMessage(receiver, out);
    }

    private void handleRequest(Result result) {
        if (!requestSent) return;
        if (!atomicResult.compareAndSet(null, result)) return;
        if (s.asPlayer() == null || r.asPlayer() == null) return; // Make sure the players don't go offline before the request is done
        if (task != null) task.cancel();
        switch (result) {
            case ACCEPTED -> {
                atomicResult = new AtomicReference<>(Result.ACCEPTED);
                TextUtil.sendMessage(sender, receiver.getName() + " has accepted your friend request.");
                TextUtil.sendMessage(receiver, "You have accepted the friend request from " + sender.getName() + ".");
                Friend.addFriend(s, r);
            }
            case REJECTED -> {
                atomicResult = new AtomicReference<>(Result.REJECTED);
                TextUtil.sendMessage(sender, receiver.getName() + " has rejected your friend request.");
                TextUtil.sendMessage(receiver, "You have turned down the friend request from " + sender.getName() + ".");
            } case TIMED_OUT -> {
                atomicResult = new AtomicReference<>(Result.TIMED_OUT);
                TextUtil.sendMessage(sender, "The friend request to " + receiver.getName() + " has expired.");
                TextUtil.sendMessage(receiver, "The friend request from " + sender.getName() + " has expired.");
            }
        }
        requestSent = false;
    }

    /**
     * Represents the result of a friend request operation.
     */
    public enum Result {
        /**
         * The request was accepted.
         */
        ACCEPTED,
        /**
         * The request was rejected.
         */
        REJECTED,
        /**
         * The request timed out.
         */
        TIMED_OUT,
        /**
         * The request is invalid.
         */
        INVALID;
    }
}
