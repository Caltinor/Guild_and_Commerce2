package dicemc.gnc.datastorage.database;

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
		//Account Table
		tblSTR = map_Accounts.get(tblAccounts.TABLE_NAME) + suffix;
		sqlSTR = " ("+ map_Accounts.get(tblAccounts.ID)	+" INT NOT NULL AUTO_INCREMENT, " + 
				map_Accounts.get(tblAccounts.OWNER) 	+" VARCHAR(36) NOT NULL, " + 
				map_Accounts.get(tblAccounts.BALANCE) 	+" DOUBLE NOT NULL, " + 
				"PRIMARY KEY ("+ map_Accounts.get(tblAccounts.ID) +"))";
		map.put(tblSTR, sqlSTR);
		//Market Table
		tblSTR = map_Markets.get(tblMarkets.TABLE_NAME) + suffix;
		sqlSTR = " ("+ map_Markets.get(tblMarkets.ID) 			+" INT NOT NULL AUTO_INCREMENT, " +
				map_Markets.get(tblMarkets.MARKET_TYPE) 		+" INT NOT NULL, " +
				map_Markets.get(tblMarkets.ITEM) 				+" LONGTEXT NOT NULL, " +
				map_Markets.get(tblMarkets.VENDOR_ID)			+" VARCHAR(36) NOT NULL, " +
				map_Markets.get(tblMarkets.LOCALITY)			+" VARCHAR(36) NOT NULL, " +
				map_Markets.get(tblMarkets.BID_END)				+" MEDIUMTEXT NOT NULL, " +
				map_Markets.get(tblMarkets.PRICE)				+" DOUBLE NOT NULL, " +
				map_Markets.get(tblMarkets.VENDOR_GIVE_ITEM)	+" TINYINT(1) NOT NULL, " +
				map_Markets.get(tblMarkets.STOCK)				+" INT NOT NULL, " +
				map_Markets.get(tblMarkets.ACTIVE_TRANSACTION)	+" TINYINT(1) NOT NULL, " +
				map_Markets.get(tblMarkets.BUYER_ID)			+" VARCHAR(36) NOT NULL, " +
				map_Markets.get(tblMarkets.DTG_PLACED)			+" MEDIUMTEXT NOT NULL, " +
				map_Markets.get(tblMarkets.DTG_BOUGHT)			+" MEDIUMTEXT NOT NULL, " +
				"PRIMARY KEY (" + map_Markets.get(tblMarkets.ID) + "))";
		map.put(tblSTR, sqlSTR);
		//Bids Table
		tblSTR = map_Bids.get(tblBids.TABLE_NAME) + suffix;
		sqlSTR = " ("+ map_Bids.get(tblBids.ID) 		+" INT NOT NULL AUTO_INCREMENT, " +
				map_Bids.get(tblBids.TRANSACTION_ID) 	+" INT NOT NULL, " +
				map_Bids.get(tblBids.BIDDER_ID) 		+" VARCHAR(36) NOT NULL, " +
				map_Bids.get(tblBids.DTG_PLACED) 		+" MEDIUMTEXT NOT NULL, " +
				map_Bids.get(tblBids.PRICE)				+" DOUBLE NOT NULL, " +
				"PRIMARY KEY (" + map_Bids.get(tblBids.ID) + "))";
		map.put(tblSTR, sqlSTR);
		//Storage Table
		tblSTR = map_Storage.get(tblStorage.TABLE_NAME) + suffix;
		sqlSTR = " ("+ map_Storage.get(tblStorage.ID) 	+" INT NOT NULL AUTO_INCREMENT, " +
				map_Storage.get(tblStorage.OWNER) 		+" VARCHAR(36) NOT NULL, " +
				map_Storage.get(tblStorage.ITEM) 		+" MEDIUMTEXT NOT NULL, " +
				map_Storage.get(tblStorage.QUANTITY) 	+" INT NOT NULL, " +
				"PRIMARY KEY (" + map_Storage.get(tblStorage.ID) + "))";
		map.put(tblSTR, sqlSTR);
		return map;
	}
	
	public static enum tblAccounts {TABLE_NAME, ID, OWNER, BALANCE}
	public static final Map<tblAccounts, String> map_Accounts = define_Accounts();
	private static Map<tblAccounts, String> define_Accounts() {
		Map<tblAccounts, String> map = new HashMap<tblAccounts, String>();
		map.put(tblAccounts.TABLE_NAME, "tbl_accounts_");
		map.put(tblAccounts.ID, "ID");
		map.put(tblAccounts.OWNER, "Owner");
		map.put(tblAccounts.BALANCE, "Balance");
		return map;
	}
	
	//INFINITE replaced with using negative values for stock
	public static enum tblMarkets {TABLE_NAME, ID, MARKET_TYPE, ITEM, VENDOR_ID, LOCALITY, 
		BID_END, PRICE, VENDOR_GIVE_ITEM, STOCK, ACTIVE_TRANSACTION, BUYER_ID, DTG_PLACED, DTG_BOUGHT}
	public static final Map<tblMarkets, String> map_Markets = define_Markets();
	private static Map<tblMarkets, String> define_Markets() {
		Map<tblMarkets, String> map = new HashMap<tblMarkets, String>();
		map.put(tblMarkets.TABLE_NAME, "tbl_markets_");
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
		map.put(tblMarkets.DTG_PLACED, "dtg_placed");
		map.put(tblMarkets.DTG_BOUGHT, "dtg_bought");
		return map;
	}
	
	public static enum tblBids {TABLE_NAME, ID, TRANSACTION_ID, BIDDER_ID, DTG_PLACED, PRICE}
	public static final Map<tblBids, String> map_Bids = define_Bids();
	private static Map<tblBids, String> define_Bids() {
		Map<tblBids, String> map = new HashMap<tblBids, String>();
		map.put(tblBids.TABLE_NAME, "tbl_bids_");
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
		map.put(tblStorage.TABLE_NAME, "tbl_storage_");
		map.put(tblStorage.ID, "ID");
		map.put(tblStorage.OWNER, "Owner");
		map.put(tblStorage.ITEM, "Item");
		map.put(tblStorage.QUANTITY, "Count");
		return map;
	}
}
