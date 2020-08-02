package dicemc.gnc.land;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.database.TableDefinitions;
import dicemc.gnc.setup.Config;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants;

public class ChunkData {
	public ChunkPos pos;
	public String owner = GnC.NIL.toString();
	public UUID renter = GnC.NIL;
	public double price = Config.DEFAULT_LAND_PRICE.get();
	public double leasePrice = -1;
	public long leaseDuration = 0;
	public int permMin = 1;
	public long rentEnd = System.currentTimeMillis() + Config.TEMPCLAIM_DURATION.get();
	public boolean isPublic = false;
	public boolean isForSale = false;
	public boolean isOutpost = false;
	public boolean canExplode = true;
	public List<WhitelistItem> whitelist = new ArrayList<WhitelistItem>();
	public Map<UUID, String> permittedPlayers = new HashMap<UUID, String>();
	
	public ChunkData(ChunkPos pos) {this.pos = pos;}
	
	public ChunkData(ResultSet rs, ChunkPos p) {
		try {
			this.pos = new ChunkPos(rs.getInt(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_X)), rs.getInt(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_Z)));
			this.owner = rs.getString(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.OWNER));
			this.renter = UUID.fromString(rs.getString(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.TEMP_RENTER)));
			this.price = rs.getDouble(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PRICE));
			this.leasePrice = rs.getDouble(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PRICE_LEASE));
			this.leaseDuration = rs.getLong(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.LEASE_DURATION));
			this.permMin = rs.getInt(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PERM_MIN));
			this.rentEnd = rs.getLong(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.RENT_END));
			this.isPublic = rs.getBoolean(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_PUBLIC));
			this.isForSale = rs.getBoolean(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_FOR_SALE));
			this.isOutpost = rs.getBoolean(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_OUTPOST));
			this.canExplode = rs.getBoolean(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CAN_EXPLOSIONS));
		} catch (SQLException e) {this.pos = p;}
	}
	
	public ChunkData(CompoundNBT nbt) {
		pos = new ChunkPos(nbt.getLong("pos"));
		leaseDuration = nbt.getLong("leaseduration");
		rentEnd = nbt.getLong("rentend");
		owner = nbt.getString("owner");
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
		nbt.putLong("leaseduration", leaseDuration);
		nbt.putLong("rentend", rentEnd);
		nbt.putString("owner", owner);
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
		return nbt;}

	public List<String> toDBList() {
		List<String> list = new ArrayList<String>();
		list.add(String.valueOf(pos.x));
		list.add(String.valueOf(pos.z));
		list.add(owner);
		list.add(renter.toString());
		list.add(String.valueOf(price));
		list.add(String.valueOf(leasePrice));
		list.add(String.valueOf(leaseDuration));
		list.add(String.valueOf(permMin));
		list.add(String.valueOf(rentEnd));
		list.add(String.valueOf(isPublic));
		list.add(String.valueOf(isForSale));
		list.add(String.valueOf(isOutpost));
		list.add(String.valueOf(canExplode));
		return list;
	}
	
	public List<String> fieldList() {
		List<String> list = new ArrayList<String>();
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_X));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CHUNK_Z));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.OWNER));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.TEMP_RENTER));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PRICE));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PRICE_LEASE));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.LEASE_DURATION));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.PERM_MIN));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.RENT_END));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_PUBLIC));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_FOR_SALE));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.IS_OUTPOST));
		list.add(TableDefinitions.map_Chunk.get(TableDefinitions.tblChunk.CAN_EXPLOSIONS));
		return list;
	}
}

