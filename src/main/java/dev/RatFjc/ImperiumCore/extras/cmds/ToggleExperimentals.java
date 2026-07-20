package dev.RatFjc.ImperiumCore.extras.cmds;

import dev.RatFjc.ImperiumCore.extras.ExperimentController;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToggleExperimentals implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "Only players can run this command.");
            return false;
        }

        if (args.length > 1) {
            TextUtil.sendMessage(player, "Invalid arguments.");
            return false;
        }

        if (args.length == 1) {
            String toggle = args[0];
            switch (toggle) {
                case "allow" -> {
                    ExperimentController.allowExperiments(player, true);
                    return true;
                }
                case "deny" -> {
                    ExperimentController.allowExperiments(player, false);
                    return true;
                }
            }
        }
        if (args.length == 0) {
            var allowed = ExperimentController.areExperimentsAllowed(player);
            ExperimentController.allowExperiments(player, !allowed);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("allow", "deny");
        else return List.of();
    }
}
