package dicemc.gnc.datastorage.wsd;

import java.util.HashMap;
import java.util.Map;
import dicemc.gnc.GnC;
import dicemc.gnc.guild.Guild;
import dicemc.gnc.land.ChunkData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class WorldWSD extends WorldSavedData{
	private static final String DATA_NAME = GnC.MOD_ID + "_Guild";
	
	private Map<Integer, Guild> GUILDS = new HashMap<Integer, Guild>();
	private Map<ChunkPos, ChunkData> CHUNKS_O = new HashMap<ChunkPos, ChunkData>();
	private Map<ChunkPos, ChunkData> CHUNKS_N = new HashMap<ChunkPos, ChunkData>();
	private Map<ChunkPos, ChunkData> CHUNKS_E = new HashMap<ChunkPos, ChunkData>();
	
	public Map<Integer, Guild> getGuilds() {return GUILDS;}
	public Map<ChunkPos, ChunkData> getChunks(RegistryKey<World> dimension) {
		if (dimension == World.field_234918_g_) return CHUNKS_O;
		if (dimension == World.field_234919_h_) return CHUNKS_N;
		if (dimension == World.field_234920_i_) return CHUNKS_E;
		return null;
	}
	
	public WorldWSD() {super(DATA_NAME);}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT list = nbt.getList("guildlist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {GUILDS.put(list.getCompound(i).getInt("ID"), new Guild(list.getCompound(i).getCompound("guild"))); }
		list = nbt.getList("chunksO", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ChunkData ck = new ChunkData(list.getCompound(i));
			CHUNKS_O.put(ck.pos, ck);
		}
		list = nbt.getList("chunksN", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ChunkData ck = new ChunkData(list.getCompound(i));
			CHUNKS_N.put(ck.pos, ck);
		}
		list = nbt.getList("chunksE", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ChunkData ck = new ChunkData(list.getCompound(i));
			CHUNKS_E.put(ck.pos, ck);
		}
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
		for (Map.Entry<ChunkPos, ChunkData> entry :CHUNKS_O.entrySet()) {
			list.add(entry.getValue().toNBT());
		}
		compound.put("chunksO", list);
		list = new ListNBT();
		for (Map.Entry<ChunkPos, ChunkData> entry :CHUNKS_N.entrySet()) {
			list.add(entry.getValue().toNBT());
		}
		compound.put("chunksN", list);
		list = new ListNBT();
		for (Map.Entry<ChunkPos, ChunkData> entry :CHUNKS_E.entrySet()) {
			list.add(entry.getValue().toNBT());
		}
		compound.put("chunksE", list);
		return compound;
	}

	public static WorldWSD get(ServerWorld world) {
		return world.getSavedData().getOrCreate(WorldWSD::new, DATA_NAME);
	}
}
