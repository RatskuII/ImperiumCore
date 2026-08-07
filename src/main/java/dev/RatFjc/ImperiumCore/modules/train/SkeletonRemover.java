package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class SkeletonRemover implements Listener {

    @EventHandler
    public void onSkeletonEnter(VehicleEnterEvent event) {

        Vehicle vehicle = event.getVehicle();
        if (MinecartMemberStore.getFromEntity(vehicle) == null) return;

        Entity entity = event.getEntered();
        if (entity instanceof AbstractSkeleton skeleton) skeleton.setHealth(0);
    }
}
