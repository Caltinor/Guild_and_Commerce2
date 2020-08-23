package dicemc.gnc.setup;

import dicemc.gnc.GnC;
import dicemc.gnc.account.AccountManager;
import dicemc.gnc.account.commands.AccountCommandRoot;
import dicemc.gnc.datastorage.database.DatabaseManager;
import dicemc.gnc.datastorage.wsd.MarketWSD;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import dicemc.gnc.guild.GuildManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = GnC.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {
	public static void init(final FMLCommonSetupEvent event) {
		Networking.registerMessages();
		GnC.gMgr = new GuildManager();
		GnC.aMgr = new AccountManager();
	}
	
	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		if (Config.MARKET_USE_DB.get())  {GnC.DBMgr = new DatabaseManager();}	
		else {MarketWSD.get(event.getServer().getWorld(event.getServer().func_241755_D_().func_234923_W_()));}
		WorldWSD.get(event.getServer().getWorld(event.getServer().func_241755_D_().func_234923_W_()));
		GnC.aMgr.setServer(event.getServer());
		GnC.gMgr.setServer(event.getServer());
	}
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		AccountCommandRoot.register(event.getDispatcher());
	}
}
