package dicemc.gnc.setup;

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
	
	public static final String SUB_CATEGORY_STORAGE = "Storage Scheme";
	public static final String SUB_CATEGORY_DB = "database";
	public static final String SUB_CATEGORY_MISC = "Misc Variables";
	
	//Data Storage Scheme Variables
	public static ForgeConfigSpec.ConfigValue<Boolean> MARKET_USE_DB;
	//Primary Database config values
	public static ForgeConfigSpec.ConfigValue<String> DB_PORT;
	public static ForgeConfigSpec.ConfigValue<String> DB_NAME;
	public static ForgeConfigSpec.ConfigValue<String> DB_URL;
	public static ForgeConfigSpec.ConfigValue<String> DB_USER;
	public static ForgeConfigSpec.ConfigValue<String> DB_PASS;
	public static ForgeConfigSpec.ConfigValue<String> DB_SUFFIX;
	//Miscellaneous configurable variables
	public static ForgeConfigSpec.ConfigValue<Double> DEFAULT_LAND_PRICE;
	public static ForgeConfigSpec.ConfigValue<Long> TEMPCLAIM_DURATION;
	public static ForgeConfigSpec.ConfigValue<Integer> CHUNKS_PER_MEMBER;
	public static ForgeConfigSpec.ConfigValue<Double> GLOBAL_TAX_RATE;
	public static ForgeConfigSpec.ConfigValue<Long> GLOBAL_TAX_INTERVAL;
	public static ForgeConfigSpec.ConfigValue<Double> TEMPCLAIM_RATE;
	public static ForgeConfigSpec.ConfigValue<Double> LAND_ABANDON_REFUND_RATE;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_NAME_CHANGE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_RANK_ADD_COST;
	public static ForgeConfigSpec.ConfigValue<Double> OUTPOST_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> STARTING_FUNDS;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_STARTING_FUNDS;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_BUY;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_SELL;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_AUCTION_TAX_SELL;
	public static ForgeConfigSpec.ConfigValue<Boolean> UNOWNED_PROTECTED;
	public static ForgeConfigSpec.ConfigValue<Long> AUCTION_OPEN_DURATION;
	
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
		setupBlockMisc(SERVER_BUILDER);
		SERVER_BUILDER.pop();
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
		COMMON_CONFIG = COMMON_BUILDER.build();
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
	
	private static void setupBlockConfig_DB(ForgeConfigSpec.Builder builder) {
		builder.comment("Database Settings").push(SUB_CATEGORY_DB);
		
		MARKET_USE_DB = builder.comment("Determines if the server will use the secondary database settings for Market data or store internally")
				.define("market_use_DB", false);
		//Database location
		DB_PORT = builder.comment("Database port")
				.define("port", "3306");
		DB_NAME = builder.comment("Database name")
				.define("name", "new_Schema");
		DB_URL = builder.comment("Database URL")
				.define("url", "localhost");
		DB_USER = builder.comment("Database Username")
				.define("user", "user");
		DB_PASS = builder.comment("Database Password")
				.define("pass", "pass");
		DB_SUFFIX = builder.comment("Database Table Suffix.  Used to create separate tables for separate worlds within the same DB")
				.define("suffix", "world");
		
		builder.pop();		
	}
	
	private static void setupBlockMisc(ForgeConfigSpec.Builder builder) {
		builder.comment("Miscellaneous configuration settings").push(SUB_CATEGORY_MISC);

		DEFAULT_LAND_PRICE = builder.comment("The price of land when generating for the first time.")
				.defineInRange("default_land_price", 100D, 0, Integer.MAX_VALUE);
		TEMPCLAIM_DURATION = builder.comment("The length of time (in miliseconds) a temp claim is protected for.")
				.defineInRange("tempclaim_duration", 43200000L, 0, Long.MAX_VALUE);
		CHUNKS_PER_MEMBER = builder.comment("The number of chunks claimable per member that is untaxed.)")
				.defineInRange("chunks_per_member", 9, 0, Integer.MAX_VALUE);
		GLOBAL_TAX_RATE = builder.comment("The global tax applied to all guilds when triggered.")
				.defineInRange("global_tax_rate", 0.1, 0, 1D);
		GLOBAL_TAX_INTERVAL = builder.comment("The interval in miliseconds between guild taxation.")
				.defineInRange("global_tax_interval", 864000L, 0, Long.MAX_VALUE);
		TEMPCLAIM_RATE = builder.comment("The percentage of the land's cost that must be paid to temporarily claim land.")
				.defineInRange("tempclaim_rate", 0.1, 0, 1D);
		LAND_ABANDON_REFUND_RATE = builder.comment("The percentage refunded to the guild when abandoning claims.")
				.defineInRange("land_abandon_refund_rate", 0.75, 0, 1D);
		GUILD_CREATE_COST = builder.comment("The cost to create a new guild.")
				.defineInRange("guild_create_cost", 2500D, 0, Double.MAX_VALUE);
		GUILD_NAME_CHANGE_COST = builder.comment("The cost to change a guild's name.")
				.defineInRange("guild_name_change_cost", 1500D, 0, Double.MAX_VALUE);
		GUILD_RANK_ADD_COST = builder.comment("The cost modifier to add a new guild rank.")
				.defineInRange("guild_rank_add_cost", 1000D, 0, Double.MAX_VALUE);
		OUTPOST_CREATE_COST = builder.comment("The cost to establish a new outpost.")
				.defineInRange("outpost_create_cost", 2000D, 0, Double.MAX_VALUE);
		STARTING_FUNDS = builder.comment("The amount of money a new player starts with when first joining the world.")
				.defineInRange("starting_funds", 1000D, 0, Double.MAX_VALUE);
		GUILD_STARTING_FUNDS = builder.comment("An amount of money given to guilds after creation.  This serves as a rebate to guild creation.")
				.defineInRange("guild_starting_funds", 0D, 0, Double.MAX_VALUE);
		MARKET_GLOBAL_TAX_BUY = builder.comment("The percentage of the posting price added on when being purchased on the global market.")
				.defineInRange("market_global_tax_buy", 0.1D, 0, 1D);
		MARKET_GLOBAL_TAX_SELL = builder.comment("The percentage of the posting price charged when being added to the global market.")
				.defineInRange("market_global_tax_sell", 0.1D, 0, 1D);
		MARKET_AUCTION_TAX_SELL = builder.comment("the fee, as a percent of the price, applied when creating a new auction.")
				.defineInRange("market_auction_tax_sell", 0.3, 0, 1D);
		UNOWNED_PROTECTED = builder.comment("Determines if unowned land is protected. setting to false will make only land that is purchased protected.")
				.define("unowned_protected", true);
		AUCTION_OPEN_DURATION = builder.comment("The length of time, in milliseconds, an auction remains open for bidding.")
				.defineInRange("auction_open_duration", 259200000L, 0, Long.MAX_VALUE);
		
		builder.pop();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {System.out.println("Config Loaded");}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {System.out.println("Config Reloaded");}
}
