package dev.RatFjc.ImperiumCore.modules.train.signadder;

import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import dev.RatFjc.ImperiumCore.extras.Streamer;
import dev.RatFjc.ImperiumCore.extras.multithreading.Threader;
import dev.RatFjc.ImperiumCore.utility.DataUtil;
import dev.RatFjc.ImperiumCore.utility.EntityUtil;
import dev.RatFjc.ImperiumCore.utility.TextUtil;
import io.papermc.paper.entity.Bucketable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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

            String tags = info.getLine(3);
            Collection<String> result = new ArrayList<>();
            Collections.addAll(result, tags.split(","));
            fillTrainRandomly(train, info, result.toArray(new String[0]));
        }
    }

    @Override
    public boolean build(SignChangeActionEvent event) {
        String chance = event.getLine(2);
        float result = DataUtil.parseFloat(chance);

        // Whether hostile mobs should be included
        // Valid entries include animal, hostile, villager
        // Entities with names are always excluded
        // animal is default
        String mobs = event.getLine(3);
        if (mobs != null) mobs = mobs.strip();

        Player player = event.getPlayer();

        if (mobs == null || mobs.isBlank()) mobs = "animal";
        String[] outlets = mobs.split(",");
        for (String out : outlets) {
            if (!DataUtil.equalsAny(out, "animal", "hostile", "villager")) {
                TextUtil.sendMessage(player, "The flag provided is invalid.", "Valid types include animal, hostile, villager");
                return false;
            }
        }

        TextUtil.sendMessage(player, "Successfully set the chance to " + result + ".", "Note: invalid inputs will be sanitized to 0 automatically.");
        TextUtil.sendMessage(player, "Set the filtering method to " + mobs.toUpperCase());
        return SignBuildOptions.create()
                .setPermission(Permission.BUILD_SPAWNER)
                .setName("passenger spawner")
                .setDescription("populates the train with nearby passengers")
                .handle(event);
    }

    private void fillTrainRandomly(MinecartGroup train, SignActionEvent event, String... tags) {
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

        // Choose carts first
        List<MinecartMember<?>> carts = new ArrayList<>();
        for (MinecartMember<?> cart : train) {
            if (cart == null) continue;
            if (!cart.getEntity().getPassengers().isEmpty()) continue;
            if (random.nextFloat() >= chance) continue;
            carts.add(cart);
        }

        if (carts.isEmpty()) return;

        // Run operations on carts
        // Make sure everything stays on main thread
        CompletableFuture<List<LivingEntity>> targets = validEntities(location, tags);
        targets.thenAccept(list -> Threader.execute(() -> {
            for (MinecartMember<?> cart : carts) {
                if (list.isEmpty()) break;

                if (!cart.getEntity().isValid()) continue;
                if (!cart.getEntity().getPassengers().isEmpty()) continue;

                LivingEntity selected = DataUtil.randomElementFromList(list);
                if (selected == null || selected.isDead() || !selected.isValid()) continue;
                cart.addPassengerForced(selected);
                list.remove(selected);
            }
        }));

    }

    @Deprecated
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

    /**
     * Loads a grid of chunks asynchronously.
     * @param start The initial location to start from
     * @return A future that contains all the loaded chunks
     */
    private CompletableFuture<List<Chunk>> loadGrid(Location start) {
        World world = start.getWorld();
        Chunk chunk = start.getChunk();
        int centerX = chunk.getX();
        int centerZ = chunk.getZ();

        // Location center = new Location(world, centerX, start.getBlockY(), centerZ);

        final int RADIUS = 3;
        List<CompletableFuture<Chunk>> out = new ArrayList<>();

        for (int x = -RADIUS; x <= RADIUS; x++) for (int z = -RADIUS; z <= RADIUS; z++) {
            out.add(world.getChunkAtAsync(centerX + x, centerZ + z));
        }
        return CompletableFuture.allOf(out.toArray(new CompletableFuture[0]))
                .thenApply(result -> out.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    /**
     * Gets a list of entities that are valid for train population.
     * @param location The location to start searching
     * @param tags An array of tags that can define which entities are valid
     * @return A future that contains a valid list of entities
     * @apiNote This future is returned on the main thread
     */
    private CompletableFuture<List<LivingEntity>> validEntities(Location location, String... tags) {
        if (tags.length == 0) tags = new String[]{"animal"};
        final String[] tags0 = tags;
        CompletableFuture<List<Chunk>> chunks = loadGrid(location);

        return chunks.thenCompose(loaded -> Threader.supplyFuture(() -> {
            Collection<LivingEntity> entities = location.getNearbyLivingEntities(32);
            Streamer<LivingEntity> entityStream = new Streamer<>(entities);

            for (String tag : tags0) {
                switch (tag.toLowerCase()) {
                    case "animal" -> entityStream.filter(obj -> obj instanceof Animals);
                    case "hostile" -> entityStream.filter(obj -> obj instanceof Mob);
                }
                if (!tag.equalsIgnoreCase("villager")) entityStream.filter(obj -> !(obj instanceof Villager));
            }

            return entityStream
                    .filter(obj -> !(obj instanceof Bucketable)) // Seeing axolotls asphyxiate is sad
                    .filter(obj -> !(obj instanceof WaterMob)) // Fish should not be in a train
                    .filter(obj -> !(obj instanceof AbstractSkeleton)) // i hate skeletons
                    .filter(obj -> !(obj instanceof Creeper)) // he came out of nowhere and blew me up
                    .filter(obj -> !(obj instanceof ArmorStand)) // ???????
                    .filter(obj -> obj.customName() == null)
                    .filter(obj -> !obj.isInsideVehicle())
                    .filter(obj -> !EntityUtil.isTamed(obj))
                    .filter(obj -> !EntityUtil.sitting(obj))
                    .list();
        }));
    }
}
