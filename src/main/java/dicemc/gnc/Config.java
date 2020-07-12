package dicemc.gnc;

import net.minecraftforge.common.ForgeConfigSpec;

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
		CLIENT_BUILDER.pop();
		
		SERVER_BUILDER.comment("Server Settings").push(CATEGORY_SERVER);
		setupBlockConfig_DB(SERVER_BUILDER);
		SERVER_BUILDER.pop();
	}
	
	private static void setupBlockConfig_DB(ForgeConfigSpec.Builder builder) {
		builder.comment("Database Settings").push(SUB_CATEGORY_DB);
		
		DB_PORT = builder.comment("Database port")
				.define("port", "3306");
		DB_NAME = builder.comment("Database name")
				.define("name", "mySchema");
		DB_URL = builder.comment("Database URL")
				.define("url", "localhost");
		DB_USER = builder.comment("Database Username")
				.define("user", "root");
		DB_PASS = builder.comment("Database Password")
				.define("pass", "root");
		
		builder.pop();		
	}
}
