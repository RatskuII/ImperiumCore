package dev.RatFjc.ImperiumCore.modules.train.signadder;

import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.util.*;

public class TrainPopulator extends SignAction implements Listener {

    private static final Random random = new Random();

    @Override
    public boolean match(SignActionEvent info) {
        return info.isType("populate");
    }

    @Override
    public void execute(SignActionEvent info) {
        if (info.isTrainSign() && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)) {
            MinecartGroup train = info.getGroup();
            fillTrainRandomly(train, info);
        }
    }

    @Override
    public boolean build(SignChangeActionEvent event) {
        String chance = event.getLine(2);
        float result = DataUtil.parseFloat(chance);

        Player player = event.getPlayer();
        TextUtil.sendMessage(player, "Successfully set the chance to " + result + ".", "Note: invalid inputs will be sanitized to 0 automatically.");
        return SignBuildOptions.create()
                .setPermission(Permission.BUILD_SPAWNER)
                .setName("passenger spawner!")
                .setDescription("populates the train with nearby passengers.")
                .handle(event);
    }

    private void fillTrainRandomly(MinecartGroup train, SignActionEvent event) {
        if (train == null) return;
        String floatChance = event.getLine(2);
        float chance = DataUtil.parseFloat(floatChance);
        if (chance > 1) chance = 1;
        if (chance < 0) chance = 0;

        MinecartMember<?> first = train.getFirst();
        if (first == null) return;
        Block block = first.getBlock();
        if (block == null) return;
        Location location = block.getLocation();

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
            if (!DataUtil.containsType(passengers, Player.class)) cart.eject();

        }
    }

    private List<? extends LivingEntity> validEntities(Location location) {
        Collection<LivingEntity> entities = location.getNearbyLivingEntities(48);
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
