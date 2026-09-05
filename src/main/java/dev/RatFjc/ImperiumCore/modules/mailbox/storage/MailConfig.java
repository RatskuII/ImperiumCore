package dev.RatFjc.ImperiumCore.modules.mailbox.storage;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class MailConfig extends ConfigurationSaver {

    private static final File messages = new File(plugin.getDataFolder(), "messages.yml");
    private static final File GUI = new File(plugin.getDataFolder(), "mailboxgui.yml");
    private static final File configFile = new File(plugin.getDataFolder(), "mailbox.yml");

    private static final FileConfiguration messageConfig = build(messages);
    private static final FileConfiguration guiConfig = build(GUI);
    private static final FileConfiguration mainConfig = build(configFile);

    @Override
    protected void set() {

    }

    // Main configuration **
    public static int getPageSize() {
        if (mainConfig == null) return 0;
        return mainConfig.getInt("mail.page-size", 45);
    }

    public static int getMaxPages() {
        if (mainConfig == null) return 0;
        return mainConfig.getInt("mail.max-pages", 50);
    }

    public static int getCooldown() {
        if (mainConfig == null) return 0;
        return mainConfig.getInt("cooldown.send-mail", 20);
    }

    // GUI configuration **
    public static String getTitle() {
        if (guiConfig == null) return "";
        return guiConfig.getString("gui.title", "&8Mailbox");
    }

    // Messages configuration **
    public static String getCooldownErrorMessage() {
        if (messageConfig == null) return "";
        return messageConfig.getString("mailbox.errors.cooldown",
                "&cPlease wait %s seconds before sending another mail.");
    }


}
