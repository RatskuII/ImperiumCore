package dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager;

import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FriendRequestCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "Only players can run this command.");
            return false;
        }
        if (args.length != 2) return false;

        String operator = args[0];

        User user = new User(player);
        User requested = User.fromName(args[1]);

        FriendRequest request = new FriendRequest(user.asPlayer(), requested.asPlayer());
        switch (operator) {
            case "add" -> request.sendRequest();
            case "remove" -> Friend.removeFriend(user, requested);
            default -> {
                TextUtil.sendMessage(player, "Invalid operator argument.");
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("add", "remove");
        if (args.length == 2) return PlayerUtil.getOnlinePlayerList();
        return List.of();
    }
}
