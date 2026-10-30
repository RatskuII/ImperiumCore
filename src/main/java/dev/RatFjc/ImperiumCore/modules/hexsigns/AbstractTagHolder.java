package dev.RatFjc.ImperiumCore.modules.hexsigns;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * A class that holds custom tag resolvers
 */
public abstract class AbstractTagHolder {

    protected final TagResolver[] resolvers = new TagResolver[]{
            Placeholder.styling("brown", TextColor.color(0x6A4439)),
            Placeholder.styling("light_blue", TextColor.color(0x7582AE)),
            Placeholder.styling("beige", TextColor.color(0xD6D39A))
    };
}
