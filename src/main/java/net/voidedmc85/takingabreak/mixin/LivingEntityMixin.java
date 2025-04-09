package net.voidedmc85.takingabreak.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.voidedmc85.takingabreak.LayingServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @WrapOperation(method = "getSleepingPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;"))
    private Object/*💖 generics*/notSleepingButLaying(DataTracker tracker, TrackedData<?> data, Operation<?> original) {
        return this instanceof LayingServerPlayer && ((LayingServerPlayer)this).takingabreak$isLaying() ?
            Optional.empty() : // empty if laying down
            original.call(tracker, data);
    }
}
