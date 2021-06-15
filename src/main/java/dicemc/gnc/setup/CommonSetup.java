package dicemc.gnc.setup;

import java.util.ArrayList;
import java.util.Arrays;

import dicemc.gnc.GnC;
import dicemc.gnc.account.commands.CmdAccountRoot;
import dicemc.gnc.common.commands.CmdAdminRoot;
import dicemc.gnc.guild.commands.CmdGuildRoot;
import dicemc.gnc.guild.events.GuildEvents;
import dicemc.gnclib.configs.ConfigCore;
import net.minecraftforge.event.RegisterCommandsEvent;
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
		GuildEvents.worlds = event.getServer().getAllLevels();
		ConfigCore.defineDataStorageConfigValues(
				Config.DB_PORT.get(), 
				Config.DB_NAME.get(), 
				Config.DB_SERVICE.get(), 
				Config.DB_URL.get().length() > 0 ? Config.DB_URL.get() : event.getServer().getServerDirectory().getAbsolutePath() + "\\saves", 
				Config.DB_USER.get(), 
				Config.DB_PASS.get());
		ConfigCore.defineGuildConfigValues(
				event.getServer().overworld().toString(), 
				Config.GLOBAL_TAX_RATE.get(), 
				Config.GLOBAL_TAX_INTERVAL.get(), 
				Config.GUILD_CREATE_COST.get(), 
				Config.GUILD_NAME_CHANGE_COST.get(), 
				Config.GUILD_RANK_ADD_COST.get());
		ConfigCore.defineMoneyConfigValues(
				event.getServer().overworld().toString(), 
				Config.STARTING_FUNDS.get(), 
				Config.GUILD_STARTING_FUNDS.get());
		ConfigCore.defineProtectionConfigValues(
				Config.UNOWNED_PROTECTED.get(), 
				new ArrayList<String>(Arrays.asList("minecraft:end")), //(List<String>)Config.UNOWNED_WHITELIST.get(), 
				new ArrayList<String>(Arrays.asList("minecraft:grass")));//(List<String>)Config.PROTECTED_DIMENSION_BLACKLIST.get());
		ConfigCore.defineRealEstateConfigValues(
				Config.DEFAULT_LAND_PRICE.get(), 
				Config.TEMPCLAIM_DURATION.get(), 
				Config.CHUNKS_PER_MEMBER.get(), 
				Config.TEMPCLAIM_RATE.get(), 
				Config.LAND_ABANDON_REFUND_RATE.get(), 
				Config.OUTPOST_CREATE_COST.get(), 
				Config.AUTO_TEMPCLAIM.get(), 
				Config.TENANT_PROTECTION_RATIO.get());
		ConfigCore.defineTradeConfigValues(
				event.getServer().overworld().toString(), 
				Config.MARKET_GLOBAL_TAX_BUY.get(), 
				Config.MARKET_GLOBAL_TAX_SELL.get(), 
				Config.MARKET_AUCTION_TAX_SELL.get(), 
				Config.AUCTION_OPEN_DURATION.get(), 
				Config.PAGE_SIZE.get());
	}
	
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		CmdAdminRoot.register(event.getDispatcher());
		CmdAccountRoot.register(event.getDispatcher());
		CmdGuildRoot.register(event.getDispatcher());
	}
}
