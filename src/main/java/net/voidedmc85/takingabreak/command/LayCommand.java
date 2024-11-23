package net.voidedmc85.takingabreak.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class LayCommand {
    public static final Set<ServerPlayerEntity> LAYING_PLAYERS = new HashSet<>();

    // Register the command
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lay")
                .executes(LayCommand::execute));
    }

    // Command logic
    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            if (!LAYING_PLAYERS.contains(player)) {
                startLaying(player);
            } else {
                stopLaying(player);
            }
        }
        return 1;
    }

    private static void startLaying(ServerPlayerEntity player) {
        LAYING_PLAYERS.add(player);
        BlockPos pos = player.getBlockPos();
        player.sleep(pos); // Make the player start sleeping at their current position
        player.sendMessage(Text.literal("You are now laying down!"), false);
    }

    public static void stopLaying(ServerPlayerEntity player) {
        if (LAYING_PLAYERS.contains(player)) {
            player.wakeUp(false, false); // Wake the player without skipping the night or setting spawn
            LAYING_PLAYERS.remove(player);
            player.sendMessage(Text.literal("You stopped laying down!"), false);
        }
    }
}
