package net.voidedmc85.takingabreak.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.voidedmc85.takingabreak.LayHandler;
import net.voidedmc85.takingabreak.LayingServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements LayingServerPlayer {
    @Unique
    private LayHandler layHandler;

    @SuppressWarnings("DataFlowIssue")
    private ServerPlayerEntityMixin() {
        super(null, null, 0F, null);
    }

    @Override
    public int takingabreak$toggleLay(ServerCommandSource source) {
        LayHandler layHandler = this.layHandler;
        if (layHandler == null) {
            if (!this.isOnGround()) {
                source.sendError(Text.translatable("taking-a-break.commands.lay.failed.notOnGround"));
                return 2;
            }

            if (this.isInSwimmingPose() || this.isInsideWall()) {
                source.sendError(Text.translatable("taking-a-break.commands.lay.failed.cramped"));
                return 2;
            }

            this.layHandler = new LayHandler((ServerPlayerEntity)(Object)this);
            return 1;
        }

        layHandler.standUp();
        this.layHandler = null;
        return 0;
    }

    @Override
    public void takingabreak$standUp() {
        LayHandler layHandler = this.layHandler;
        if (layHandler != null) {
            layHandler.standUp();
            this.layHandler = null;
        }
    }

    @Override
    public boolean takingabreak$isLaying() {
        return this.layHandler != null;
    }

    @Inject(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tick()V"))
    private void standUpOnSneak(CallbackInfo ci) {
        LayHandler layHandler = this.layHandler;
        // is laying down AND (is sneaking OR moved from original laying position)
        if (layHandler != null && (this.shouldDismount() || !this.getBlockPos().equals(layHandler.layingPosition))) {
            layHandler.standUp();
            this.layHandler = null;
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private void standUpOnDamage(CallbackInfoReturnable<Boolean> cir) {
        takingabreak$standUp();
    }
}
