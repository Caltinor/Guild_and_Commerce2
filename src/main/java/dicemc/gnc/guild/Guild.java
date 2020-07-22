package dicemc.gnc.guild;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;

public class Guild {
	public int GuildID;
	public String Name;
	public boolean Open;
	public double Tax;
	public Map<Integer, String> PermLevels = new HashMap<Integer, String>();
	public Map<String, Integer> Permissions = new HashMap<String, Integer>();
	public Map<UUID, Integer> Members = new HashMap<UUID, Integer>();
	

	public Guild(String name) {
		GuildID = -1;
		Name = name;
		Open = false;
		Tax = 0.0;
	}
	
	public Guild(ResultSet rs) {}
	
	public Guild(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();		
		return nbt;
	}
}
