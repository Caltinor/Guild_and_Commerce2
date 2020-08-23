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
	private Map<ChunkPos, ChunkData> CHUNKS = new HashMap<ChunkPos, ChunkData>();
	
	public Map<Integer, Guild> getGuilds() {return GUILDS;}
	public Map<ChunkPos, ChunkData> getChunks() {return CHUNKS;}
	
	public WorldWSD() {super(DATA_NAME);}

	@Override
	public void read(CompoundNBT nbt) {
		ListNBT list = nbt.getList("guildlist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {GUILDS.put(list.getCompound(i).getInt("ID"), new Guild(list.getCompound(i).getCompound("guild"))); }
		list = nbt.getList("chunklist", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ChunkData ck = new ChunkData(list.getCompound(i));
			CHUNKS.put(ck.pos, ck);
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
		for (Map.Entry<ChunkPos, ChunkData> entry :CHUNKS.entrySet()) {
			list.add(entry.getValue().toNBT());
		}
		compound.put("chunklist", list);
		return compound;
	}

	public static WorldWSD get(ServerWorld world) {
		return world.getSavedData().getOrCreate(WorldWSD::new, DATA_NAME);
	}
}
