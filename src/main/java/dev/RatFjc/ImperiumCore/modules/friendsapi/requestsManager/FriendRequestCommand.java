package dev.RatFjc.ImperiumCore.modules.friendsapi.requestsManager;

import dev.RatFjc.ImperiumCore.init.FriendsAPI;
import dev.RatFjc.ImperiumCore.modules.friendsapi.Friend;
import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.modules.friendsapi.style.FriendTextList;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.PlayerUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public class FriendRequestCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "Only players can run this command.");
            return false;
        }
        if (args.length == 2) {

            String operator = args[0];

            User user = new User(player);
            User requested = User.fromName(args[1]);

            if (user.asPlayer() == null || requested.asPlayer() == null) {
                TextUtil.sendMessage(sender, "The player provided is offline.");
                return false;
            }

            if (user.equals(requested)) {
                TextUtil.sendMessage(player, "You can't send a friend request to yourself.");
                return false;
            }

            FriendRequest request = new FriendRequest(user.asPlayer(), requested.asPlayer());
            switch (operator) {
                case "add" -> request.sendRequest();
                case "remove" -> Friend.removeFriend(user, requested);
                default -> {
                    TextUtil.sendMessage(player, "Invalid operator argument.");
                    return false;
                }
            }
            return true;
        }
        if (args.length == 1) {
            String input = args[0];
            if (!input.equalsIgnoreCase("list")) {
                TextUtil.sendMessage(player, "Invalid argument.");
                return false;
            }
            User user = new User(player);
            FriendTextList builder = new FriendTextList(user);
            Component result;
            try {
                result = builder
                        .head(Component.text("Friends")
                                .appendNewline()
                                .append(
                                        Component.text("==============")
                                                .color(NamedTextColor.GOLD)
                                                .style(Style.style(TextDecoration.BOLD))
                                ))
                        .body(NamedTextColor.YELLOW)
                        .border('=', 14, 2, NamedTextColor.GOLD)
                        .build(true);
            } catch (IllegalAccessException e) {
                TextUtil.sendMessage(player, "Something went wrong while trying to fetch the friend list.", "Check the logs for more information.");
                LogUtil.log("Something went wrong while trying to fetch the friend list.", new FriendsAPI(), Level.SEVERE, false);
                LogUtil.log(e.getMessage());
                return false;
            }
            return result != null;

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("add", "remove", "list");
        if (args.length == 2) return PlayerUtil.getOnlinePlayerList();
        return List.of();
    }
}
