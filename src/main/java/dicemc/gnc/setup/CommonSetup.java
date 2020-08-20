package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.account.AccountManager;
import dicemc.gnc.account.commands.AccountCommandRoot;
import dicemc.gnc.datastorage.database.DatabaseManager;
import dicemc.gnc.guild.GuildManager;
import dicemc.gnc.land.ChunkManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = GnC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event) {
		Networking.registerMessages();
		GnC.ckMgr = new ChunkManager();	
		GnC.gMgr = new GuildManager();
		GnC.aMgr = new AccountManager();
	}
	
	@SubscribeEvent
	public static void onServerStart(FMLServerStartedEvent event) {
		if (Config.MARKET_USE_DB.get())  {GnC.DBMgr = new DatabaseManager();}
		GnC.ckMgr.setServer(event.getServer());
		GnC.aMgr.setServer(event.getServer());
	}
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		AccountCommandRoot.register(event.getDispatcher());
	}
}
