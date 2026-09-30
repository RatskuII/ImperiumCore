package dev.RatFjc.ImperiumCore.extras.cmds;

import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.command.CommandSender;

/**
 * A simple interface that holds common operations for commands.
 */
public interface CommandInterface {

    /**
     * Indicates that this portion of the command is for players only.
     * @param sender The sender to notify
     * @return False, to indicate the command was not successful
     */
    default boolean playerOnly(CommandSender sender) {
        TextUtil.sendMessage(sender, "Only players can run this command.");
        return false;
    }

    /**
     * Indicates that the sender specified does not have permissions to continue.
     * @param sender The sender to notify
     * @return False, to indicate the command was not successful
     */
    default boolean noPermission(CommandSender sender) {
        TextUtil.sendMessage(sender, "You do not have permission to run this command.");
        return false;
    }

    /**
     * Indicates that invalid arguments were provided.
     * @param sender The sender to notify
     * @param arguments An optional set of parameters to show the sender.
     * @return False, to indicate the command was not successful
     */
    default boolean badArguments(CommandSender sender, String... arguments) {
        TextUtil.sendMessage(sender, "Invalid arguments");
        TextUtil.sendMessage(sender, arguments);
        return false;
    }
}
