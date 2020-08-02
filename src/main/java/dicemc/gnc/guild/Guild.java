package dicemc.gnc.guild;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;

public class Guild {
	public UUID guildID;
	public String name;
	public boolean open;
	public double tax;
	public Map<Integer, String> permLevels = new HashMap<Integer, String>();
	public Map<String, Integer> permissions = new HashMap<String, Integer>();
	public Map<UUID, Integer> members = new HashMap<UUID, Integer>();
	

	public Guild(String name, UUID guildID) {
		this.guildID = guildID;
		this.name = name;
		open = false;
		tax = 0.0;
	}
	
	public Guild(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();		
		return nbt;
	}
}
