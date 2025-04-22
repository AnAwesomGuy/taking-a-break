package net.voidedmc85.takingabreak.mixin;

import net.minecraft.entity.LivingEntity;
import net.voidedmc85.takingabreak.LayingServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "isSleeping", at = @At("HEAD"), cancellable = true)
    private void notSleepingButLaying(CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof LayingServerPlayer && ((LayingServerPlayer)this).takingabreak$isLaying())
            cir.setReturnValue(false);
    }
}
