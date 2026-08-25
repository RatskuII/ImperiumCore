package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.Utility;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;

import java.util.List;

public class TextUtil extends Utility {

    public static Component nbt(String data) {
        return PlainTextComponentSerializer.plainText().deserialize(data);
    }

    public static String data(Component nbt) {
        return PlainTextComponentSerializer.plainText().serialize(nbt);
    }

    public static String legacyColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String strip(String text) {
        Component component = nbt(text);

        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static Component color(String data, TextColor color) {
        return Component.text()
                .content(data)
                .color(color)
                .build();
    }

    public static void sendMessage(Audience audience, String message) {
        audience.sendMessage(nbt(message));
    }

    public static void sendMessages(Audience audience, String... messages) {
        for (String message : messages) sendMessage(audience, message);
    }

    public static void sendMessages(Audience audience, List<String> messages) {
        for (String message : messages) sendMessage(audience, message);
    }

    public static void announce(String message) {
        plugin.getServer().broadcast(nbt(message));
    }
}
