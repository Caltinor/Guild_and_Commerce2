package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.util.math.ChunkPos;

public class ChunkManager {
	private Map<ChunkPos, ChunkData> cap = new HashMap<ChunkPos, ChunkData>();

	public ChunkManager() {}
	
	public String addChunk(ChunkPos pos) {
		//dummy code for compiler
		cap.put(pos, new ChunkData(pos));
		return "";}
	
	public String updateChunk(ChunkPos ck, Map<String, String> values) {return "";}
	
	public ChunkData getChunk(ChunkPos pos) {return new ChunkData(pos);}
	
	public String setWhitelist(ChunkPos ck, List<WhitelistItem> whitelist) {return "";}
	
	public String updateWhitelistItem(ChunkPos ck, WhitelistItem wlItem) {return "";}
	
	public String removeWhitelistItem(ChunkPos ck, String item) {return "";}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck) {return new ArrayList<WhitelistItem>();}
	
	public String addPlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String updatePlayer(ChunkPos ck, UUID player, boolean principal) {return "";}
	
	public String removePlayer(ChunkPos ck, UUID player) {return "";}
	
	public Map<UUID, String> getPlayers(ChunkPos ck) {return new HashMap<UUID, String>();}
	
	public String saveChunkData(ChunkPos ck) {return "";}
	
	public String loadChunkData(ChunkPos ck) {return "";}
}
