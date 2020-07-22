package dicemc.gnc.datastorage.wsd;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.ChunkData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class WorldWSD extends WorldSavedData implements Supplier<WorldWSD>{
	private static final String DATA_NAME = GnC.MOD_ID + "_Guild";
	
	private Map<Integer, Guild> GUILDS = new HashMap<Integer, Guild>();
	private Map<ChunkPos, ChunkData> CHUNKS = new HashMap<ChunkPos, ChunkData>();
	
	public Map<Integer, Guild> getGuilds() {return GUILDS;}
	public Map<ChunkPos, ChunkData> getChunks() {return CHUNKS;}
	
	public WorldWSD() {super(DATA_NAME);}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT list = nbt.getList("guildlist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {GUILDS.put(list.getCompound(i).getInt("ID"), new Guild(list.getCompound(i).getCompound("guild"))); }
		list = nbt.getList("chunklist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {CHUNKS.put(new ChunkPos(list.getCompound(i).getLong("pos")), new ChunkData(list.getCompound(i).getCompound("data")));}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = new CompoundNBT();
		ListNBT list = new ListNBT();
		for (Map.Entry<Integer, Guild> entry : GUILDS.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putInt("ID", entry.getKey());
			snbt.put("guild", entry.getValue().toNBT());
			list.add(snbt);
		}
		compound.put("guildlist", list);
		list = new ListNBT();
		for (Map.Entry<ChunkPos, ChunkData> entry :CHUNKS.entrySet()) {
			CompoundNBT snbt = new CompoundNBT();
			snbt.putLong("pos", entry.getKey().asLong());
			snbt.put("data", entry.getValue().toNBT());
			list.add(snbt);
		}
		compound.put("chunklist", list);
		return compound;
	}
	
	public static WorldWSD forWorld(ServerWorld world) {
		DimensionSavedDataManager storage = world.getSavedData();
		Supplier<WorldWSD> sup = new WorldWSD();
		WorldWSD instance = (WorldWSD) storage.getOrCreate(sup, GnC.MOD_ID);
		
		if (instance == null) {
			instance = new WorldWSD();
			storage.set(instance);
		}
		return instance;
	}

	@Override
	public WorldWSD get() {
		return this;
	}
}
