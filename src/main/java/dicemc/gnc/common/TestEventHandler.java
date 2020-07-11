package dicemc.gnc.common;

import dicemc.gnc.command.impl.DBCommands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber
public class TestEventHandler {
	public static MySQLCon dbConn;

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		DBCommands.register(event.getCommandDispatcher());
		dbConn = new MySQLCon();
	}

}

