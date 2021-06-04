package dicemc.gnc.land;

import java.util.Map;
import java.util.UUID;

import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.realestate.WhitelistEntry;
import dicemc.gnclib.util.Agent;
import dicemc.gnclib.util.ChunkPos3D;
import dicemc.gnclib.util.Agent.Type;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class ChunkDataUtil {
	public static CompoundNBT chunkDataToNBT(ChunkData data) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putLong("pos", data.pos.toLong());
		nbt.put("owner", agentToNBT(data.owner));
		nbt.put("renter", agentToNBT(data.renter));
		nbt.putDouble("price", data.price);
		nbt.putDouble("leaseprice", data.leasePrice);
		nbt.putInt("leaseduration", data.leaseDuration);
		nbt.putInt("permmin", data.permMin);
		nbt.putLong("rentend", data.rentEnd);
		nbt.putBoolean("public", data.isPublic);
		nbt.putBoolean("forsale", data.isForSale);
		nbt.putBoolean("explode", data.canExplode);
		ListNBT list = new ListNBT();
		for (Map.Entry<String, WhitelistEntry> wle : data.whitelist.entrySet()) {
			CompoundNBT wnbt = new CompoundNBT();
			wnbt.putString("item", wle.getKey());
			wnbt.putBoolean("break", wle.getValue().getCanBreak());
			wnbt.putBoolean("interact", wle.getValue().getCanInteract());
			list.add(wnbt);
		}
		nbt.put("whitelist", list);
		list = new ListNBT();
		for (int i = 0; i < data.permittedPlayers.size(); i++) {
			list.add(agentToNBT(data.permittedPlayers.get(i)));
		}
		nbt.put("permitted", list);
		return nbt;
	}
	
	public static ChunkData chunkDataFromNBT(CompoundNBT nbt) {
		ChunkData data = new ChunkData(new ChunkPos3D(nbt.getLong("pos")));
		data.owner = agentFromNBT(nbt.getCompound("owner"));
		data.renter = agentFromNBT(nbt.getCompound("renter"));
		data.price = nbt.getDouble("price");
		data.leasePrice = nbt.getDouble("leaseprice");
		data.leaseDuration = nbt.getInt("leaseduration");
		data.permMin = nbt.getInt("permmin");
		data.rentEnd = nbt.getLong("rentend");
		data.isPublic = nbt.getBoolean("public");
		data.isForSale = nbt.getBoolean("forsale");
		data.canExplode = nbt.getBoolean("explode");
		ListNBT list = nbt.getList("whitelist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			String item = list.getCompound(i).getString("item");
			boolean canBreak = list.getCompound(i).getBoolean("break");
			boolean canInteract = list.getCompound(i).getBoolean("interact");
			data.whitelist.put(item, new WhitelistEntry(canBreak, canInteract));
		}
		list = nbt.getList("permitted", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			data.permittedPlayers.add(agentFromNBT(list.getCompound(i)));
		}
		return data;
	}
	
	public static CompoundNBT agentToNBT(Agent agent) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("id", agent.id);
		nbt.putInt("type", agent.type.ordinal());
		nbt.putUUID("refid", agent.refID);
		nbt.putString("name", agent.name);
		return nbt;
	}
	
	public static Agent agentFromNBT(CompoundNBT nbt) {
		int id = nbt.getInt("id");
		Type type = Type.values()[nbt.getInt("type")];
		UUID refID = nbt.getUUID("refid");
		String name = nbt.getString("name");
		return new Agent(id, type, refID, name);
	}
}
