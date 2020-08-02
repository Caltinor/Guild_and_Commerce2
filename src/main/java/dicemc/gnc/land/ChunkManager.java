package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dicemc.gnc.datastorage.wsd.WorldWSD;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;

public class ChunkManager {
	private Map<ChunkPos, ChunkData> cap = new HashMap<ChunkPos, ChunkData>();

	public ChunkManager() {}
	
	public String addChunk(ChunkPos pos) {
		//dummy code for compiler
		cap.put(pos, new ChunkData(pos));
		return "";}
	
	public String updateChunk(ChunkPos ck, Map<String, String> values) {
		//TODO populate switch statement
		ChunkData cd = cap.getOrDefault(ck, new ChunkData(ck));
		for (Map.Entry<String, String> vals : values.entrySet()) {
			switch(vals.getKey()) {
			case "price": {
				cd.price = Double.valueOf(vals.getValue());
				System.out.println("price updated" +String.valueOf(cap.get(ck).price));
				break;
			}
			default: return "Key Unrecognized" + vals.getKey();
			}
		}
		cap.put(ck, cd);
		return "Success";
	}
	
	public ChunkData getChunk(ChunkPos pos) {return cap.get(pos);}
	
	public String setWhitelist(ChunkPos ck, List<WhitelistItem> whitelist) {return "";}
	
	public String updateWhitelistItem(ChunkPos ck, WhitelistItem wlItem) {return "";}
	
	public String removeWhitelistItem(ChunkPos ck, String item) {return "";}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck) {return new ArrayList<WhitelistItem>();}
	
	public String addPlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String updatePlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String removePlayer(ChunkPos ck, UUID player) {return "";}
	
	public Map<UUID, String> getPlayers(ChunkPos ck) {return new HashMap<UUID, String>();}
	
	public void saveChunkData(ServerWorld world) {
		for (Map.Entry<ChunkPos, ChunkData> map : cap.entrySet()) {
			WorldWSD.get(world).getChunks().put(map.getKey(), map.getValue());
		}
		WorldWSD.get(world).markDirty();
	}
	
	public void loadChunkData(ChunkPos ck, ServerWorld world) {
		ChunkData cnk = WorldWSD.get(world).getChunks().getOrDefault(ck, new ChunkData(ck));			
		cap.put(ck, cnk);
	}
}
