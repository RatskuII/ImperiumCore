package dev.RatFjc.ImperiumCore.modules.friendsapi;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.extras.Pair;
import dev.RatFjc.ImperiumCore.modules.friendsapi.data.ListType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

/**
 * Utility class that handles the adding, removal, and messaging of friends
 */
public class Friend {

    /**
     * Completes an accepted friend request (both users become friends of each other).
     * @param sender The user who sent the request
     * @param target The user who accepted the request
     * @return Whether the transaction was successful
     * @apiNote This method needs both players to be online, and will return false if either
     * the sender or receiver go offline before this transaction can complete.
     */
    public static boolean addFriend(User sender, User target) {
        if (isFriends(sender, target)) return false;
        PersistentDataContainer container0 = sender.container();
        if (container0 == null) return false;

        // Symbiotic relationship, both need to add each other as friends
        var current = container0.get(Keys.FRIENDS, PersistentDataType.LIST.strings());
        if (current == null) current = new ArrayList<>();
        String in = target.uuid().toString();
        current.add(in);
        container0.set(Keys.FRIENDS, PersistentDataType.LIST.strings(), current);

        PersistentDataContainer container1 = target.container();
        if (container1 == null) return false;

        var current1 = container1.get(Keys.FRIENDS, PersistentDataType.LIST.strings());
        if (current1 == null) current1 = new ArrayList<>();
        String in1 = sender.uuid().toString();
        current1.add(in1);
        container1.set(Keys.FRIENDS, PersistentDataType.LIST.strings(), current1);

        return isFriends(sender, target);
    }

    public static boolean isFriends(User one, User two) {
        PersistentDataContainer container = one.container();
        if (container == null) throw new IllegalStateException("The user provided is offline or does not exist.");

        var values = container.get(Keys.FRIENDS, PersistentDataType.LIST.strings());
        String in = two.uuid().toString();
        return values != null && values.contains(in);
    }

    /**
     * Removes an associated pair of friends.
     * @param sender The user initiating the removal
     * @param target The user being removed
     * @return A {@link Pair} representing the result of the operation, where both slots being true indicate total success. If both slots
     * are false, the operation failed entirely. If one slot is true and one false, then the operation was partially successful.
     * @apiNote If the second slot is false, then the user being removed is likely offline, and the relationship has been marked stale. Their data
     * will update when they log in again.
     */
    public static Pair<Boolean, Boolean> removeFriend(User sender, User target) {
        if (!isFriends(sender, target)) return new Pair<>(false, false);
        boolean one, two;
        PersistentDataContainer container0 = sender.container();
        if (container0 == null) return new Pair<>(false, false);

        var values = container0.get(Keys.FRIENDS, ListType.STRING);
        if (values == null) values = new ArrayList<>(); // i cant leave it uninitialized
        String removed = target.uuid().toString();
        one = values.remove(removed);
        container0.set(Keys.FRIENDS, ListType.STRING, values);

        PersistentDataContainer container1 = target.container();
        if (container1 == null) return new Pair<>(one, false);
        var values1 = container1.get(Keys.FRIENDS, ListType.STRING);
        if (values1 == null) values1 = new ArrayList<>();
        String removed1 = sender.uuid().toString();
        two = values1.remove(removed1);
        container1.set(Keys.FRIENDS, ListType.STRING, values1);

        return new Pair<>(one, two);
    }

}
