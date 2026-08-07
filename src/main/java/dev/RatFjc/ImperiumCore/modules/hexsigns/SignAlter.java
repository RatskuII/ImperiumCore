package dev.RatFjc.ImperiumCore.modules.hexsigns;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignAlter implements Listener {

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]");

    @EventHandler
    public void onSignAlter(SignChangeEvent event) {
        List<Component> lines = event.lines();
        int set = lines.size();
        for (int i = 0; i < set; i++) {
            String original = TextUtil.data(lines.get(i));
            String colored = buildLegacyColorString(original);
            Component result = TextUtil.nbt(colored);
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
