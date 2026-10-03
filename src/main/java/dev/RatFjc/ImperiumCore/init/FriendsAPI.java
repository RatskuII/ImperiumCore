package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.data.FriendHolder;
import dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager.FriendRequestCommand;
import dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager.Stale;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class FriendsAPI extends Module {

    private static FriendHolder friendHolder;

    @Override
    public String name() {
        return "FriendsAPI";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        BukkitUtil.registerCommand(new FriendRequestCommand(), "friend");
        BukkitUtil.registerEvent(new Stale());
        friendHolder = new FriendHolder();
    }

    public static FriendHolder friendHolder() {
        return friendHolder;
    }

}
