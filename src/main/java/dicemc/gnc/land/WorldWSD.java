package dicemc.gnc.land;

import java.util.HashMap;
import java.util.Map;
import dicemc.gnc.GnC;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.realestate.ILogicRealEstate;
import dicemc.gnclib.util.ChunkPos3D;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class WorldWSD extends WorldSavedData implements ILogicRealEstate{
	private static final String DATA_NAME = GnC.MOD_ID + "_WorldData";

	private Map<ChunkPos3D, ChunkData> CHUNKS = new HashMap<ChunkPos3D, ChunkData>();	
	
	public WorldWSD() {super(DATA_NAME);}

	@Override
	public void load(CompoundNBT nbt) {
		ListNBT list = nbt.getList("ckData", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			ChunkData data = ChunkDataUtil.chunkDataFromNBT(list.getCompound(i));
			CHUNKS.put(data.pos, data);
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		ListNBT list = new ListNBT();
		for (Map.Entry<ChunkPos3D, ChunkData> map : CHUNKS.entrySet()) {
			list.add(ChunkDataUtil.chunkDataToNBT(map.getValue()));
		}
		compound.put("ckData", list);
		return compound;
	}

	public static WorldWSD get(ServerWorld world) {
		return world.getDataStorage().computeIfAbsent(WorldWSD::new, DATA_NAME);
	}

	@Override
	public Map<ChunkPos3D, ChunkData> getCap() {return CHUNKS;}
	
	public ChunkData getOrCreate(ChunkPos3D pos) {
		if (CHUNKS.containsKey(pos)) return CHUNKS.get(pos);
		CHUNKS.put(pos, new ChunkData(pos));
		return new ChunkData(pos);
	}

	@Override
	public void saveChunkData() {this.setDirty();}
}
