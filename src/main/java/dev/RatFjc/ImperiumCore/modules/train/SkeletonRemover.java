package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import dev.RatFjc.ImperiumCore.modules.train.configuration.TrainDataSaver;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class SkeletonRemover implements Listener {

    @EventHandler
    public void onSkeletonEnter(VehicleEnterEvent event) {
        if (!TrainDataSaver.skeletonKillingAllowed()) return;
        Vehicle vehicle = event.getVehicle();
        if (MinecartMemberStore.getFromEntity(vehicle) == null) return;

        Entity entity = event.getEntered();
        if (entity instanceof AbstractSkeleton skeleton) skeleton.setHealth(0);
    }
}
