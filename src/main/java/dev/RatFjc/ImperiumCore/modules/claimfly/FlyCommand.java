package dev.RatFjc.ImperiumCore.modules.claimfly;

import dev.RatFjc.ImperiumCore.extras.cmds.CommandInterface;
import dev.RatFjc.ImperiumCore.extras.hooks.GPHook;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand extends FlyManagement implements CommandExecutor, CommandInterface {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return playerOnly(sender);

        FlyState flyState = flyStatus(player);


        if (!checkGlobal(player)) {
            if (!checkClaimOnly(player)) return noPermission(sender);
            else if (GPHook.isInClaim(player) == null) {
                TextUtil.sendMessage(player, "You only have permission to use fly in claims, and you are not in a claim.");
                return false;
            } else {
                if (flyState != FlyState.NOT_FLYING) setFly(player, FlyState.NOT_FLYING);
                else setFly(player, FlyState.FLYING_CLAIM);
            }
        } else {
            if (flyState != FlyState.NOT_FLYING) setFly(player, FlyState.NOT_FLYING);
            else setFly(player, FlyState.FLYING);
        }

        return true;
    }
}
