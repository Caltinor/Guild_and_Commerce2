package dicemc.gnc.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
	
	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	
	public static final String CATEGORY_SERVER = "server";
	public static final String CATEGORY_CLIENT = "client";
	
	public static final String SUB_CATEGORY_STORAGE = "Storage Scheme";
	public static final String SUB_CATEGORY_DB = "database";
	public static final String SUB_CATEGORY_MISC = "Misc Variables";
	
	//Data Storage Scheme Variables
	public static ForgeConfigSpec.ConfigValue<Boolean> WORLD_USE_DB;
	public static ForgeConfigSpec.ConfigValue<Boolean> MARKET_USE_DB;
	//Primary Database config values
	public static ForgeConfigSpec.ConfigValue<String> DB_PORT;
	public static ForgeConfigSpec.ConfigValue<String> DB_NAME;
	public static ForgeConfigSpec.ConfigValue<String> DB_URL;
	public static ForgeConfigSpec.ConfigValue<String> DB_USER;
	public static ForgeConfigSpec.ConfigValue<String> DB_PASS;
	public static ForgeConfigSpec.ConfigValue<String> DB_SUFFIX;
	//Alternate Database config values
	public static ForgeConfigSpec.ConfigValue<String> DB2_PORT;
	public static ForgeConfigSpec.ConfigValue<String> DB2_NAME;
	public static ForgeConfigSpec.ConfigValue<String> DB2_URL;
	public static ForgeConfigSpec.ConfigValue<String> DB2_USER;
	public static ForgeConfigSpec.ConfigValue<String> DB2_PASS;
	public static ForgeConfigSpec.ConfigValue<String> DB2_SUFFIX;
	//Miscellaneous configurable variables
	public static ForgeConfigSpec.ConfigValue<Double> DEFAULT_LAND_PRICE;
	public static ForgeConfigSpec.ConfigValue<Long> TEMPCLAIM_DURATION;
	public static ForgeConfigSpec.ConfigValue<Integer> CHUNKS_PER_MEMBER;
	public static ForgeConfigSpec.ConfigValue<Double> GLOBAL_TAX_RATE;
	public static ForgeConfigSpec.ConfigValue<Long> GLOBAL_TAX_INTERVAL;
	public static ForgeConfigSpec.ConfigValue<Double> TEMPCLAIM_RATE;
	public static ForgeConfigSpec.ConfigValue<Double> LAND_ABANDON_REFUND_RATE;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> OUTPOST_CREATE_COST;
	public static ForgeConfigSpec.ConfigValue<Double> STARTING_FUNDS;
	public static ForgeConfigSpec.ConfigValue<Double> GUILD_STARTING_FUNDS;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_BUY;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_GLOBAL_TAX_SELL;
	public static ForgeConfigSpec.ConfigValue<Double> MARKET_AUCTION_TAX_SELL;
	public static ForgeConfigSpec.ConfigValue<Boolean> UNOWNED_PROTECTED;
	public static ForgeConfigSpec.ConfigValue<Long> AUCTION_OPEN_DURATION;
	
	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
		
		CLIENT_BUILDER.comment("Client Settings").push(CATEGORY_CLIENT);
		CLIENT_BUILDER.pop();
		
		SERVER_BUILDER.comment("Server Settings").push(CATEGORY_SERVER);
		setupBlockStorageScheme(SERVER_BUILDER);
		setupBlockConfig_DB(SERVER_BUILDER);
		setupBlockMisc(SERVER_BUILDER);
		SERVER_BUILDER.pop();
		
		CLIENT_CONFIG = CLIENT_BUILDER.build();
		SERVER_CONFIG = SERVER_BUILDER.build();
	}
	
	private static void setupBlockConfig_DB(ForgeConfigSpec.Builder builder) {
		builder.comment("Database Settings").push(SUB_CATEGORY_DB);
		
		//Primary Database location
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
		//Secondary (Market/Account) database 
		DB2_PORT = builder.comment("Alternate Database port")
				.define("port", "3306");
		DB2_NAME = builder.comment("Alternate Database name")
				.define("name", "new_Schema");
		DB2_URL = builder.comment("Alternate Database URL")
				.define("url", "localhost");
		DB2_USER = builder.comment("Alternate Database Username")
				.define("user", "user");
		DB2_PASS = builder.comment("Alternate Database Password")
				.define("pass", "pass");
		DB2_SUFFIX = builder.comment("Alternate Database Table Suffix.  Used to create separate tables for separate worlds within the same DB")
				.define("suffix", "world");
		
		builder.pop();		
	}
	
	private static void setupBlockStorageScheme(ForgeConfigSpec.Builder builder) {
		builder.comment("Server Storage Scheme").push(SUB_CATEGORY_STORAGE);
		
		WORLD_USE_DB = builder.comment("Determines if the server will use the primary database settings for world data or store internally")
				.define("world_use_DB", false);
		MARKET_USE_DB = builder.comment("Determines if the server will use the secondary database settings for Market data or store internally")
				.define("market_use_DB", false);
		
		builder.pop();
	}
	
	private static void setupBlockMisc(ForgeConfigSpec.Builder builder) {
		builder.comment("Miscellaneous configuration settings").push(SUB_CATEGORY_MISC);
		//TODO populate with misc variables
		builder.pop();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {System.out.println("Config Loaded");}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {System.out.println("Config Reloaded");}
}
