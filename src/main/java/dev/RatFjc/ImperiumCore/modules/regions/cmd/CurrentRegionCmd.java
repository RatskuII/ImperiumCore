package dev.RatFjc.ImperiumCore.modules.regions.cmd;

import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import dev.RatFjc.ImperiumCore.modules.regions.config.RegionBuilder;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurrentRegionCmd implements TabExecutor, CommandInterface, PluginProvider {

    private final String permission = "imperium.region.info";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission(permission)) return noPermission(sender);
        if (!(sender instanceof Player player)) return playerOnly(sender);
        if (args.length > 0) return badArguments(player, "No arguments expected.");

        Location location = player.getLocation();
        Region region = RegionBuilder.fromPosition(location);
        if (region != null) {
            RegionProperty property = region.properties();
            TextUtil.sendMessage(player, "Region name: " + property.name(), "Region UUID: " + region.uuid(), "Local diff offset: " + property.localDiffOffset(), "mobScale offset: " + property.mobScale());
            return true;
        } else {
            TextUtil.sendMessage(player, "You are currently not within a defined region.");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
