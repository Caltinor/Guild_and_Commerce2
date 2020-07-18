package dicemc.gnc.database;

import java.util.HashMap;
import java.util.Map;

public class TableDefinitions {
	public Map<String, String> tableDef = new HashMap<String, String>();
	
	public TableDefinitions(String suffix) {
		tableDef = defineTables(suffix);
	}
	
	private Map<String, String> defineTables(String suffix) {
		Map<String, String> map = new HashMap<String, String>();
		String sqlSTR = "";
		String tblSTR = "";
		//Guild Table
		tblSTR = map_Guild.get(tblGuild.TABLE_NAME) + suffix;
		sqlSTR = " ("+ map_Guild.get(tblGuild.GUILD_ID)+"INT NOT NULL AUTO_INCREMENT, " + 
				map_Guild.get(tblGuild.NAME) +"VARCHAR(32) NOT NULL, " + 
				map_Guild.get(tblGuild.OPEN) +"TINYINT(1) NOT NULL, " + 
				map_Guild.get(tblGuild.TAX) +"DOUBLE NOT NULL, " + 
				"PRIMARY KEY ("+ map_Guild.get(tblGuild.GUILD_ID) +"))";
		map.put(tblSTR, sqlSTR);
		//Other table
		//repeat
		//TODO: Build Table Creation Definitions
		return map;
	}
	
	public static enum tblGuild {TABLE_NAME, GUILD_ID, NAME, OPEN, TAX}
	public static final Map<tblGuild, String> map_Guild = define_Guild();
	private static Map<tblGuild, String> define_Guild() {
		Map<tblGuild, String> map = new HashMap<tblGuild, String>();
		map.put(tblGuild.TABLE_NAME, "tbl_guild_");
		map.put(tblGuild.GUILD_ID, "ID");
		map.put(tblGuild.NAME, "Name");
		map.put(tblGuild.OPEN, "Open");
		map.put(tblGuild.TAX, "Tax");
		return map;
	}
	
	public static enum tblPermLevels {TABLE_NAME, ID, GUILD_ID, RANK, TITLE}
	public static final Map<tblPermLevels, String> map_PermLevels = define_PermLevels();
	private static Map<tblPermLevels, String> define_PermLevels() {
		Map<tblPermLevels, String> map = new HashMap<tblPermLevels, String>();
		map.put(tblPermLevels.TABLE_NAME, "tbl_permlevels_"+ suffix);
		map.put(tblPermLevels.ID, "ID");
		map.put(tblPermLevels.GUILD_ID, "Guild_ID");
		map.put(tblPermLevels.RANK, "Rank");
		map.put(tblPermLevels.TITLE, "Title");
		return map;
	}
	
	public static enum tblPermissions {TABLE_NAME, ID, GUILD_ID, PERM_TAG, VALUE}
	public static final Map<tblPermissions, String> map_Permissions = define_Permissions();
	private static Map<tblPermissions, String> define_Permissions() {
		Map<tblPermissions, String> map = new HashMap<tblPermissions, String>();
		map.put(tblPermissions.TABLE_NAME, "tbl_permissions_" + suffix);
		map.put(tblPermissions.ID, "ID");
		map.put(tblPermissions.GUILD_ID, "Guild_ID");
		map.put(tblPermissions.PERM_TAG, "Perm_Tag");
		map.put(tblPermissions.VALUE, "Value");
		return map;
	}
	
	public static enum tblMembers {TABLE_NAME, ID, GUILD_ID, UUID, RANK}
	public static final Map<tblMembers, String> map_Members = define_Members();
	private static Map<tblMembers, String> define_Members() {
		Map<tblMembers, String> map = new HashMap<tblMembers, String>();
		map.put(tblMembers.TABLE_NAME, "tbl_members_" +suffix);
		map.put(tblMembers.ID, "ID");
		map.put(tblMembers.GUILD_ID, "Guild_ID");
		map.put(tblMembers.UUID, "UUID");
		map.put(tblMembers.RANK, "Rank");
		return map;
	}
	
