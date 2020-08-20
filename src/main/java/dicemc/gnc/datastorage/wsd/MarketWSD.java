package dicemc.gnc.datastorage.wsd;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.market.BidEntry;
import dicemc.gnc.market.MarketItem;
import dicemc.gnc.market.StorageItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class MarketWSD extends WorldSavedData{
	private static final String DATA_NAME = GnC.MOD_ID + "_Commerce";
	
	private Map<Integer, MarketItem> MARKETS = new HashMap<Integer, MarketItem>();
	private Map<UUID, Double> ACCOUNTS = new HashMap<UUID, Double>();
	private Map<Integer, StorageItem> STORAGE = new HashMap<Integer, StorageItem>();
	private Map<Integer, BidEntry> BIDS = new HashMap<Integer, BidEntry>();
	
	public Map<Integer, MarketItem> getMarket() {return MARKETS;}
	public Map<UUID, Double> getAccounts() {return ACCOUNTS;}
	public Map<Integer, StorageItem> getStorage() {return STORAGE;}
	public Map<Integer, BidEntry> getBids() {return BIDS;}
	
	public MarketWSD() {super(DATA_NAME);}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT list = nbt.getList("accounts", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {ACCOUNTS.put(list.getCompound(i).getUniqueId("ID"), list.getCompound(i).getDouble("balance"));}
		list = nbt.getList("markets", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {MARKETS.put(list.getCompound(i).getInt("ID"), new MarketItem(list.getCompound(i).getCompound("item")));}
		list = nbt.getList("storage", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {STORAGE.put(list.getCompound(i).getInt("ID"), new StorageItem(list.getCompound(i).getCompound("item")));}
		list = nbt.getList("bids", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {BIDS.put(list.getCompound(i).getInt("ID"), new BidEntry(list.getCompound(i).getCompound("item")));}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (Map.Entry<UUID, Double> entry : ACCOUNTS.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putUniqueId("ID", entry.getKey());
			snbt.putDouble("balance", entry.getValue());
			list.add(snbt);
		}
		compound.put("accounts", list);
		list = new ListNBT();
		for (Map.Entry<Integer, MarketItem> entry : MARKETS.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("ID", entry.getKey());
			snbt.put("item", entry.getValue().toNBT());
			list.add(snbt);
		}
		compound.put("markets", list);
		list = new ListNBT();
		for (Map.Entry<Integer, StorageItem> entry : STORAGE.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("ID", entry.getKey());
			snbt.put("item", entry.getValue().toNBT());
			list.add(snbt);
		}
		compound.put("storage", list);
		list = new ListNBT();
		for (Map.Entry<Integer, BidEntry> entry : BIDS.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("ID", entry.getKey());
			snbt.put("item", entry.getValue().toNBT());
			list.add(snbt);
		}
		compound.put("bids", list);
		return compound;
	}

	public static MarketWSD get(ServerWorld world) {
		return world.getSavedData().getOrCreate(MarketWSD::new, DATA_NAME);
	}
}
