package net.voidedmc85.takingabreak;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class LayHandler {
    private static final BlockState OCCUPIED_BED = Blocks.GRAY_BED.getDefaultState()
                                                                  .with(BedBlock.OCCUPIED, Boolean.TRUE)
                                                                  .with(BedBlock.PART, BedPart.HEAD);

    public final ServerPlayerEntity player;
    public final BlockPos fakeBedPos;
    public final BlockState previousState;
    public final BlockPos layingPosition;

    public LayHandler(ServerPlayerEntity player) {
        this.player = player;

        // lay down functionality below
        if (player.hasVehicle())
            player.stopRiding();
        // lay down pose
        player.setPose(EntityPose.SLEEPING);
        player.setVelocity(Vec3d.ZERO);
        player.velocityDirty = true; // send the velocity update to client
        player.setPosition(player.getPos().add(0, 0.2, 0));
        this.layingPosition = player.getBlockPos();
        World world = player.getWorld();
        BlockPos pos = this.fakeBedPos = player.getBlockPos().withY(world.getBottomY());
        // make the player "sleep" TODO find a way to make the player not actually sleep (data tracker sync mixins? EntityTrackerEntry#syncEntityData)
        player.setSleepingPosition(pos);
        this.previousState = world.getBlockState(pos);
        // trick the client into thinking that there's a bed at the bottom of the world
        player.networkHandler.sendPacket( // make the bed be in the same facing as the player so
            new BlockUpdateS2CPacket(pos, OCCUPIED_BED.with(BedBlock.FACING, player.getHorizontalFacing().getOpposite())));
    }

    public void standUp() {
        ServerPlayerEntity player = this.player;
        // remove the fake bed
        player.networkHandler.sendPacket(
            new BlockUpdateS2CPacket(this.fakeBedPos, this.previousState));
        // this will make the player "wake up"
        player.clearSleepingPosition();
        player.setPose(EntityPose.STANDING); // stand up
        // stand up animation
        player.getServerWorld().getChunkManager().sendToNearbyPlayers(player, new EntityAnimationS2CPacket(player, EntityAnimationS2CPacket.WAKE_UP));
    }
}
