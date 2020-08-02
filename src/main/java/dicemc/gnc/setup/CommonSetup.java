package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.database.DatabaseManager;
import dicemc.gnc.land.ChunkManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = GnC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event) {
		Networking.registerMessages();
		GnC.ckMgr = new ChunkManager();	
		//TODO guild Manager
	}
	
	@SubscribeEvent
	public static void onServerStart(FMLServerStartedEvent event) {
		if (Config.MARKET_USE_DB.get())  {GnC.DBMgr = new DatabaseManager();}
	}
}
