package dicemc.gnc.guild;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class Guild {
	public UUID guildID;
	public String name;
	public boolean open;
	public boolean isAdmin;
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
    	SUBLET_MANAGE,		//can change sublet settings
    	CHANGE_NAME,
    	SET_TAX,
    	SET_OPEN_TO_JOIN,
    	BUY_NEW_RANK,
    	RANK_TITLE_CHANGE,
    	ACCOUNT_WITHDRAW,
    	MANAGE_PERMISSIONS,
    	SET_MEMBER_RANKS,
    	INVITE_MEMBERS,
    	KICK_MEMBER
    }

	public Guild(String name, UUID guildID, boolean isAdmin) {
		this.guildID = guildID;
		this.isAdmin = isAdmin;
		this.name = name;
		open = true;
		tax = 0.0;
		permLevels.put(0, "Member");
	}
	
	public Guild(CompoundNBT nbt) {
		guildID = nbt.getUniqueId("ID");
		name = nbt.getString("name");
		open = nbt.getBoolean("open");
		isAdmin = nbt.getBoolean("admin");
		tax = nbt.getDouble("tax");
		ListNBT list = nbt.getList("permlvls", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {permLevels.put(list.getCompound(i).getInt("rank"), list.getCompound(i).getString("title"));}
		list = nbt.getList("permissions", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {permissions.put(permKey.values()[list.getCompound(i).getInt("key")], list.getCompound(i).getInt("value"));}
		list = nbt.getList("members", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {members.put(list.getCompound(i).getUniqueId("ID"), list.getCompound(i).getInt("rank"));}
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putUniqueId("ID", guildID);
		nbt.putString("name", name);
		nbt.putBoolean("open", open);
		nbt.putBoolean("admin", isAdmin);
		nbt.putDouble("tax", tax);
		ListNBT list = new ListNBT();
		for (Map.Entry<Integer, String> map : permLevels.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("rank", map.getKey());
			snbt.putString("title", map.getValue());
			list.add(snbt);
		}
		nbt.put("permlvls", list);
		list = new ListNBT();
		for (Map.Entry<permKey, Integer> map : permissions.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("key", map.getKey().ordinal());
			snbt.putInt("value", map.getValue());
			list.add(snbt);
		}
		nbt.put("permissions", list);
		list = new ListNBT();
		for (Map.Entry<UUID, Integer> map : members.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putUniqueId("ID", map.getKey());
			snbt.putInt("rank", map.getValue());
			list.add(snbt);
		}
		nbt.put("members", list);
		return nbt;
	}
}
