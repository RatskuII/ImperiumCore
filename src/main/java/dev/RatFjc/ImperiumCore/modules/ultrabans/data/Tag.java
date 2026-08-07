package dev.RatFjc.ImperiumCore.modules.ultrabans.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A {@link Tag} provides additional context on punishments that were issued to a target.
 */
public class Tag {

    private final String tag;

    private Tag(String tag) {
        this.tag = tag;
    }

    public static final Tag SILENT = new Tag("silent");

    public static final Tag VERBOSE = new Tag("verbose");

    public String tag() {
        return this.tag;
    }

    public static Tag get(String tag) {
        if (tag.equalsIgnoreCase(SILENT.tag())) return SILENT;
        if (tag.equalsIgnoreCase(VERBOSE.tag())) return VERBOSE;
        return new Tag(tag);
    }

    public static Collection<Tag> parse(String array) {
        var collection = new ArrayList<Tag>();
        if (array == null || array.isBlank()) return collection;

        for (String splitter : array.split(",")) {
            splitter = splitter.trim();
            splitter = splitter.strip();

            if (!splitter.isEmpty()) {
                Tag result = new Tag(splitter);
                collection.add(result);
            }
        }
        return collection;
    }
}
