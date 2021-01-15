package dicemc.gnc.market;

import java.util.UUID;

import dicemc.gnc.GnC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class MarketItem {
	public int id = -1;
	public int marketType;
	public ItemStack item;
	public UUID vendor;
	public UUID locality;
	public long bidEnd;
	public double price;
	public boolean giveItem;
	public int stock = 1;
	public boolean active = true;
	public UUID buyer = GnC.NIL;
	public long dtgPlaced;
	public long dtgBought = 0;
	
	public MarketItem(int type, ItemStack item, UUID vendor, UUID locality,long bidEnd, double price, boolean giveItem) {
		this.marketType = type;
		this.item = item;
		this.vendor = vendor;
		this.locality = locality;
		this.bidEnd = bidEnd;
		this.price = price;
		this.giveItem = giveItem;
		dtgPlaced = System.currentTimeMillis();
	}
	
	public MarketItem(CompoundNBT nbt) {
		id = 		nbt.getInt("id");
		marketType =nbt.getInt("markettype");
		stock =		nbt.getInt("stock");
		item = 		ItemStack.read(nbt.getCompound("item"));
		vendor = 	nbt.getUniqueId("vendor");
		locality = 	nbt.getUniqueId("locality");
		buyer = 	nbt.getUniqueId("buyer");
		bidEnd =	nbt.getLong("bidend");
		dtgPlaced = nbt.getLong("dtgplaced");
		dtgBought = nbt.getLong("dtgbought");
		price =		nbt.getDouble("price");
		giveItem = 	nbt.getBoolean("give");
		active = 	nbt.getBoolean("active");
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("id", id);
		nbt.putInt("markettype", marketType);
		nbt.putInt("stock", stock);
		nbt.put("item", item.serializeNBT());
		nbt.putUniqueId("vendor", vendor);
		nbt.putUniqueId("locality", locality);
		nbt.putUniqueId("buyer", buyer);
		nbt.putLong("bidend", bidEnd);
		nbt.putLong("dtgplaced", dtgPlaced);
		nbt.putLong("dtgbought", dtgBought);
		nbt.putDouble("price", price);
		nbt.putBoolean("give", giveItem);
		nbt.putBoolean("active", active);
		return nbt;
	}
}