	public static enum tblAccounts {TABLE_NAME, ID, OWNER, BALANCE}
	public static final Map<tblAccounts, String) map_Accounts = define_Accounts();
	private static Map<tblAccounts, String) define_Accounts() {
		Map<tblAccounts, String> map = new HashMap<tblAccounts, String>();
		map.put(tblAccounts.TABLE_NAME, "tbl_accounts_" +suffix);
		map.put(tblAccounts.ID, "ID");
		map.put(tblAccounts.OWNER, "Owner");
		map.put(tblAccounts.BALANCE, "Balance");
		return map;
	}
	
	public static enum tblChunk {TABLE_NAME, CHUNK_X, CHUNK_Z, OWNER, TEMP_RENTER, PRICE,
		PRICE_LEASE, LEASE_DURATION, PERM_MIN, RENT_END, IS_PUBLIC, IS_FOR_SALE, IS_OUTPOST, 
		CAN_EXPLOSIONS}
	public static final Map<tblChunk, String> map_Chunk = define_Chunk();
	private static Map<tblChunk, String> define_Chunk() {
		Map<tblChunk, String> map = new HashMap<tblChunk, String>();
		map.put(tblChunk.TABLE_NAME, "tbl_chunk_" + suffix);
		map.put(tblChunk.CHUNK_X, "chunk_X");
		map.put(tblChunk.CHUNK_Z, "chunk_Z");
		map.put(tblChunk.OWNER, "Owner");
		map.put(tblChunk.TEMP_RENTER, "temp_plyr");
		map.put(tblChunk.PRICE, "Price");
		map.put(tblChunk.PRICE_LEASE, "lease_price");
		map.put(tblChunk.LEASE_DURATION, "lease_duration");
		map.put(tblChunk.PERM_MIN, "perm_min");
		map.put(tblChunk.RENT_END, "rent_end");
		map.put(tblChunk.IS_PUBLIC, "public");
		map.put(tblChunk.IS_FOR_SALE, "for_sale");
		map.put(tblChunk.IS_OUTPOST, "outpost");
		map.put(tblChunk.CAN_EXPLOSIONS, "explosions");
		return map;
	}
	
	public static enum tblWhitelist {TABLE_NAME, ID, CHUNK_POS, BLOCK, ENTITY, CAN_BREAK, CAN_INTERACT}
	public static final Map<tblWhitelist, String> map_Whitelist = define_Whitelist();
	private static Map<tblWhitelist, String> define_Whitelist() {
		Map<tblWhitelist, String> map = HashMap<tblWhitelist, String>();
		map.put(tblWhitelist.TABLE_NAME, "tbl_whitelist_" + suffix);
		map.put(tblWhitelist.ID, "ID");
		map.put(tblWhitelist.CHUNK_POS, "pos");
		map.put(tblWhitelist.BLOCK, "block");
		map.put(tblWhitelist.ENTITY, "entity");
		map.put(tblWhitelist.CAN_BREAK, "break");
		map.put(tblWhitelist.CAN_INTERACT, "interact");
		return map;
	}
	
	public static enum tblPermittedPlayers {TABLE_NAME, ID, UUID, PRINCIPAL}
	public static final Map<tblPermittedPlayers, String> map_PermittedPlayers = define_PermittedPlayers();
	private static Map<tblPermittedPlayers, String> define_PermittedPlayers() {
		Map<tblPermittedPlayers, String> map = new HashMap<tblPermittedPlayers, String>();
		map.put(tblPermittedPlayers.TABLE_NAME, "tbl_permitted_players_" + suffix);
		map.put(tblPermittedPlayers.ID, "ID");
		map.put(tblPermittedPlayers.UUID, "UUID");
		map.put(tblPermittedPlayers.PRINCIPAL, "principal");
		return map;
	}
	
	//INFINITE replaced with using negative values for stock
	public static enum tblMarkets {TABLE_NAME, ID, MARKET_TYPE, ITEM, VENDOR_ID, LOCALITY, 
		BID_END, PRICE, VENDOR_GIVE_ITEM, STOCK, ACTIVE_TRANSACTION, BUYER_ID}
	public static final Map<tblMarkets, String> map_Markets = define_Markets();
	private static Map<tblMarkets, String> define_Markets() {
		Map<tblMarkets, String> map = new HashMap<tblMarkets, String>();
		map.put(tblMarkets.TABLE_NAME, "tbl_markets_" + suffix);
		map.put(tblMarkets.ID, "ID");
		map.put(tblMarkets.MARKET_TYPE, "type");
		map.put(tblMarkets.ITEM, "Item");
		map.put(tblMarkets.VENDOR_ID, "Vendor");
		map.put(tblMarkets.LOCALITY, "locality");
		map.put(tblMarkets.BID_END, "bid_end");
		map.put(tblMarkets.PRICE, "Price");
		map.put(tblMarkets.VENDOR_GIVE_ITEM, "GiveItem");
		map.put(tblMarkets.STOCK, "Stock");
		map.put(tblMarkets.ACTIVE_TRANSACTION, "active");
		map.put(tblMarkets.BUYER_ID, "buyer");
		map.put(tblMarkets.
		return map;
	}
	
	public static enum tblBids {TABLE_NAME, ID, TRANSACTION_ID, BIDDER_ID, DTG_PLACED, PRICE}
	public static final Map<tblBids, String> map_Bids = define_Bids();
	private static Map<tblBids, String> define_Bids() {
		Map<tblBids, String> map = new HashMap<tblBids, String>();
		map.put(tblBids.TABLE_NAME, "tbl_bids_" + suffix);
		map.put(tblBids.ID, "ID");
		map.put(tblBids.TRANSACTION_ID, "trans_ID");
		map.put(tblBids.BIDDER_ID, "bidder");
		map.put(tblBids.DTG_PLACED, "DTG");
		map.put(tblBids.PRICE, "Price");
		return map;
	}
	
	public static enum tblStorage {TABLE_NAME, ID, OWNER, ITEM, QUANTITY}
	public static final Map<tblStorage, String> map_Storage = define_Storage();
	private static Map<tblStorage, String> define_Storage() {
		Map<tblStorage, String> map = new HashMap<tblStorage, String>();
		map.put(tblStorage.TABLE_NAME, "tbl_storage_" + suffix);
		map.put(tblStorage.ID, "ID");
		map.put(tblStorage.OWNER, "Owner");
		map.put(tblStorage.ITEM, "Item");
		map.put(tblStorage.QUANTITY, "Count");
		return map;
	}
}
