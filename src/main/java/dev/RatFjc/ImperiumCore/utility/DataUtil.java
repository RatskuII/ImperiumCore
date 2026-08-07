package dev.RatFjc.ImperiumCore.utility;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataUtil {

    private static final Pattern pattern = Pattern.compile("(\\d+)([smhd])");

    /**
     * Parses a valid Duration from the given text.
     * @param context The text that will be converted into a Duration
     * @return A Duration
     */
    public static Duration parseDuration(String context) {
        if (context == null) throw new NullPointerException("The input provided cannot be null.");

        context = context.toLowerCase();
        Matcher matcher = pattern.matcher(context);

        Duration duration = Duration.ZERO;
        while (matcher.find()) {
            long amount = Long.parseLong(matcher.group(1));
            String type = matcher.group(2);

            switch (type) {
                case "s" -> duration = duration.plus(Duration.ofSeconds(amount));
                case "m" -> duration = duration.plus(Duration.ofMinutes(amount));
                case "h" -> duration = duration.plus(Duration.ofHours(amount));
                case "d" -> duration = duration.plus(Duration.ofDays(amount));
            }
        }
        return duration;
    }

    /**
     * Parses a double from a string
     * @param value The string
     * @return A valid double. Will return 0 if the parsing fails.
     */
    public static double parseDouble(String value) {
        double result;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
            result = 0;
        }
        return result;
    }

    /**
     * Converts the string provided into a valid UUID.
     * @param input The string to convert
     * @return A valid UUID, or null if the conversion failed
     */
    public static @Nullable UUID stringToUUID(String input) {
        if (input == null) return null;
        UUID result = null;
        try {
            result = UUID.fromString(input);
        } catch (IllegalArgumentException ignored) {

        }
        return result;
    }
}
