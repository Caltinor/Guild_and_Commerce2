package dicemc.gnc.setup;

import java.util.List;

import dicemc.gnclib.configs.ConfigCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
	
	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	public static ForgeConfigSpec SERVER_CONFIG;
	
	public static final String CATEGORY_SERVER = "server";
	public static final String CATEGORY_CLIENT = "client";
	public static final String CATEGORY_COMMON = "common";
	
	//Data Storage Scheme Variables
	//Primary Database config values
	public static ForgeConfigSpec.ConfigValue<String> DB_PORT;
	public static ForgeConfigSpec.ConfigValue<String> DB_NAME;
	public static ForgeConfigSpec.ConfigValue<String> DB_SERVICE;
	public static ForgeConfigSpec.ConfigValue<String> DB_URL;
	public static ForgeConfigSpec.ConfigValue<String> DB_USER;
	public static ForgeConfigSpec.ConfigValue<String> DB_PASS;
	//Money Related Variables
	public static ForgeConfigSpec.ConfigValue<Double> STARTING_FUNDS;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_STARTING_FUNDS;
	//Guild Related Variables
	public static ForgeConfigSpec.ConfigValue<Double> GLOBAL_TAX_RATE;
	public static ForgeConfigSpec.ConfigValue<Long> GLOBAL_TAX_INTERVAL;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_NAME_CHANGE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_RANK_ADD_COST;
	//Real Estate Related Variables
	public static ForgeConfigSpec.ConfigValue<Double> DEFAULT_LAND_PRICE;
	public static ForgeConfigSpec.ConfigValue<Long> TEMPCLAIM_DURATION;
	public static ForgeConfigSpec.ConfigValue<Integer> CHUNKS_PER_MEMBER;
	public static ForgeConfigSpec.ConfigValue<Double> TEMPCLAIM_RATE;
	public static ForgeConfigSpec.ConfigValue<Double> LAND_ABANDON_REFUND_RATE;	
	public static ForgeConfigSpec.ConfigValue<Double> OUTPOST_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Boolean> AUTO_TEMPCLAIM;
	public static ForgeConfigSpec.ConfigValue<Double> TENANT_PROTECTION_RATIO;
	//Protection Related Variables
	public static ForgeConfigSpec.ConfigValue<Boolean> UNOWNED_PROTECTED;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> PROTECTED_DIMENSION_BLACKLIST;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> UNOWNED_WHITELIST;
	//Trade Related Variables	
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_BUY;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_SELL;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_AUCTION_TAX_SELL;	
	public static ForgeConfigSpec.ConfigValue<Long> AUCTION_OPEN_DURATION;
	public static ForgeConfigSpec.ConfigValue<Integer> PAGE_SIZE;
	
	static {
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		
		CLIENT_BUILDER.comment("Client Settings").push(CATEGORY_CLIENT);
		CLIENT_BUILDER.pop();
		
		COMMON_BUILDER.comment("Common Settings").push(CATEGORY_COMMON);		
		COMMON_BUILDER.pop();
		
		SERVER_BUILDER.comment("Server Settings").push(CATEGORY_SERVER);	
		setupBlockConfig_DB(SERVER_BUILDER);
		setupBlockMoney(SERVER_BUILDER);
		setupBlockGuild(SERVER_BUILDER);
		setupBlockRealEstate(SERVER_BUILDER);
		setupBlockProtection(SERVER_BUILDER);
		setupBlockTrade(SERVER_BUILDER);
		SERVER_BUILDER.pop();
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
		COMMON_CONFIG = COMMON_BUILDER.build();
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
	
	private static void setupBlockConfig_DB(ForgeConfigSpec.Builder builder) {
		builder.comment("Database Settings").push("db");
		
		//Database location
		DB_PORT = builder.comment("Database port")
				.define("port", ConfigCore.DB_PORT);
		DB_NAME = builder.comment("Database name")
				.define("name", ConfigCore.DB_NAME);
		DB_SERVICE = builder.comment("Database service please leave as 'H2' until further support is added")
				.define("service", ConfigCore.DB_SERVICE);
		DB_URL = builder.comment("Database URL")
				.define("url", ConfigCore.DB_URL);
		DB_USER = builder.comment("Database Username")
				.define("user", ConfigCore.DB_USER);
		DB_PASS = builder.comment("Database Password")
				.define("pass", ConfigCore.DB_PASS);		
		builder.pop();		
	}
	
	private static void setupBlockMoney(ForgeConfigSpec.Builder builder) {
		builder.comment("Money configuration settings").push("money");
		
		STARTING_FUNDS = builder.comment("The amount of money a new player starts with when first joining the world.")
				.defineInRange("starting_funds", ConfigCore.STARTING_FUNDS, 0, Double.MAX_VALUE);
		GUILD_STARTING_FUNDS = builder.comment("An amount of money given to guilds after creation.  This serves as a rebate to guild creation.")
				.defineInRange("guild_starting_funds", ConfigCore.GUILD_STARTING_FUNDS, 0, Double.MAX_VALUE);
		
		builder.pop();
	}
	
	private static void setupBlockGuild(ForgeConfigSpec.Builder builder) {
		builder.comment("Guild configuration settings").push("guild");
		
		GLOBAL_TAX_RATE = builder.comment("The global tax applied to all guilds when triggered.")
				.defineInRange("global_tax_rate", ConfigCore.GLOBAL_TAX_RATE, 0, 1D);
		GLOBAL_TAX_INTERVAL = builder.comment("The interval in miliseconds between guild taxation.")
				.defineInRange("global_tax_interval", ConfigCore.GLOBAL_TAX_INTERVAL, 0, Long.MAX_VALUE);
		GUILD_CREATE_COST = builder.comment("The cost to create a new guild.")
				.defineInRange("guild_create_cost", ConfigCore.GUILD_CREATE_COST, 0, Double.MAX_VALUE);
		GUILD_NAME_CHANGE_COST = builder.comment("The cost to change a guild's name.")
				.defineInRange("guild_name_change_cost", ConfigCore.GUILD_NAME_CHANGE_COST, 0, Double.MAX_VALUE);
		GUILD_RANK_ADD_COST = builder.comment("The cost modifier to add a new guild rank.")
				.defineInRange("guild_rank_add_cost", ConfigCore.GUILD_RANK_ADD_COST, 0, Double.MAX_VALUE);
		
		builder.pop();
	}
	
	private static void setupBlockRealEstate(ForgeConfigSpec.Builder builder) {
		builder.comment("Land configuration settings").push("land");
		
		DEFAULT_LAND_PRICE = builder.comment("The price of land when generating for the first time.")
				.defineInRange("default_land_price", ConfigCore.DEFAULT_LAND_PRICE, 0, Integer.MAX_VALUE);
		TEMPCLAIM_DURATION = builder.comment("The length of time (in miliseconds) a temp claim is protected for.")
				.defineInRange("tempclaim_duration", ConfigCore.TEMPCLAIM_DURATION, 0, Long.MAX_VALUE);
		CHUNKS_PER_MEMBER = builder.comment("The number of chunks claimable per member that is untaxed.)")
				.defineInRange("chunks_per_member", ConfigCore.CHUNKS_PER_MEMBER, 0, Integer.MAX_VALUE);
		TEMPCLAIM_RATE = builder.comment("The percentage of the land's cost that must be paid to temporarily claim land.")
				.defineInRange("tempclaim_rate", ConfigCore.TEMPCLAIM_RATE, 0, 1D);
		LAND_ABANDON_REFUND_RATE = builder.comment("The percentage refunded to the guild when abandoning claims.")
				.defineInRange("land_abandon_refund_rate", ConfigCore.LAND_ABANDON_REFUND_RATE, 0, 1D);
		OUTPOST_CREATE_COST = builder.comment("The cost to establish a new outpost.")
				.defineInRange("outpost_create_cost", ConfigCore.OUTPOST_CREATE_COST, 0, Double.MAX_VALUE);
		AUTO_TEMPCLAIM = builder.comment("determines whether auto-claiming is permitted by the server")
				.define("auto tempclaim", ConfigCore.AUTO_TEMPCLAIM);
		TENANT_PROTECTION_RATIO = builder.comment("the percentage of rental interval that locks owners from modifying rental details")
				.defineInRange("tenant protections", ConfigCore.TENANT_PROTECTION_RATIO, 0, Double.MAX_VALUE);
		
		builder.pop();
	}
	
	private static void setupBlockProtection(ForgeConfigSpec.Builder builder) {
		builder.comment("Land configuration settings").push("prot");
		
		UNOWNED_PROTECTED = builder.comment("Determines if unowned land is protected. setting to false will make only land that is purchased protected.")
				.define("unowned_protected", ConfigCore.UNOWNED_PROTECTED);
		PROTECTED_DIMENSION_BLACKLIST = builder.comment("dimensions excluded from protection checks")
				.defineList("dimension blacklist", ConfigCore.PROTECTED_DIMENSION_BLACKLIST, (o) -> o instanceof String);
		UNOWNED_WHITELIST = builder.comment("blocks that are excluded from protections in unowned land")
				.defineList("unowned whitelist", ConfigCore.UNOWNED_WHITELIST, (o) -> o instanceof String);
		
		builder.pop();
	}	
	
	private static void setupBlockTrade(ForgeConfigSpec.Builder builder) {
		builder.comment("Land configuration settings").push("trade");
		
		MARKET_GLOBAL_TAX_BUY = builder.comment("The percentage of the posting price added on when being purchased on the global market.")
				.defineInRange("market_global_tax_buy", ConfigCore.MARKET_GLOBAL_TAX_BUY, 0, 1D);
		MARKET_GLOBAL_TAX_SELL = builder.comment("The percentage of the posting price charged when being added to the global market.")
				.defineInRange("market_global_tax_sell", ConfigCore.MARKET_GLOBAL_TAX_SELL, 0, 1D);
		MARKET_AUCTION_TAX_SELL = builder.comment("the fee, as a percent of the price, applied when creating a new auction.")
				.defineInRange("market_auction_tax_sell", ConfigCore.MARKET_AUCTION_TAX_SELL, 0, 1D);		
		AUCTION_OPEN_DURATION = builder.comment("The length of time, in milliseconds, an auction remains open for bidding.")
				.defineInRange("auction_open_duration", ConfigCore.AUCTION_OPEN_DURATION, 0, Long.MAX_VALUE);
		PAGE_SIZE = builder.comment("The default number of items displayed in market pages.  this impacts DB performance")
				.defineInRange("page_size", ConfigCore.PAGE_SIZE, 1, Integer.MAX_VALUE);
		
		builder.pop();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {System.out.println("Config Loaded");}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {System.out.println("Config Reloaded");}
}
