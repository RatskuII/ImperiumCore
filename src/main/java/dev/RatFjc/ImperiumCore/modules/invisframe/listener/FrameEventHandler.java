package dev.RatFjc.ImperiumCore.modules.invisframe.listener;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.init.InvisFrame;
import dev.RatFjc.ImperiumCore.modules.invisframe.Frame;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Location;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

public class FrameEventHandler implements Listener {

    @EventHandler
    public void onFrameBreak(HangingBreakEvent event) {
        Hanging hanging = event.getEntity();

        if (Frame.isFrame(hanging)) {
            LogUtil.log("Frame match found. Trying to drop item...", new InvisFrame(), Level.INFO, true);
            event.setCancelled(true);
            hanging.remove();

            Location location = hanging.getLocation();

            Frame frame = new Frame();
            if (Frame.isGlowing(hanging)) frame.glow();
            ItemStack result = frame.name("Invisible Item Frame").enchant().flags(ItemFlag.HIDE_ENCHANTS).build();

            hanging.getWorld().dropItemNaturally(
                    location, result
            );
        }
    }

    @EventHandler
    public void onFramePlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        Hanging hanging = event.getEntity();


        if (player == null) return;
        if (!Frame.isFrame(hanging)) return;
        LogUtil.log("Frame match found. Trying to place item...", new InvisFrame(), Level.INFO, true);

        if (!(hanging instanceof ItemFrame itemFrame)) return;
        itemFrame.setVisible(false);

        PersistentDataContainer container = itemFrame.getPersistentDataContainer();
        container.set(Keys.INVIS_FRAME, PersistentDataType.BOOLEAN, true);
    }
}
