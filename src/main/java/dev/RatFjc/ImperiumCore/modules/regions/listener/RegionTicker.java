package dev.RatFjc.ImperiumCore.modules.regions.listener;

import dev.Fjc.localDiff.abstractions.types.LocalDifficulty;
import dev.RatFjc.ImperiumCore.PluginProvider;
import dev.RatFjc.ImperiumCore.extras.pairing.Pair;
import dev.RatFjc.ImperiumCore.init.Regions;
import dev.RatFjc.ImperiumCore.modules.regions.Region;
import dev.RatFjc.ImperiumCore.modules.regions.RegionProperty;
import dev.RatFjc.ImperiumCore.modules.regions.config.RegionBuilder;
import dev.RatFjc.ImperiumCore.modules.regions.events.RegionTransferEvent;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.EntityUtil;
import dev.RatFjc.ImperiumCore.utility.LogUtil;
import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class RegionTicker implements Listener, PluginProvider {

    private static final Map<UUID, Region> cachedRegions = new HashMap<>();

    private static final Random random = new Random();

    // Start the tracking process for each player that logs in
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Region cache = RegionBuilder.fromPosition(player.getLocation());
        if (cache != null) cachedRegions.put(player.getUniqueId(), cache);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        cachedRegions.remove(player.getUniqueId());
    }

    // Call transfer event when player enters new region
    @EventHandler
    public void positionTracker(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!EntityUtil.isActualBlockMovement(event)) return;

        Region oldRegion = cachedRegions.get(player.getUniqueId());
        Region now = RegionBuilder.fromPosition(event.getTo());
        if (now == null) return;

        if (oldRegion == null) {
            cachedRegions.put(player.getUniqueId(), now);
            BukkitUtil.callEvent(new RegionTransferEvent(player, now, null));
            return;
        }
        if (oldRegion.uuid().equals(now.uuid())) return;
        cachedRegions.put(player.getUniqueId(), now);
        BukkitUtil.callEvent(new RegionTransferEvent(player, now, oldRegion));
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

        // see i ended up using the uuid
        Map<UUID, Float> mapScale = new HashMap<>();
        Map<UUID, Float> mapStrength = new HashMap<>();
        nearbyPlayers.forEach(player -> {
            Region fromCache = cachedRegions.get(player.getUniqueId());
            if (fromCache == null) return;

            RegionProperty regionProperty = fromCache.properties();

            float scale = regionProperty.mobScale();
            mapScale.put(player.getUniqueId(), scale);

            float strength = regionProperty.mobStrength();
            mapStrength.put(player.getUniqueId(), strength);

            if (regionProperty.unnaturalMobs()) {
                List<? extends Class<? extends LivingEntity>> selectors = RegionBuilder.mobSelection();
                Class<? extends LivingEntity> selector = DataUtil.randomElementFromList(selectors);
                if (random.nextDouble() <= regionProperty.localDiffOffset() / 100) fromCache.spawn(livingEntity, selector, null);
            }
        });
        final Map<UUID, Float> scaleResults = DataUtil.sortByValue(mapScale);
        Pair<UUID, Float> highest = Pair.fromEntry(DataUtil.getLast(scaleResults));
        if (highest.value() != null) setAttribute(livingEntity, Attribute.SCALE, "region-mobScale", highest.value(), AttributeModifier.Operation.ADD_NUMBER);

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
