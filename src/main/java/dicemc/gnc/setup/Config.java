package dicemc.gnc.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
	
	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	
	public static final String CATEGORY_SERVER = "server";
	public static final String CATEGORY_CLIENT = "client";
	
	public static final String SUB_CATEGORY_DB = "database";
	
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
	
	static {
		ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
		
		CLIENT_BUILDER.comment("Client Settings").push(CATEGORY_CLIENT);
		setupBlockConfig_DB(CLIENT_BUILDER);
		CLIENT_BUILDER.pop();
		
		SERVER_BUILDER.comment("Server Settings").push(CATEGORY_SERVER);
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
				.define("user", "gncLink");
		DB_PASS = builder.comment("Database Password")
				.define("pass", "gncAdmin1!");
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
				.define("user", "gncLink");
		DB2_PASS = builder.comment("Alternate Database Password")
				.define("pass", "gncAdmin1!");
		DB2_SUFFIX = builder.comment("Alternate Database Table Suffix.  Used to create separate tables for separate worlds within the same DB")
				.define("suffix", "world");
		
		builder.pop();		
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {}
}
