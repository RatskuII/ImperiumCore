package dev.RatFjc.ImperiumCore.modules.petconverter;

import dev.RatFjc.ImperiumCore.init.PetConverter;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadData {

    public static Optional<Integer> getLevel(String text) {
        text = TextUtil.strip(text);

        Matcher matcher = Pattern.compile("(?i)\\blvl\\s*(\\d+)").matcher(text);
        if (matcher.find()) {
            LogUtil.log("Found a match.", new PetConverter(), Level.INFO, true);
            int parsed = Integer.parseInt(matcher.group(1));
            return Optional.of(parsed);
        }

        return Optional.empty();
    }

    public static Optional<Integer> getLevelFromLore(@Nullable List<Component> lore) {
        if (lore == null) return Optional.empty();
        for (Component component : lore) {
            String string = TextUtil.data(component);
            string = TextUtil.strip(string);

            Matcher matcher = Pattern.compile("(?i)\\blvl\\s*(\\d+)").matcher(string);
            if (matcher.find()) {
                int parsed = Integer.parseInt(matcher.group(1));
                return Optional.of(parsed);
            }
        }
        return Optional.empty();
    }
}
