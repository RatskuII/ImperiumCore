package dev.RatFjc.ImperiumCore.modules.claimfly;

import dev.RatFjc.ImperiumCore.Keys;
import dev.RatFjc.ImperiumCore.extras.hooks.GPHook;
import dev.RatFjc.ImperiumCore.utility.PDCUtil;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class FlyManagement {

    private static final String flyGlobal = "imperium.fly.global";
    private static final String flyClaim = "imperium.fly.claim";

    protected boolean checkGlobal(Player player) {
        return player.hasPermission(flyGlobal);
    }

    protected boolean checkClaimOnly(Player player) {
        return player.hasPermission(flyClaim) && GPHook.isInClaim(player) != null;
    }

    protected synchronized FlyState flyStatus(Player player) {
        return PDCUtil.get(player, Keys.FLYSTATE, new FlyStore());
    }

    protected synchronized void setFly(Player player, FlyState flyState) {
        PDCUtil.set(player, Keys.FLYSTATE, new FlyStore(), flyState);
        if (flyState != FlyState.NOT_FLYING) {
            player.setAllowFlight(true);
        }
    }

    private static class FlyStore implements PersistentDataType<byte[], FlyState> {

        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<FlyState> getComplexType() {
            return FlyState.class;
        }

        @Override
        public byte @NonNull [] toPrimitive(@NonNull FlyState complex, @NotNull PersistentDataAdapterContext context) {
            return switch (complex) {
                case NOT_FLYING -> new byte[]{0};
                case FLYING -> new byte[]{1};
                case FLYING_CLAIM -> new byte[]{2};
            };
        }

        @Override
        public @NonNull FlyState fromPrimitive(byte @NonNull [] primitive, @NotNull PersistentDataAdapterContext context) {
            byte target = primitive[0];
            return switch (target) {
                case 1 -> FlyState.FLYING;
                case 2 -> FlyState.FLYING_CLAIM;
                default -> FlyState.NOT_FLYING;
            };
        }
    }
}
