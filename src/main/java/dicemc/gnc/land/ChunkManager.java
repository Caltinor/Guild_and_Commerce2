package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
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
			case "player": {
				cd.permittedPlayers.put(UUID.fromString(vals.getValue().substring(0, 36)), vals.getValue().substring(36));
				break;
			}
			case "whitelist": {
				try {GnC.ckMgr.updateWhitelistItem(ck, new WhitelistItem(JsonToNBT.getTagFromJson(vals.getValue())));} catch (CommandSyntaxException e) {}
				break;
			}
			default: return "Key Unrecognized" + vals.getKey();
			}
		}
		cap.put(ck, cd);
		return "Success";
	}
	
	public ChunkData getChunk(ChunkPos pos) {return cap.get(pos);}
	
	public String setWhitelist(ChunkPos ck, List<WhitelistItem> whitelist) {
		//F-U: probably smart to add a null check here.
		cap.get(ck).whitelist = whitelist;
		return "Whitelist Set";
	}
	
	public String updateWhitelistItem(ChunkPos ck, WhitelistItem wlItem) {
		for (int i = 0; i < cap.get(ck).whitelist.size(); i++) {
			if (!wlItem.getBlock().isEmpty() && cap.get(ck).whitelist.get(i).getBlock().equalsIgnoreCase(wlItem.getBlock())) {
				if (cap.get(ck).whitelist.get(i).getCanBreak() != wlItem.getCanBreak()) {cap.get(ck).whitelist.get(i).setCanBreak(cap.get(ck).whitelist.get(i).getCanBreak());}
				if (cap.get(ck).whitelist.get(i).getCanInteract() != wlItem.getCanInteract()) {cap.get(ck).whitelist.get(i).setCanInteract(wlItem.getCanInteract());}
				return "WLItem Updated";
			}
			else if (!wlItem.getEntity().isEmpty() && cap.get(ck).whitelist.get(i).getEntity().equalsIgnoreCase(wlItem.getEntity())) {
				if (cap.get(ck).whitelist.get(i).getCanBreak() != wlItem.getCanBreak()) {cap.get(ck).whitelist.get(i).setCanBreak(cap.get(ck).whitelist.get(i).getCanBreak());}
				if (cap.get(ck).whitelist.get(i).getCanInteract() != wlItem.getCanInteract()) {cap.get(ck).whitelist.get(i).setCanInteract(wlItem.getCanInteract());}
				return "WLItem Updated";
			}
		}
		cap.get(ck).whitelist.add(wlItem);
		return "WLItem Added";
	}
	
	public String removeWhitelistItem(ChunkPos ck, String item) {return "";}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck) {return cap.get(ck).whitelist;}
	
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
