package dicemc.gnc.land.items;

import java.util.HashMap;
import java.util.Map;

import dicemc.gnclib.realestate.WhitelistEntry;
import dicemc.gnclib.realestate.WhitelistEntry.UpdateType;
import dicemc.gnclib.realestate.items.IDefaultWhitelister;
import dicemc.gnclib.realestate.items.IWhitelister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Whitelister extends Item implements IDefaultWhitelister, IWhitelister{

	public Whitelister(Properties p_i48487_1_) {
		super(p_i48487_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, WhitelistEntry> getWhitelist(Object stackIn) {
		Map<String, WhitelistEntry> map = new HashMap<>();
		if (!(stackIn instanceof ItemStack)) return map;
		ItemStack stack = (ItemStack)stackIn;
		CompoundNBT nbt = stack.getTag();
		// TODO Auto-generated method stub
		return map;
	}

	@Override
	public void setWhitelister(Object stack, Map<String, WhitelistEntry> whitelist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToWhitelister(Object stack, String item, UpdateType type) {
		// TODO Auto-generated method stub
		
	}

}
