package dev.RatFjc.ImperiumCore.modules.train;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import dev.RatFjc.ImperiumCore.modules.train.configuration.TrainDataSaver;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class PassengerCreator implements Listener {

    private static final Map<Location, Long> cache = new HashMap<>();
    private static long sync = System.currentTimeMillis();

    private static final Random random = new Random();

    private static final boolean enabled = TrainDataSaver.isEnabled();

    @EventHandler
    public void onTrainStop(SignActionEvent event) {
        if (!enabled) return;
        if (!event.isType("station")) return;
        if (System.currentTimeMillis() - sync < 1000) {
            sync = System.currentTimeMillis();
            return;
        }
        MinecartGroup train = event.getGroup();
        ejectTrainRandomly(train, TrainDataSaver.getCurrentChance());
        fillTrainRandomly(train, TrainDataSaver.getCurrentChance());
    }

    private void fillTrainRandomly(MinecartGroup train, float chance) {
        if (train == null) return;
        if (chance > 1) chance = 1;
        if (chance < 0) chance = 0;

        MinecartMember<?> first = train.getFirst();
        if (first == null) return;
        Block block = first.getBlock();
        if (block == null) return;
        Location location = block.getLocation();

        if (cache.containsKey(location)) {
            long current = System.currentTimeMillis();
            long saved = cache.getOrDefault(location, 0L);

            // Timeout = 60k ms
            // This is to hopefully prevent too many lookups at once, since
            // Location#getNearbyEntities can get expensive.
            if (current - saved < 60000) return;
        }

        for (MinecartMember<?> cart : train) {
            if (cart == null) continue; // In-case weird stuff happens
            if (!cart.getEntity().getPassengers().isEmpty()) return; // don't boot off an existing passenger
            if (random.nextFloat() >= chance) continue;

            LivingEntity result = DataUtil.randomElementFromList(validEntities(location));
            if (result == null) continue;
            cart.addPassengerForced(result);
        }
    }

    private void ejectTrainRandomly(MinecartGroup train, float chance) {
        if (train == null) return;
        if (chance > 1) chance = 1;
        if (chance < 0) chance = 0;

        for (MinecartMember<?> cart : train) {
            if (cart == null) continue;
            if (random.nextFloat() >= chance) continue;

            List<Entity> passengers = cart.getEntity().getPassengers();
            for (Entity passenger : passengers) if (passenger instanceof Player) continue;
            cart.eject();
        }
    }

    private List<? extends LivingEntity> validEntities(Location location) {
       Collection<LivingEntity> entities = location.getNearbyLivingEntities(48);
       cache.put(location, System.currentTimeMillis());
       return entities.stream()
               .filter(obj -> obj instanceof Animals)
               .map(obj -> (Animals) obj)
               .filter(obj -> obj.customName() == null)
               .filter(obj -> !obj.isInsideVehicle())
               .filter(obj -> {
                   if (obj instanceof Tameable tameable) {
                       return !tameable.isTamed();
                   }
                   if (obj instanceof Sittable sittable) {
                       return !sittable.isSitting();
                   }
                   return true;
               })
               .toList();
    }


}
