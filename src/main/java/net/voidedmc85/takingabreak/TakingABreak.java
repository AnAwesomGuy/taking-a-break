package net.voidedmc85.takingabreak;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TakingABreak implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {
                String playerArg = "player";
                dispatcher.register(
                    CommandManager.literal("lay")
                                  .then(
                                      CommandManager.argument(playerArg, EntityArgumentType.player())
                                                    .requires(source -> source.hasPermissionLevel(2))
                                                    .executes(ctx -> ((LayingServerPlayer)EntityArgumentType.getPlayer(
                                                        ctx, playerArg)).takingabreak$toggleLay(ctx.getSource())))
                                  .executes(ctx -> {
                                      ServerCommandSource source = ctx.getSource();
                                      return ((LayingServerPlayer)source.getPlayerOrThrow())
                                          .takingabreak$toggleLay(source);
                                  })
                );
            }
        );
    }
}
