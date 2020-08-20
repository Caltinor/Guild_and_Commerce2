package dicemc.gnc.market;

import java.sql.ResultSet;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class StorageItem {
	public UUID owner;
	public ItemStack item;
	public int quantity;
	
	public StorageItem(UUID owner, ItemStack item, int quantity) {
		this.owner = owner;
		this.item = item;
		this.quantity = quantity;
	}
	
	public StorageItem(ResultSet rs) {}
	
	public StorageItem(CompoundNBT nbt) {
		owner = nbt.getUniqueId("ID");
		quantity = nbt.getInt("quantity");
		item = ItemStack.read(nbt.getCompound("item"));
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putUniqueId("ID", owner);
		nbt.putInt("quantity", quantity);
		nbt.put("item", item.serializeNBT());
		return nbt;
	}
}
