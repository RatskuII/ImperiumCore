package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.extras.Pair;
import org.jetbrains.annotations.NotNull;
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

    public static int parseInt(String value) {
        int result;
        try {
            result = Integer.parseInt(value);
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

    /**
     * Intended to get a string-integer {@link Pair} with the following expression:
     * <br>
     * <br>
     * {@link String}:{@link Integer}
     * @param input The string to parse
     * @return The resulting {@link Pair}, not null
     * @throws NumberFormatException if the input does not match the required expression, or if the object
     * found is not a valid integer
     * @throws IllegalArgumentException if the input provided is invalid in some way
     * @throws NullPointerException if the input provided is null
     */
    public static @NotNull Pair<String, Integer> parseData(String input) {
        if (input == null) throw new NullPointerException("The input provided is invalid.");
        if (!input.contains(":")) throw new IllegalArgumentException("The input provided is invalid.");
        String[] splits = input.split(":", 2);
        if (splits[1].length() > 4) throw new IllegalArgumentException("The value extracted is too large.");
        if (splits[0].isEmpty()) throw new IllegalArgumentException("The input provided is empty.");
        return new Pair<>(splits[0], Integer.parseInt(splits[1].trim()));
    }
}
