package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.setup.Config;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants;

public class ChunkData {
	public ChunkPos pos;
	public UUID owner = GnC.NIL;
	public UUID renter = GnC.NIL;
	public double price = Config.DEFAULT_LAND_PRICE.get();
	public double leasePrice = -1;
	public int leaseDuration = 0;
	public int permMin = 1;
	public long rentEnd = System.currentTimeMillis() + Config.TEMPCLAIM_DURATION.get();
	public boolean isPublic = false;
	public boolean isForSale = false;
	public boolean isOutpost = false;
	public boolean canExplode = true;
	public List<WhitelistItem> whitelist = new ArrayList<WhitelistItem>();
	public Map<UUID, String> permittedPlayers = new HashMap<UUID, String>();
	
	public ChunkData(ChunkPos pos) {this.pos = pos;}
	
	public ChunkData(CompoundNBT nbt) {
		pos = new ChunkPos(nbt.getLong("pos"));
		leaseDuration = nbt.getInt("leaseduration");
		rentEnd = nbt.getLong("rentend");
		owner = nbt.getUniqueId("owner");
		renter = nbt.getUniqueId("renter");
		price = nbt.getDouble("price");
		leasePrice = nbt.getDouble("leaseprice");
		permMin = nbt.getInt("permmin");
		isPublic = nbt.getBoolean("ispublic");
		isForSale = nbt.getBoolean("isforsale");
		isOutpost = nbt.getBoolean("isoutpost");
		canExplode = nbt.getBoolean("canexplode");
		ListNBT list = nbt.getList("whitelist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			whitelist.add(new WhitelistItem(list.getCompound(i)));
		}
		list = nbt.getList("permittedplayers", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			permittedPlayers.put(list.getCompound(i).getUniqueId("UUID"), list.getCompound(i).getString("string"));
		}
	}
	
	public CompoundNBT toNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putLong("pos", pos.asLong());
		nbt.putInt("leaseduration", leaseDuration);
		nbt.putLong("rentend", rentEnd);
		nbt.putUniqueId("owner", owner);
		nbt.putUniqueId("renter", renter);
		nbt.putDouble("price", price);
		nbt.putDouble("leaseprice", leasePrice);
		nbt.putInt("permmin", permMin);
		nbt.putBoolean("ispublic", isPublic);
		nbt.putBoolean("isforsale", isForSale);
		nbt.putBoolean("isoutpost", isOutpost);
		nbt.putBoolean("canexplode", canExplode);
		ListNBT list = new ListNBT();
		for (int i = 0; i < whitelist.size(); i++) {
			list.add(whitelist.get(i).toNBT());
		}
		nbt.put("whitelist", list);
		list = new ListNBT();
		for (Map.Entry<UUID, String> entry : permittedPlayers.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putUniqueId("UUID", entry.getKey());
			snbt.putString("string", entry.getValue());
			list.add(snbt);
		}
		nbt.put("permittedplayers", list);
		return nbt;
	}
}

