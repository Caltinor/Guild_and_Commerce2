package dicemc.gnc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
	
	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	
	public static final String CATEGORY_SERVER = "server";
	public static final String CATEGORY_CLIENT = "client";
	
	public static final String SUB_CATEGORY_DB = "database";
	
	public static ForgeConfigSpec.ConfigValue<String> DB_PORT;
	public static ForgeConfigSpec.ConfigValue<String> DB_NAME;
	public static ForgeConfigSpec.ConfigValue<String> DB_URL;
	public static ForgeConfigSpec.ConfigValue<String> DB_USER;
	public static ForgeConfigSpec.ConfigValue<String> DB_PASS;
	
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
		
		builder.pop();		
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {}
	
	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {}
}
