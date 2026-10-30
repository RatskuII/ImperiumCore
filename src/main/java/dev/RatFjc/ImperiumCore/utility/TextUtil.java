package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.bukkit.ChatColor;

import java.util.List;

public class TextUtil extends Utility {

    // FORMATTERS

    public static Component plainComponent(String data) {
        return PlainTextComponentSerializer.plainText().deserialize(data);
    }

    public static Component plainComponent(Component component) {
        return plainComponent(fromComponent(component));
    }

    public static Component toMiniMessage(String string, TagResolver... resolvers) {
        return MiniMessage.miniMessage().deserialize(string, resolvers);
    }

    public static Component toMiniMessage(Component component, TagResolver... resolvers) {
        return toMiniMessage(fromComponent(component), resolvers);
    }

    public static Component legacy(Component component) {
        return legacy(fromComponent(component));
    }

    public static Component legacy(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }

    public static Component color(String data, TextColor color) {
        return Component.text()
                .content(data)
                .color(color)
                .build();
    }

    public static String fromComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String fromMiniMessage(final Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Deprecated
    public static String legacyColor(String text) {
        try {
            return ChatColor.translateAlternateColorCodes('&', text);
        } catch (IllegalArgumentException ignored) {
            return text;
        }
    }

    public static String strip(String string) {
        return LegacyComponentSerializer.legacySection().serialize(plainComponent(string));
    }

    // MESSENGERS

    public static Component sendMessage(Audience audience, Component component) {
        audience.sendMessage(component);
        return component;
    }

    public static void sendMessage(Audience audience, String message) {
        audience.sendMessage(plainComponent(message));
    }

    public static void sendMessage(Audience audience, String... messages) {
        for (String message : messages) sendMessage(audience, message);
    }

    public static void sendMessage(Audience audience, List<String> messages) {
        for (String message : messages) sendMessage(audience, message);
    }

    public static void announce(String message) {
        plugin.getServer().broadcast(plainComponent(message));
    }
}
