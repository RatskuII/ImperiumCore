package dev.RatFjc.ImperiumCore.extras.cmds;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.Reloadable;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Command portal to reload all configurations.
 */
public class CoreReload implements TabExecutor, PluginProvider, CommandInterface {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) return noPermission(sender);
        if (args.length != 1) return badArguments(sender);

        String targetFile = args[0];
        File file = new File(plugin.getDataFolder(), targetFile);
        if (!file.exists()) {
            TextUtil.sendMessage(sender, "The file provided is invalid or does not exist.");
            return false;
        }
        if (!DataUtil.validateYaml(file)) {
            TextUtil.sendMessage(sender, "The file provided is not a valid configuration file.");
            return false;
        }
        for (ConfigurationSaver saver : ImperiumCore.savers()) {
            File iteratedFile = saver.file();

            // make sure the file exists before trying to process it
            if (iteratedFile == null || !iteratedFile.exists()) {
                LogUtil.debug("File is null or doesn't exist, moving on...", null);
                continue;
            }
            if (!(saver instanceof Reloadable reloadable)) {
                LogUtil.debug("File " + iteratedFile.getName() + " doesn't extend reloadable, moving on...", null);
                continue;
            }
            if (iteratedFile.getName().equalsIgnoreCase(file.getName())) {
                reloadable.reload();
                TextUtil.sendMessage(sender, "The file provided was successfully reloaded.");
                return true;
            } else {
                LogUtil.debug("File names " + iteratedFile.getName() + ", " + file.getName() + " do not match. Exiting...", null);
            }
        }
        TextUtil.sendMessage(sender, "The file provided is backed by a configuration that does not support reloading.");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            File[] targets = plugin.getDataFolder().listFiles();
            return DataUtil.arrayToList(targets).stream()
                    .filter(Objects::nonNull)
                    .map(File::getName)
                    .filter(obj -> obj.endsWith(".yml"))
                    .toList();
        }
        return List.of();
    }
}
