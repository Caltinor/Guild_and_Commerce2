package dicemc.gnc.database;

import java.util.HashMap;
import java.util.Map;

public class TableDefinitions {
	public Map<String, String> tableDef = new HashMap<String, String>();
	
	public TableDefinitions(String suffix) {
		map_Guild = define_Guild(suffix);
		map_PermLevels = define_PermLevels(suffix);
		//construct Table Reference 
		tableDef = defineTables(suffix);
	}
	
	private Map<String, String> defineTables(String suffix) {
		Map<String, String> map = new HashMap<String, String>();
		String sqlSTR = "";
		String tblSTR = "";
		//Guild Table
		tblSTR = map_Guild.get(tblGuild.TABLE_NAME)+ suffix;
		sqlSTR = " ("+ map_Guild.get(tblGuild.GUILD_ID)+"INT NOT NULL AUTO_INCREMENT, " + 
				map_Guild.get(tblGuild.NAME) +"VARCHAR(32) NOT NULL, " + 
				map_Guild.get(tblGuild.OPEN) +"TINYINT(1) NOT NULL, " + 
				map_Guild.get(tblGuild.TAX) +"DOUBLE NOT NULL, " + 
				"PRIMARY KEY ("+ map_Guild.get(tblGuild.GUILD_ID) +"))";
		map.put(tblSTR, sqlSTR);
		//Other table
		//repeat
		//
		return map;
	}
	
	public static enum tblGuild {TABLE_NAME, GUILD_ID, NAME, OPEN, TAX}
	public static Map<tblGuild, String> map_Guild;
	private static Map<tblGuild, String> define_Guild(String suffix) {
		Map<tblGuild, String> map = new HashMap<tblGuild, String>();
		map.put(tblGuild.TABLE_NAME, "tbl_guild_");
		map.put(tblGuild.GUILD_ID, "ID");
		map.put(tblGuild.NAME, "Name");
		map.put(tblGuild.OPEN, "Open");
		map.put(tblGuild.TAX, "Tax");
		return map;
	}
	
	public static enum tblPermLevels {TABLE_NAME, ID, GUILD_ID, RANK, TITLE}
	public static Map<tblPermLevels, String> map_PermLevels;
	private static Map<tblPermLevels, String> define_PermLevels(String suffix) {
		Map<tblPermLevels, String> map = new HashMap<tblPermLevels, String>();
		map.put(tblPermLevels.TABLE_NAME, "tbl_permlevels_");
		map.put(tblPermLevels.ID, "ID");
		map.put(tblPermLevels.GUILD_ID, "Guild_ID");
		map.put(tblPermLevels.RANK, "Rank");
		map.put(tblPermLevels.TITLE, "Title");
		return map;
	}
	
	public static enum tblPermissions {TABLE_NAME, ID, GUILD_ID, PERM_TAG, VALUE}
	
	
	public static enum tblMembers {TABLE_NAME, ID, GUILD_ID, UUID, RANK}
	
	
	public static enum tblAccounts {TABLE_NAME, ID, OWNER, BALANCE}
	
	
	public static enum tblChunk {TABLE_NAME, CHUNK_X, CHUNK_Y, OWNER, TEMP_RENTER, PRICE,
		PRICE_LEASE, LEASE_DURATION, PERM_MIN, RENT_END, IS_PUBLIC, IS_FOR_SALE, IS_OUTPOST, 
		CAN_EXPLOSIONS}
	
	
	public static enum tblWhitelist {TABLE_NAME, ID, CHUNK_POS, BLOCK, ENTITY, CAN_BREAK, CAN_INTERACT}
	
	
	public static enum tblPermittedPlayers {TABLE_NAME, ID, UUID, PRINCIPAL}
	
	
	public static enum tblMarkets {TABLE_NAME, ID, MARKET_TYPE, ITEM, VENDOR_ID, LOCALITY, 
		BID_END, PRICE, VENDOR_GIVE_ITEM, INFINITE, STOCK, ACTIVE_TRANSACTION, BUYER_ID}
	
	
	public static enum tblBids {TABLE_NAME, ID, TRANSACTION_ID, BIDDER_ID, DTG_PLACED, PRICE}
	
	
	public static enum tblStorage {TABLE_NAME, ID, OWNER, ITEM, QUANTITY}
	
}
