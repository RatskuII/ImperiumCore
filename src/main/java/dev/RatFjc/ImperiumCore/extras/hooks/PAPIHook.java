package dev.RatFjc.ImperiumCore.extras.hooks;

import dev.RatFjc.ImperiumCore.utility.DataUtil;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PAPIHook {

    private static final PlaceholderAPIPlugin apiPlugin = PlaceholderAPIPlugin.getInstance();

    public static @Nullable PlaceholderExpansion expansion(String identifier) {
        return apiPlugin.getLocalExpansionManager().getExpansion(identifier);
    }

    public static class LocalDiffHook {

        private static final PlaceholderExpansion localDiffExpansion = expansion("diff");

        public static double localDiff(Player player) {
          if (localDiffExpansion == null) return 0;
          return DataUtil.parseDouble(localDiffExpansion.onRequest(player, "local"));
        }

        public static double globalDiff() {
            if (localDiffExpansion == null) return 0;
            return DataUtil.parseDouble(localDiffExpansion.onRequest(null, "global"));
        }
    }
}
