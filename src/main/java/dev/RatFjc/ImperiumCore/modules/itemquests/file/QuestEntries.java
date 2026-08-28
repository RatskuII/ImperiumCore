package dev.RatFjc.ImperiumCore.modules.itemquests.file;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class QuestEntries extends ConfigurationSaver {

    private static final File file = new File(plugin.getDataFolder(), "quests.yml");

    private static final FileConfiguration fileConfiguration = build(file);

    public void set() {
        if (fileConfiguration == null) {
            nullFail("Configuration could not be found.");
            return;
        }

        plugin.saveResource("quests.yml", false);

        fileConfiguration.set("version", plugin.getPluginMeta().getVersion());
        fileConfiguration.setComments("version", List.of(
                "DO NOT MODIFY THIS LINE.",
                "This helps to track the configuration version."
        ));

       ConfigurationSection initial = fileConfiguration.getConfigurationSection("quests");
       if (initial == null) initial = fileConfiguration.createSection("quests");
       int index = initial.getKeys(false).size();

        for (int i = 0; i < index; i++) {
            String path = "quests." + i;
            ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(path);
            if (configurationSection == null) continue;
            QuestLoader.loadFromFile(configurationSection, i);
        }
    }

    // Structure
    // quests -> ordinal -> name/type/description/target/amount


}
