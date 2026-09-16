package dev.RatFjc.ImperiumCore.modules.hexsigns;

import dev.RatFjc.ImperiumCore.extras.Pair;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.*;
import net.kyori.adventure.util.RGBLike;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignAlter implements Listener {

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]");
    private final Pattern compPattern = Pattern.compile("(?i)&([0-9a-ik-or])|(#[0-9a-f]{6})");

    @EventHandler
    public void onSignAlter(SignChangeEvent event) {
        List<Component> lines = event.lines();
        int set = lines.size();
        for (int i = 0; i < set; i++) {
            Component original = event.line(i);
            if (original == null) continue;

            Component result = build(original);
            event.line(i, result);
        }
    }

    @EventHandler
    public void onMessage(AsyncChatEvent event) {
        String colored = buildLegacyColorString(TextUtil.data(event.message()));
        Component result = TextUtil.nbt(colored);
        event.message(result);
    }

    private String buildLegacyColorString(String text) {
        text = register(text, "#6A4439", "&g"); // Brown
        text = register(text, "#7582AE", "&h"); // Light-ish blue
        text = register(text, "D6D39A", "&i"); // Beige

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String parsed = text.substring(matcher.start(), matcher.end());
            text = text.replace(parsed, ChatColor.of(parsed) + "");
            matcher = pattern.matcher(text);
        }

        return TextUtil.legacyColor(text);
    }

    private Component build(String input) {
        Matcher matcher = compPattern.matcher(input);
        Pair<TextColor, TextDecoration> style = Pair.empty();

        TextComponent.Builder out = Component.text();
        int end = 0;

        while (matcher.find()) {
            if (matcher.start() > end) {
                String string = input.substring(end, matcher.start());
                Component partial = Component.text(string);
                if (style.key() != null) partial = partial.color(style.key());
                if (style.value() != null) partial = partial.decorate(style.value());
                out.append(partial);
            }

            if (matcher.group(1) != null) {
                char obj = matcher.group(1).charAt(0);
                style = new Pair<>(fromLegacy(obj), legacyFormat(obj));
            }
            else if (matcher.group(2) != null) style = new Pair<>(TextColor.fromHexString(matcher.group(2)), style.value());

            end = matcher.end();
        }
        if (end < input.length()) {
            String string = input.substring(end);
            Component partial = Component.text(string);
            if (style.key() != null) partial = partial.color(style.key());
            if (style.value() != null) partial = partial.decorate(style.value());
            out.append(partial);
        }

        return out.build();
    }

    private Component build(Component input) {
        Pair<TextColor, Set<TextDecoration>> style = new Pair<>(input.color(), input.decorations().keySet());
        String parser = TextUtil.data(input);
        Matcher matcher = compPattern.matcher(parser);

        TextComponent.Builder out = Component.text();
        int end = 0;

        while (matcher.find()) {
            if (matcher.start() > end) {
                String string = parser.substring(end, matcher.start());
                Component partial = Component.text(string);
                if (style.key() != null) partial = partial.color(style.key());
                if (!style.value().isEmpty()) partial = partial.decorate(style.value().toArray(new TextDecoration[0]));
                out.append(partial);
            }

            if (matcher.group(1) != null) {
                char obj = matcher.group(1).charAt(0);
                TextColor textColor = fromLegacy(obj);
                TextDecoration decoration = legacyFormat(obj);

                if (textColor != null) style = new Pair<>(textColor, style.value());
                if (decoration != null) {
                    Set<TextDecoration> decorations = new HashSet<>(style.value());
                    decorations.add(decoration);
                    style = new Pair<>(style.key(), decorations);
                }
                if (obj == 'r') style = new Pair<>(null, Set.of()); // reset
            } else if (matcher.group(2) != null) {
                style = new Pair<>(TextColor.fromHexString(matcher.group(2)), style.value());
            }

            end = matcher.end();
        }

        if (end < parser.length()) {
            String string = parser.substring(end);
            Component partial = Component.text(string);
            if (style.key() != null) partial = partial.color(style.key());
            if (!style.value().isEmpty()) partial = partial.decorate(style.value().toArray(new TextDecoration[0]));
            out.append(partial);
        }
        return out.build();
    }

    private TextColor fromLegacy(char key) {
        return switch (key) {
            // Default legacy colors
            case '0' -> NamedTextColor.BLACK;
            case '1' -> NamedTextColor.DARK_BLUE;
            case '2' -> NamedTextColor.DARK_GREEN;
            case '3' -> NamedTextColor.DARK_AQUA;
            case '4' -> NamedTextColor.DARK_RED;
            case '5' -> NamedTextColor.DARK_PURPLE;
            case '6' -> NamedTextColor.GOLD;
            case '7' -> NamedTextColor.GRAY;
            case '8' -> NamedTextColor.DARK_GRAY;
            case '9' -> NamedTextColor.BLUE;
            case 'a' -> NamedTextColor.GREEN;
            case 'b' -> NamedTextColor.AQUA;
            case 'c' -> NamedTextColor.RED;
            case 'd' -> NamedTextColor.LIGHT_PURPLE;
            case 'e' -> NamedTextColor.YELLOW;
            case 'f' -> NamedTextColor.WHITE;

            // Additional colors
            case 'g' -> TextColor.color(0x6A4439);
            case 'h' -> TextColor.color(0x7582AE);
            case 'i' -> TextColor.color(0xD6D39A);
            default -> null;
        };
    }

    private TextDecoration legacyFormat(char key) {
        return switch (key) {
            case 'k' -> TextDecoration.OBFUSCATED;
            case 'l' -> TextDecoration.BOLD;
            case 'm' -> TextDecoration.STRIKETHROUGH;
            case 'n' -> TextDecoration.UNDERLINED;
            case 'o' -> TextDecoration.ITALIC;
            default -> null;
        };
    }

    /**
     * Registers an additional color to the index.
     * @param text The text being modified
     * @param color The color, which should be a valid rgb color.
     * @param data The set of characters that will transcribe this color to the text.
     * @return The new text
     */
    private String register(String text, String color, CharSequence data) {
        if (color.startsWith("#") && color.length() == 7) if (text.contains(data)) {
            text = text.replace(data, color);
            return text;
        }
        return text;
    }
}
