package net.voidedmc85.takingabreak.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.voidedmc85.takingabreak.LayingServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @ModifyExpressionValue(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSleeping()Z"))
    private boolean stayInSleepingPose(boolean original) {
        return original || this instanceof LayingServerPlayer && ((LayingServerPlayer)this).takingabreak$isLaying();
    }
}
