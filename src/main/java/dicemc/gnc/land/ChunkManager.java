package dicemc.gnc.land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import dicemc.gnc.setup.Config;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ChunkManager {
	private Map<ChunkPos, ChunkData> cap = new HashMap<ChunkPos, ChunkData>();
	private MinecraftServer server;

	public ChunkManager() {}
	
	public void setServer(MinecraftServer server) {this.server = server;}
	
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
				try {updateWhitelistItem(ck, new WhitelistItem(JsonToNBT.getTagFromJson(vals.getValue())));} catch (CommandSyntaxException e) {}
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
		cap.get(ck).whitelist = whitelist;
		if (whitelist.size() == 0) return "Whitelist Cleared";
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
	
	public String removeWhitelistItem(ChunkPos ck, String item) {
		for (int i = 0; i < cap.get(ck).whitelist.size(); i++) {
			if (cap.get(ck).whitelist.get(i).getBlock().equalsIgnoreCase(item) || cap.get(ck).whitelist.get(i).getEntity().equalsIgnoreCase(item)) {
				cap.get(ck).whitelist.remove(i);
				return "WLitem Removed";
			}
		}
		return "Item Not Found";
	}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck) {return cap.get(ck).whitelist;}
	
	public String addPlayer(ChunkPos ck, UUID player) {		
		cap.get(ck).permittedPlayers.put(player, server.getPlayerProfileCache().getProfileByUUID(player).getName());
		return "Player Added";
	}
	
	public String removePlayer(ChunkPos ck, UUID player) {
		cap.get(ck).permittedPlayers.remove(player);
		return "Player Removed";
	}
	
	public Map<UUID, String> getPlayers(ChunkPos ck) {return cap.get(ck).permittedPlayers;}
	
	public void saveChunkData(ServerWorld world) {
		if (cap.size() == 0) return;
		for (Map.Entry<ChunkPos, ChunkData> map : cap.entrySet()) {
			WorldWSD.get(world).getChunks().put(map.getKey(), map.getValue());
		}
		WorldWSD.get(world).markDirty();
	}
	
	public void loadChunkData(ChunkPos ck, ServerWorld world) {
		ChunkData cnk = WorldWSD.get(world).getChunks().getOrDefault(ck, new ChunkData(ck));			
		cap.put(ck, cnk);
	}
	
	//BEGIN GAME LOGIC SECTION
	public String tempClaim(ChunkPos ck, UUID player) {
		if (!cap.get(ck).owner.equals(GnC.NIL) || !cap.get(ck).renter.equals(GnC.NIL)) return "Chunk Already Claimed";
		double balP = GnC.aMgr.getBalance(player);
		if (balP >= cap.get(ck).price*Config.TEMPCLAIM_RATE.get()) {
			GnC.aMgr.changeBalance(player, (-1 * (cap.get(ck).price * Config.TEMPCLAIM_RATE.get())));
			cap.get(ck).renter = player;
			cap.get(ck).permittedPlayers.put(player, server.getPlayerProfileCache().getProfileByUUID(player).getName());
			cap.get(ck).rentEnd = System.currentTimeMillis()+Config.TEMPCLAIM_DURATION.get();
			return "Temp Claim Successful";
		}
		else return "Insufficient funds to claim";
	}
	/**
	 * Used to claim land for a guild.  Method logic checks whether the land can be
	 * claimed as core land or if it should be claimed as an outpost.  If outpost,
	 * the method checks if this is adding to an outpost or creating a new one.
	 * If a new one, the outpost cost is added to the cost.  Additionally, after
	 * a successful claim a recursive method is called to procedurally check each
	 * adjacent chunk to see if it borders core land and update it if it is an outpost.
	 * @param ck the chunk being claimed
	 * @param guild the guild attempting to claim
	 * @return a textual result statement.
	 */
	public String guildClaim(ChunkPos ck, UUID guild) {
		int buyType = purchaseType(ck, guild);
		double outpostFee = (buyType == 1 ? Config.OUTPOST_CREATE_COST.get() : 0d);
		if (!cap.get(ck).owner.equals(GnC.NIL)) return "Chunk Already Claimed";
		double balG = GnC.aMgr.getBalance(guild);
		if (balG >= cap.get(ck).price + outpostFee) {
			GnC.aMgr.changeBalance(guild, (-1 * (cap.get(ck).price)));
			if (!cap.get(ck).renter.equals(GnC.NIL)) {
				GnC.aMgr.changeBalance(cap.get(ck).renter, cap.get(ck).price*Config.TEMPCLAIM_RATE.get());
				cap.get(ck).renter = GnC.NIL;
				cap.get(ck).permittedPlayers = new HashMap<UUID, String>();
			}
			cap.get(ck).owner = guild;
			cap.get(ck).leasePrice = -1;
			cap.get(ck).leaseDuration = 0;
			cap.get(ck).permMin = GnC.gMgr.getBottomRank(guild);
			cap.get(ck).rentEnd = System.currentTimeMillis();
			cap.get(ck).isPublic = false;
			cap.get(ck).isForSale = false;
			cap.get(ck).isOutpost = (buyType != 0);
			cap.get(ck).canExplode = false;
			cap.get(ck).whitelist = new ArrayList<WhitelistItem>();
			cap.get(ck).permittedPlayers = new HashMap<UUID, String>();
		}
		else return "Insufficient Guild Funds";
		updateOutpostToCore(ck, guild);
		return "Claim Successful";
	}
	
	/**
	 * Returns a value based on the type of claim.
	 * @param ck the chunk being checked
	 * @param guild the guild whose ownership is being evaluated
	 * @return 0=normal claim, 1=new outpost, 2=outpostclaim
	 */
	private int purchaseType(ChunkPos ck, UUID guild) {
		if ((cap.get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x+1, ck.z)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x+1, ck.z)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) 
			return 0;
		if ((cap.get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && cap.get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x+1, ck.z)).owner.equals(guild) && cap.get(new ChunkPos(ck.x+1, ck.z)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && cap.get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
			(cap.get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && cap.get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) 
				return 2;
		return 1;
	}
	
	private void updateOutpostToCore(ChunkPos ck, UUID guild) {
		if (cap.get(ck).isOutpost && cap.get(ck).owner.equals(guild)) {
			if ((cap.get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
				(cap.get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x+1, ck.z)).isOutpost)	||
				(cap.get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
				(cap.get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && !cap.get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) {
				cap.get(ck).isOutpost = false;
				//TODO: Remove after testing
				System.out.println("Updated to Core: "+ck.toString());
				updateOutpostToCore(new ChunkPos(ck.x-1, ck.z), guild);
				updateOutpostToCore(new ChunkPos(ck.x+1, ck.z), guild);
				updateOutpostToCore(new ChunkPos(ck.x, ck.z-1), guild);
				updateOutpostToCore(new ChunkPos(ck.x, ck.z+1), guild);
			}
		}
	}
	
	public String extendClaim(ChunkPos ck, UUID player) {
		double balP = GnC.aMgr.getBalance(player);
		double cost = (cap.get(ck).price*Config.TEMPCLAIM_RATE.get()*cap.get(ck).permittedPlayers.size());
		if (balP >= cost) {
			GnC.aMgr.changeBalance(player, (-1 * cost));
			cap.get(ck).rentEnd += Config.TEMPCLAIM_DURATION.get();
			return "Claim Extended";
		}
		return "Insufficient Funds";
	}
	
	public String publicToggle(ChunkPos ck, boolean value) {
		cap.get(ck).isPublic = value;
		return "Access Updated";
	}
}
