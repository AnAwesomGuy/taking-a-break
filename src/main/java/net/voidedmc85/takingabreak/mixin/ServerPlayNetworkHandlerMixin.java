package net.voidedmc85.takingabreak.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.voidedmc85.takingabreak.LayingServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @ModifyExpressionValue(method = "onClientCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSleeping()Z"))
    private boolean standUpOnLeaveBed(boolean original) {
        if (!original && this.player instanceof LayingServerPlayer layingPlayer && layingPlayer.takingabreak$isLaying())
            layingPlayer.takingabreak$standUp();
        return original;
    }


    @ModifyExpressionValue(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSleeping()Z"))
    private boolean iGuessIAmSleepingWhenIMove(boolean original) {
        return original || ((LayingServerPlayer)player).takingabreak$isLaying();
    }
}
