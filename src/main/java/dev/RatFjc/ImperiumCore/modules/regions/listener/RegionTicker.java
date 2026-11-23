package dev.RatFjc.ImperiumCore.modules.regions.listener;

import dev.Fjc.localDiff.abstractions.types.LocalDifficulty;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.init.Regions;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import dev.RatFjc.ImperiumCore.modules.regions.config.RegionBuilder;
import dev.RatFjc.ImperiumCore.modules.regions.events.RegionTransferEvent;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RegionTicker implements Listener, PluginProvider {

    private static final Map<UUID, Location> cachedLocations = new HashMap<>();

    /**
     * Occasionally ticks the server to update a cached position.
     * @param player The player being tracked
     */
    public void tick(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = Threader.tick(() -> cachedLocations.put(uuid, player.getLocation()), 100);
        if (!player.isOnline() || !player.isValid()) {
            task.cancel();
            cachedLocations.remove(uuid);
        }
    }

    // Start the tracking process for each player that logs in
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tick(player);
    }

    // Call transfer event when player enters new region
    @EventHandler
    public void positionTracker(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Region oldRegion = RegionBuilder.fromPosition(cachedLocations.get(player.getUniqueId()));
        Region result = RegionBuilder.fromPosition(location);
        if (result == null) return;
        if (oldRegion != null) {
            if (!result.uuid().equals(oldRegion.uuid())) BukkitUtil.callEvent(new RegionTransferEvent(player, result, oldRegion));
        } else BukkitUtil.callEvent(new RegionTransferEvent(player, result, null));
    }

    // Inform player of region entered
    // Set local diff offset
    @EventHandler
    public void onRegionMove(RegionTransferEvent event) {
        Player player = event.player();
        Region old = event.oldRegion();
        if (old != null) {
            String regionName = old.properties().name();
            double oldOffset = old.properties().localDiffOffset();

            LogUtil.debug("Player " + player.getName() + " left region " + regionName, new Regions());
            LocalDifficulty.offset(-oldOffset);
        }
        Region now = event.region();
        String regionName = now.properties().name();
        LogUtil.debug("Player " + player.getName() + " entered region " + regionName, new Regions());

        double offset = now.properties().localDiffOffset();
        LocalDifficulty.offset(offset);
    }

    // PROPERTIES

    // Modify behaviors
    public void modify(Player player) {
        Region region = RegionBuilder.fromPosition(player.getLocation());
        if (region == null) return;
        RegionProperty property = region.properties();

        //todo spawn limits are global which means i cant just change it. ill need to somehow define my own limits
        // arbitrarily. for now this will do nothing
        int currentRate = Bukkit.getServer().getSpawnLimit(SpawnCategory.MONSTER);
        int newRate = Math.toIntExact(currentRate + property.mobSpawnRate());
    }

    // Change mob scale and strength
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) return;
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) return;

        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.customName() != null) return;
        if (livingEntity instanceof Animals) return;
        if (!(livingEntity instanceof Monster)) return;
        List<Player> nearbyPlayers = livingEntity.getNearbyEntities(32, 32, 32).stream()
                .filter(Objects::nonNull)
                .filter(obj -> obj instanceof Player)
                .map(obj -> (Player) obj)
                .toList();

        // I don't actually need the UUID to do this operation, but I might use it later
        Map<UUID, Float> mapScale = new HashMap<>();
        Map<UUID, Float> mapStrength = new HashMap<>();
        nearbyPlayers.forEach(player -> {
            Region region = RegionBuilder.fromPosition(player.getLocation());
            if (region == null) return;

            RegionProperty regionProperty = region.properties();

            float scale = regionProperty.mobScale();
            mapScale.put(player.getUniqueId(), scale);

            float strength = regionProperty.mobStrength();
            mapStrength.put(player.getUniqueId(), strength);
        });
        final Map<UUID, Float> scaleResults = DataUtil.sortByValue(mapScale);
        Pair<UUID, Float> highest = Pair.fromEntry(DataUtil.getLast(scaleResults));
        if (highest.value() != null) setAttribute(livingEntity, Attribute.SCALE, "region-mobScale", highest.value(), AttributeModifier.Operation.ADD_SCALAR);

        final Map<UUID, Float> strengthResults = DataUtil.sortByValue(mapStrength);
        Pair<UUID, Float> highestStrength = Pair.fromEntry(DataUtil.getLast(strengthResults));
        if (highestStrength.value() != null) setAttribute(livingEntity, Attribute.ATTACK_DAMAGE, "region-mobStrength", highestStrength.value(), AttributeModifier.Operation.ADD_SCALAR);
    }

    private void setAttribute(Attributable attributable, Attribute attribute, String key, double impact, AttributeModifier.Operation operation) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        AttributeModifier modifier = new AttributeModifier(namespacedKey, impact, operation);
        var instance = attributable.getAttribute(attribute);
        if (instance != null) instance.addModifier(modifier);
    }
}
