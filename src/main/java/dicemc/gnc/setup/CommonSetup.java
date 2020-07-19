package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.database.DatabaseManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = GnC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event) {
		Networking.registerMessages();		
	}
	
	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		GnC.DBM_MAIN = new DatabaseManager(true);
		boolean useAlt = (Config.DB_URL.get().equalsIgnoreCase(Config.DB2_URL.get()) &&
				Config.DB_NAME.get().equalsIgnoreCase(Config.DB2_NAME.get()) &&
				Config.DB_PORT.get().equalsIgnoreCase(Config.DB2_PORT.get()));
		GnC.DBM_ALT = useAlt ? new DatabaseManager(false) : new DatabaseManager(true);
		GnC.ckMgr = new ChunkManager();
	}
}
