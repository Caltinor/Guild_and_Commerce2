package dicemc.gnc.market;

import java.sql.ResultSet;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;

public class StorageItem {
	public String owner;
	public Item item;
	public int quantity;
	
	public StorageItem(String owner, Item item, int quantity) {
		this.owner = owner;
		this.item = item;
		this.quantity = quantity;
	}
	
	public StorageItem(ResultSet rs) {}
	
	public StorageItem(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {return new CompoundNBT();}
}
