package net.voidedmc85.takingabreak;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.voidedmc85.takingabreak.command.LayCommand;
import net.voidedmc85.takingabreak.event.PlayerEvents;

public class TakingABreak implements ModInitializer {
	public static final String MOD_ID = "taking-a-break";

	@Override
	public void onInitialize() {
		// Register the /lay command
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LayCommand.register(dispatcher);
		});

		// Register the tick event
		ServerTickEvents.END_SERVER_TICK.register(PlayerEvents::onServerTick);
	}
}
