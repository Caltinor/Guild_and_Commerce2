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
	public Map<permKey, Integer> permissions = new HashMap<permKey, Integer>();
	public Map<UUID, Integer> members = new HashMap<UUID, Integer>();
	
	public static enum permKey {
    	CORE_CLAIM,			//can claim land connected to the core
    	OUTPOST_CLAIM,		//can claim land connected to an outpost (increases taxation) 
    	OUTPOST_CREATE,		//can create new outposts
    	CLAIM_ABANDON,		//can abandon claims
    	CLAIM_SELL,			//can sell claims
    	SUBLET_MANAGE		//can change sublet settings
    }

	public Guild(String name, UUID guildID) {
		this.guildID = guildID;
		this.name = name;
		open = false;
		tax = 0.0;
		permLevels.put(0, "Member");
	}
	
	public Guild(CompoundNBT nbt) {}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();		
		return nbt;
	}
}
