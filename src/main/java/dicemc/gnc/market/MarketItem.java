package dicemc.gnc.market;

import java.util.UUID;

public class MarketItem {
	public int id = -1;
	public int marketType;
	public ItemStack item;
	public UUID vendor;
	public int locality;
	public long bidEnd;
	public double price;
	public boolean giveItem;
	public int stock = 1;
	public boolean active = true;
	public UUID buyer = GnC.NIL;
	public long dtgPlaced;
	public long dtgBought = null;
	
	public MarketItem(int type, ItemStack item, UUID vendor, int locality, @Nullable long bidEnd, double price, boolean giveItem) {
		this.marketType = type;
		this.item = item;
		this.vendor = vendor;
		this.locality = locality;
		this.bidEnd = bidEnd == null ? null : bidEnd;
		this.price = price;
		this.giveItem = giveItem;
		dtgPlaced = System.currentTimeMillis();
	}
	
	public MarketItem(ResultSet rs) {}
	
	public MarketItem(CompoundNBT nbt) {}
	
	public CompountNBT toNBT() {return new CompoundNBT();}
}
