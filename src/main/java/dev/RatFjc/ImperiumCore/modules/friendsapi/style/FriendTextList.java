package dev.RatFjc.ImperiumCore.modules.friendsapi.style;

import dev.RatFjc.ImperiumCore.modules.friendsapi.User;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a builder that can construct a friend list from a variety of valid {@link Component}. The list consists
 * of a {@link #head(Component)} (like an introduction of the list), the {@link #body(List, TextColor)} (the list itself),
 * and a {@link #border(char, int, int, TextColor)} at the bottom for decorative purposes. Any part of this builder can
 * be excluded and the resulting {@link Component} would still be valid.
 * @see #body(TextColor)
 */
public class FriendTextList {

    private User requester;

    private Component result = Component.empty();

    public FriendTextList() {}

    public FriendTextList(User user) {
        this.requester = user;
    }

    /**
     * Represents the user who may supply the list of friends.
     * @param user A user
     * @return This builder, for chaining
     */
    public FriendTextList user(User user) {
        this.requester = user;
        return this;
    }

    /**
     * Represents the head of the list, which acts as an introduction and can be used for decorative purposes.
     * @param head A {@link Component} that describes the head of the list
     * @return This builder, for chaining
     */
    public FriendTextList head(Component head) {
        this.result = result.append(head).appendNewline();
        return this;
    }

    /**
     * Represents the body of the list, which contains the actual list of friends.
     * @param bodyColor The color of this part of the list
     * @return This builder, for chaining
     * @throws IllegalAccessException if the {@link User} supplier was not initialized. If you can't initialize a valid {@link User},
     * supply the friend list yourself via {@link #body(List, TextColor)}
     */
    public FriendTextList body(TextColor bodyColor) throws IllegalAccessException {
        if (requester == null) throw new IllegalAccessException("This method shouldn't be called without a specified user.");
        return body(requester.getFriends().stream()
                .map(Bukkit::getOfflinePlayer)
                .map(OfflinePlayer::getName)
                .toList(), bodyColor);
    }

    /**
     * Represents the body of the list, which contains the actual list of friends.
     * @param entries A supplied list of friends, which will be mutated if empty
     * @param bodyColor The color of this part of the list
     * @return This builder, for chaining
     */
    public FriendTextList body(List<String> entries, TextColor bodyColor) {
        Component out = Component.empty();
        if (entries.isEmpty()) {
            bodyColor = NamedTextColor.RED;
            entries.add("Your friends list is currently empty.");
        }
        for (String entry : entries) {
            out = out.append(
                    TextUtil.nbt(entry)
                            .color(bodyColor)
                            .appendNewline()
            );
        }
        this.result = result.append(out);
        return this;
    }

    /**
     * Represents the bottom border of the list, which mostly serves for decorative purposes.
     * @param key The character to use as the border
     * @param length The length of the border
     * @param thickness The thickness of the border
     * @param borderColor The color of the border
     * @return This builder, for chaining
     */
    public FriendTextList border(char key, int length, int thickness, @Nullable TextColor borderColor) {
        if (length > 64) length = 64;
        Component out = TextUtil.nbt(String.valueOf(key).repeat(Math.max(0, length)));
        if (thickness < 1) thickness = 1;
        if (thickness > 8) thickness = 8;
        if (borderColor != null) out = out.color(borderColor);
        for (int i = 0; i < thickness; i++) {
            this.result = result.append(out).appendNewline();
        }
        return this;
    }

    /**
     * Represents the final result of the previous chained builder methods.
     * @param sendImmediately Whether this {@link Component} should be immediately messaged to the user
     * @return The resulting {@link Component}
     */
    public final Component build(boolean sendImmediately) {
        if (result == null) result = Component.empty();
        if (sendImmediately && requester != null) {
            Player player = requester.asPlayer();
            if (player != null) TextUtil.sendMessage(player, result);
        }
        return result;
    }
}
