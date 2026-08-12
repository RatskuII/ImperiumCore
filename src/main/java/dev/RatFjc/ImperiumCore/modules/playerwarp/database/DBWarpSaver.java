package dev.RatFjc.ImperiumCore.modules.playerwarp.database;

import dev.RatFjc.ImperiumCore.modules.playerwarp.Warp;
import dev.RatFjc.ImperiumCore.modules.playerwarp.data.WarpUser;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.RatFjc.ImperiumCore.init.PlayerWarps.executor;
import static dev.RatFjc.ImperiumCore.init.PlayerWarps.sqLite;

public class DBWarpSaver {

    public static CompletableFuture<Boolean> saveWarp(Warp warp) {
        if (!databaseCheck()) return CompletableFuture.failedFuture(new NullPointerException("The database was not initialized."));
        return CompletableFuture.supplyAsync(() -> sqLite.save(warp), executor());
    }

    public static CompletableFuture<Boolean> saveUser(WarpUser warpUser) {
        if (!databaseCheck()) return CompletableFuture.failedFuture(new NullPointerException("The database was not initialized."));
        return CompletableFuture.supplyAsync(() -> sqLite.save(warpUser), executor());
    }

    public static CompletableFuture<Boolean> updateWarp(Warp warp) {
        return CompletableFuture.supplyAsync(() -> sqLite.update(warp), executor());
    }

    public static CompletableFuture<Warp> getWarp(String name) {
        return CompletableFuture.supplyAsync(() -> sqLite.warp(name), executor());
    }

    public static CompletableFuture<WarpUser> getWarpUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> sqLite.retrieve(uuid), executor());
    }

    public static CompletableFuture<List<Warp>> getWarps(WarpUser warpUser) {
        return CompletableFuture.supplyAsync(() -> sqLite.warps(warpUser), executor());
    }

    public static CompletableFuture<List<Warp>> getWarps() {
        return CompletableFuture.supplyAsync(() -> sqLite.warps(), executor());
    }

    public static CompletableFuture<Void> removeWarp(Warp warp) {
        return CompletableFuture.runAsync(() -> sqLite.remove(warp), executor());
    }

    private static boolean databaseCheck() {
        return sqLite != null;
    }

}
