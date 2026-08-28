package dev.RatFjc.ImperiumCore.extras.cmds;

import dev.RatFjc.ImperiumCore.ConfigurationSaver;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Command portal to reload all configurations.
 */
public class CoreReload implements TabExecutor, PluginProvider {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.isOp()) {
            TextUtil.sendMessage(sender, "You do not have permission to run this command.");
            return false;
        }
        if (args.length != 1) {
            TextUtil.sendMessage(sender, "Invalid arguments.");
            return false;
        }
        String targetFile = args[0];
        File file = new File(plugin.getDataFolder(), targetFile);
        if (!file.exists()) {
            TextUtil.sendMessage(sender, "The file provided is invalid or does not exist.");
            return false;
        }
        ConfigurationSaver.syncSave(file);
        TextUtil.sendMessage(sender, "The file provided was successfully saved.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            File[] targets = plugin.getDataFolder().listFiles();
            return DataUtil.arrayToList(targets).stream()
                    .filter(Objects::nonNull)
                    .map(File::getName)
                    .toList();
        }
        return List.of();
    }
}
