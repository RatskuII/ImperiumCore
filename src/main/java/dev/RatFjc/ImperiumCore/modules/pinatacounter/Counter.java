package dev.RatFjc.ImperiumCore.modules.pinatacounter;

import com.ordwen.odailyquests.api.events.QuestCompletedEvent;
import dev.RatFjc.ImperiumCore.init.PinataQuestCounter;
import dev.RatFjc.ImperiumCore.modules.pinatacounter.timer.QuestTimer;
import dev.RatFjc.ImperiumCore.utility.EventUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter implements Listener {

    @EventHandler
    public void onQuestComplete(QuestCompletedEvent event) {
        if (event.isCancelled()) return;
        if (!QuestTimer.checkCD()) return;

        LogUtil.log("Quest completion event was fired. If nothing else happens, something went wrong.");

        Player player = event.getPlayer();
        int current = CounterSaver.countIncrement();
        int cap = CounterSaver.getCap();

        if (current >= cap) {
            String complete = CounterSaver.getSpawnMsg();
            complete = format(complete, player, current, cap);

            TextUtil.announce(complete);
            String command = CounterSaver.getCompletionCmd();
            command = command.replace("%player%", player.getName());
            LogUtil.log("Running console cmd: " + command, new PinataQuestCounter(), Level.INFO, true);
            EventUtil.dispatchCommand(command);

            CounterSaver.resetCounter();
            QuestTimer.reset();
        } else {
            String progress = CounterSaver.getProgressMsg();
            progress = format(progress, player, current, cap);

            TextUtil.announce(progress);
            QuestTimer.reset();
        }

    }

    // Yes there's already formatting methods via the util but who cares
    @SuppressWarnings("deprecation")
    private String format(String msg, Player player, int current, int cap) {
        if (msg == null) return "";

        msg = msg
                .replace("%player%", player.getName())
                .replace("%current%", String.valueOf(current))
                .replace("%cap%", String.valueOf(cap));

        msg = org.bukkit.ChatColor.translateAlternateColorCodes('&', msg);

        Pattern pattern = Pattern.compile("#[A-Fa-f0-9]{6}");
        Matcher matcher = pattern.matcher(msg);

        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            try {
                String color = matcher.group();
                matcher.appendReplacement(buffer, ChatColor.of(color).toString());
            } catch (IllegalArgumentException ignored) {}
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}
