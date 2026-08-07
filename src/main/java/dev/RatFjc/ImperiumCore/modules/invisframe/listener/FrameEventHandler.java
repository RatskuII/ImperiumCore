package dev.RatFjc.ImperiumCore.modules.invisframe.listener;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.modules.invisframe.Frame;
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

public class FrameEventHandler implements Listener {

    @EventHandler
    public void onFrameBreak(HangingBreakEvent event) {
        Hanging hanging = event.getEntity();

        if (Frame.isFrame(hanging)) {
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

        if (!(hanging instanceof ItemFrame itemFrame)) return;
        itemFrame.setVisible(false);

        PersistentDataContainer container = itemFrame.getPersistentDataContainer();
        container.set(Keys.INVIS_FRAME, PersistentDataType.BOOLEAN, true);
    }
}
