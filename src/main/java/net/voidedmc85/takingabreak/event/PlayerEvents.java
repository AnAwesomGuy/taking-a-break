package net.voidedmc85.takingabreak.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.voidedmc85.takingabreak.command.LayCommand;

public class PlayerEvents {
    public static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (LayCommand.LAYING_PLAYERS.contains(player) && player.isSneaking()) {
                LayCommand.stopLaying(player);
            }
        }
    }
}
