package dev.RatFjc.ImperiumCore.modules.friendsapi.data;

import dev.RatFjc.ImperiumCore.modules.friendsapi.User;

import java.util.HashMap;
import java.util.Map;

public interface FriendHolder {

    Map<User, User> staleFriendships = new HashMap<>();

    default void add(User remover, User removed) {
        staleFriendships.put(remover, removed);
    }

    default void remove(User remover, User removed) {
        staleFriendships.remove(remover, removed);
    }

    default void clear(User user) {
        staleFriendships.remove(user);
    }

    default void clear() {
        staleFriendships.clear();
    }
}
