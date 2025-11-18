package dev.Fjc.pinataQuestCounter.counter;

import com.ordwen.odailyquests.api.events.QuestCompletedEvent;
import dev.Fjc.pinataQuestCounter.PinataQuestCounter;
import dev.Fjc.pinataQuestCounter.file.FileBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class CounterClass implements Listener {

    private final PinataQuestCounter plugin;
    private final FileBuilder fileBuilder;

    public CounterClass(PinataQuestCounter plugin) {
        this.plugin = plugin;
        this.fileBuilder = plugin.getFileBuilder();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuestComplete(QuestCompletedEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        int current = fileBuilder.incrementCounter();
        int cap = plugin.getConfig().getInt("settings.quests_cap", 2);

        if (current >= cap) {
            String reachedMsg = plugin.getConfig().getString("messages.spawn");
            String formSpawnMsg = format(reachedMsg, player, current, cap);

            //Try component
            Bukkit.getServer().sendMessage(Component.text(formSpawnMsg));

            String command = plugin.getConfig().getString("settings.progress_complete_command");
            command = command.replace("%player%", player.getName());
            plugin.getLogger().info("[PinataQuestCounter] Running console command: " + command);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

            fileBuilder.resetCounter();
        } else {
            String progressMsg = plugin.getConfig().getString("messages.progress");
            String formProgressMsg = format(progressMsg, player, current, cap);

            //Try component
            Bukkit.getServer().sendMessage(Component.text(formProgressMsg));
        }
    }

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
