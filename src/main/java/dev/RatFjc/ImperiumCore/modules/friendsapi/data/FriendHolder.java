package dev.RatFjc.ImperiumCore.modules.friendsapi.data;

import dev.RatFjc.ImperiumCore.modules.friendsapi.User;

import java.util.HashMap;
import java.util.Map;

public class FriendHolder {

    public FriendHolder() {}

    public FriendHolder(Map<User, User> relationships) {
        this.staleFriendships = relationships;
    }

    private Map<User, User> staleFriendships = new HashMap<>();

    public FriendHolder addStale(User remover, User removed) {
        staleFriendships.put(remover, removed);
        return this;
    }

    public FriendHolder removeStale(User remover, User removed) {
        staleFriendships.remove(remover, removed);
        return this;
    }

    public FriendHolder clearStale(User user) {
        staleFriendships.remove(user);
        return this;
    }

    public FriendHolder clearStale() {
        staleFriendships.clear();
        return this;
    }

}
