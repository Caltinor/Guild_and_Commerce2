package dicemc.gnc.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MarketUtils {
	
	public String createTransaction(MarketItem item) {return "";}
	
	public String closeTransaction(int ID) {return "";}
	
	public String purchaseItem(int ID, UUID buyer) {return "";}
	
	public String placeBid(int ID, UUID bidder, double value) {return "";}
	
	public String addToStorage(ItemStack item, UUID player) {return "";}
	
	public String removeFromStorage(ItemStack item, int quantity, UUID player) {return "";}
	
	public String IncreaseTransactionSupply(int transaction_ID, int count) {return "";}
	
	public enum SortType {PRICE_ASC, PRICE_DESC, NAMES_ASC, NAMES_DESC, ITEM_SPECIFIC, EXCLUDE_SELF_POSTED, ENCHANT}
	public List<MarketItem> getMarket(int Market_Type, int page, List<SortType> sort, @Nullable Item sortItem, @Nullable String sortEnchant) {
		List<MarketItem> list = new ArrayList<MarketItem>();
		return list;
	}
	
	public Map<ItemStack, Integer> getStorage(UUID player, List<SortType> sort, @Nullable Item sortItem, @Nullable String sortEnchant) {
		Map<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();
		return map;
	}
	
	public List<String> getTransactionHistory(UUID player, int page) {
		List<String> list = new ArrayList<String>();
		return list;
	}
	
	public List<String> getBidHistory(int TransactionID, int page) {
		List<String> list = new ArrayList<String>();
		return list;	
	}
}
