package dev.RatFjc.ImperiumCore.modules.mailbox;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MailMain {

    private final MailDatabase db;
    private final MailboxGUI gui;
    private final MailCommands commands;
    private final MailAPI api;
    private final FileConfiguration cfg;
    private final FileConfiguration messages;
    private final FileConfiguration guiCfg;

    private final ImperiumCore plugin;

    public MailMain(ImperiumCore plugin) throws Exception {
        this.plugin = plugin;


        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) plugin.saveResource("messages.yml", false);

        File guiFile = new File(plugin.getDataFolder(), "mailboxgui.yml");
        if (!guiFile.exists()) plugin.saveResource("mailboxgui.yml", false);

        File configFile = new File(plugin.getDataFolder(), "mailbox.yml");
        if (!configFile.exists()) plugin.saveResource("mailbox.yml", false);

        this.cfg = YamlConfiguration.loadConfiguration(configFile);
        this.messages = YamlConfiguration.loadConfiguration(messagesFile);
        this.guiCfg = YamlConfiguration.loadConfiguration(guiFile);

        int pageSize = cfg.getInt("Mailbox.mail.page-size", 45);
        int maxPages = cfg.getInt("Mailbox.mail.max-pages", 50);

        this.db = new MailDatabase(new File(plugin.getDataFolder(), "mail_storage.db"), pageSize, maxPages);
        this.gui = new MailboxGUI(db, plugin, cfg, messages, guiCfg);
        this.commands = new MailCommands(plugin, db, cfg, messages, guiCfg, gui);
        this.api = new MailAPI(db, commands);

        Bukkit.getPluginManager().registerEvents(gui, plugin);


    }

    public FileConfiguration getMessages() { return messages; }
    public FileConfiguration getCfg() { return cfg; }
    public MailDatabase getDB() { return db; }
    public MailCommands getCommands() { return commands; }
    public MailboxGUI getGUI() { return gui; }

    public void startReminder() {
        MailReminder reminder = new MailReminder(db, cfg, messages);

    }
}