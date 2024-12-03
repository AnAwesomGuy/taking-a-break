package net.voidedmc85.takingabreak;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.voidedmc85.takingabreak.mixin.ServerPlayerEntityMixin;

/**
 * Interface implemented onto {@link ServerPlayerEntity} through a {@linkplain ServerPlayerEntityMixin mixin}.
 */
public interface LayingServerPlayer {
    /**
     * Toggles whether the player is laying down or not.
     *
     * @return {@code 1} if the player started laying down, {@code 0} if the player stood up, and {@code 2} if either action failed.
     */
    int takingabreak$toggleLay(ServerCommandSource source);

    void takingabreak$standUp();

    boolean takingabreak$isLaying();
}
