package dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.Pair;
import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.utility.PDCUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

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

    private final AtomicReference<Result> atomicResult = new AtomicReference<>();
    private Pair<User, User> lock = Pair.empty();

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

    /**
     * Initializes the friend request process. Note that a player who has already sent a request will not be
     * able to send another one until the current request is completed or expires.
     */
    public void sendRequest() {
        if (!receiver.isOnline()) return; // offline handling isn't implemented yet
        if (isLocked()) {
            TextUtil.sendMessage(sender, "You have already sent a friend request. Wait a while before sending another one.");
            return;
        }
        // player accepting a request from themselves would be problematic
        if (sender.equals(receiver)) {
            TextUtil.sendMessage(sender, "You cannot send a friend request to yourself.");
            return;
        }
        TextUtil.sendMessage(sender, "A friend request was sent to " + receiver.getName());
        lock = lock(true);

        // cancel the request if no one responds to it within specified timeframe
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
        if (!atomicResult.compareAndSet(null, result)) return;
        if (s.asPlayer() == null || r.asPlayer() == null) return; // Make sure the players don't go offline before the request is done
        if (task != null) task.cancel();
        switch (result) {
            case ACCEPTED -> {
                atomicResult.set(Result.ACCEPTED);
                TextUtil.sendMessage(sender, receiver.getName() + " has accepted your friend request.");
                TextUtil.sendMessage(receiver, "You have accepted the friend request from " + sender.getName() + ".");
                Friend.addFriend(s, r);
            }
            case REJECTED -> {
                atomicResult.set(Result.REJECTED);
                TextUtil.sendMessage(sender, receiver.getName() + " has rejected your friend request.");
                TextUtil.sendMessage(receiver, "You have turned down the friend request from " + sender.getName() + ".");
            } case TIMED_OUT -> {
                atomicResult.set(Result.TIMED_OUT);
                TextUtil.sendMessage(sender, "The friend request to " + receiver.getName() + " has expired.");
                TextUtil.sendMessage(receiver, "The friend request from " + sender.getName() + " has expired.");
            }
        }
        lock = lock(false);
    }

    private Pair<User, User> lock(boolean state) {
        if (state) {
            PDCUtil.set(sender, Keys.FRIEND_LOCK, PersistentDataType.BOOLEAN, true);
            PDCUtil.set(receiver, Keys.FRIEND_LOCK, PersistentDataType.BOOLEAN, true);
            return new Pair<>(s, r);
        } else {
            PDCUtil.clear(sender, Keys.FRIEND_LOCK);
            PDCUtil.clear(receiver, Keys.FRIEND_LOCK);
            return Pair.empty();
        }
    }

    private boolean isLocked() {
        var value = PDCUtil.get(sender, Keys.FRIEND_LOCK, PersistentDataType.BOOLEAN);
        var value1 = PDCUtil.get(receiver, Keys.FRIEND_LOCK, PersistentDataType.BOOLEAN);
        return value != null || value1 != null;
    }

    public final Pair<User, User> lockState() {
        return this.lock;
    }

    public final Result status() {
        if (atomicResult.getAcquire() == null) return Result.INVALID;
        return atomicResult.get();
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
