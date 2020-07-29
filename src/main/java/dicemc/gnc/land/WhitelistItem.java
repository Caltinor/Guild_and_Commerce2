package dicemc.gnc.land;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

public class WhitelistItem {
	public String refBlock = "";
	public String refEntity = "";
	public boolean canBreak = false;
	public boolean canInteract = false;

	public WhitelistItem(Entity entity) {
		refEntity = entity.getClass().getSimpleName();
	}
	
	public WhitelistItem(String blockRegistryName) {
		refBlock = blockRegistryName;
	}
	
	public WhitelistItem(CompoundNBT nbt) {
		refEntity = nbt.getString("entity");
		refBlock = nbt.getString("block");
		canBreak = nbt.getBoolean("canbreak");
		canInteract = nbt.getBoolean("caninteract");
	}
	
	public boolean getCanBreak() {return canBreak;}
	public void setCanBreak(boolean bool) {canBreak = bool;}
	public boolean getCanInteract() {return canInteract;}
	public void setCanInteract(boolean bool) {canInteract = bool;}
	public String getEntity() {return refEntity;}
	public String getBlock() {return refBlock;}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("entity", refEntity);
		nbt.putString("block", refBlock);
		nbt.putBoolean("canbreak", canBreak);
		nbt.putBoolean("caninteract", canInteract);
		return nbt;
	}
}
