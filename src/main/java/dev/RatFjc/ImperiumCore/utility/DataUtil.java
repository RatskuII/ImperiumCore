package dev.RatFjc.ImperiumCore.utility;

import dev.RatFjc.ImperiumCore.extras.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
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

    public static float parseFloat(String value) {
        float result;
        try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
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

    public static <R> @Nullable R randomElementFromList(List<R> input) {
        Random random = new Random();
        if (input.isEmpty()) return null;
        int index = random.nextInt(input.size());
        return input.get(index);
    }

    public static <P> boolean containsType(List<?> list, Class<P> type) {
        return list.stream()
                .anyMatch(type::isInstance);
    }

    /**
     * Converts an array ([]) to a {@link List}.
     * @param array The array to be converted
     * @return A valid {@link List}, not null
     * @param <R> The type of the elements within the array
     */
    public static <R> List<R> arrayToList(R[] array) {
        return Arrays.asList(array);
    }

    public static byte[] addToArray(byte[] array, byte element) {
        byte[] result = new byte[array.length + 1];

        System.arraycopy(array, 0, result, 0, array.length);
        result[result.length - 1] = element;
        return result;
    }

    /**
     * Ensures that the file provided is a YAML configuration file.
     * @param file The file to validate
     * @return Whether the file provided is YAML.
     */
    public static boolean validateYaml(File file) {
        String name = file.getName();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

    public static double truncate(double input) {
        return new BigDecimal(String.valueOf(input))
                .setScale(2, RoundingMode.DOWN)
                .doubleValue();
    }
}
