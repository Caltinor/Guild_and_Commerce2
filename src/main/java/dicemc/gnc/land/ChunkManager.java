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
	private Map<ChunkPos, ChunkData> ocap = new HashMap<ChunkPos, ChunkData>();
	private Map<ChunkPos, ChunkData> ncap = new HashMap<ChunkPos, ChunkData>();
	private Map<ChunkPos, ChunkData> ecap = new HashMap<ChunkPos, ChunkData>();
	private MinecraftServer server;

	public ChunkManager() {}
	
	public void setServer(MinecraftServer server) {this.server = server;}
	
	public Map<ChunkPos, ChunkData> cap(RegistryKey<World> dimension) {
		if (dimension == World.field_234918_g_) return ocap;
		if (dimension == World.field_234919_h_) return ncap;
		if (dimension == World.field_234920_i_) return ecap;
		return null;
	}
	
	public String updateChunk(ChunkPos ck, Map<String, String> values, RegistryKey<World> dim) {
		//TODO populate switch statement
		ChunkData cd = cap(dim).getOrDefault(ck, new ChunkData(ck));
		for (Map.Entry<String, String> vals : values.entrySet()) {
			switch(vals.getKey()) {
			case "price": {
				cd.price = Double.valueOf(vals.getValue());
				System.out.println("price updated" +String.valueOf(cap(dim).get(ck).price));
				break;
			}
			case "player": {
				cd.permittedPlayers.put(UUID.fromString(vals.getValue().substring(0, 36)), vals.getValue().substring(36));
				break;
			}
			case "whitelist": {
				try {GnC.ckMgr.updateWhitelistItem(ck, new WhitelistItem(JsonToNBT.getTagFromJson(vals.getValue())), dim);} catch (CommandSyntaxException e) {}
				break;
			}
			default: return "Key Unrecognized" + vals.getKey();
			}
		}
		cap(dim).put(ck, cd);
		return "Success";
	}
	
	public ChunkData getChunk(ChunkPos pos, RegistryKey<World> dim) {return cap(dim).get(pos);}
	
	public String setWhitelist(ChunkPos ck, List<WhitelistItem> whitelist, RegistryKey<World> dim) {
		cap(dim).get(ck).whitelist = whitelist;
		if (whitelist.size() == 0) return "Whitelist Cleared";
		return "Whitelist Set";
	}
	
	public String updateWhitelistItem(ChunkPos ck, WhitelistItem wlItem, RegistryKey<World> dim) {
		for (int i = 0; i < cap(dim).get(ck).whitelist.size(); i++) {
			if (!wlItem.getBlock().isEmpty() && cap(dim).get(ck).whitelist.get(i).getBlock().equalsIgnoreCase(wlItem.getBlock())) {
				if (cap(dim).get(ck).whitelist.get(i).getCanBreak() != wlItem.getCanBreak()) {cap(dim).get(ck).whitelist.get(i).setCanBreak(cap(dim).get(ck).whitelist.get(i).getCanBreak());}
				if (cap(dim).get(ck).whitelist.get(i).getCanInteract() != wlItem.getCanInteract()) {cap(dim).get(ck).whitelist.get(i).setCanInteract(wlItem.getCanInteract());}
				return "WLItem Updated";
			}
			else if (!wlItem.getEntity().isEmpty() && cap(dim).get(ck).whitelist.get(i).getEntity().equalsIgnoreCase(wlItem.getEntity())) {
				if (cap(dim).get(ck).whitelist.get(i).getCanBreak() != wlItem.getCanBreak()) {cap(dim).get(ck).whitelist.get(i).setCanBreak(cap(dim).get(ck).whitelist.get(i).getCanBreak());}
				if (cap(dim).get(ck).whitelist.get(i).getCanInteract() != wlItem.getCanInteract()) {cap(dim).get(ck).whitelist.get(i).setCanInteract(wlItem.getCanInteract());}
				return "WLItem Updated";
			}
		}
		cap(dim).get(ck).whitelist.add(wlItem);
		return "WLItem Added";
	}
	
	public String removeWhitelistItem(ChunkPos ck, String item, RegistryKey<World> dim) {
		for (int i = 0; i < cap(dim).get(ck).whitelist.size(); i++) {
			if (cap(dim).get(ck).whitelist.get(i).getBlock().equalsIgnoreCase(item) || cap(dim).get(ck).whitelist.get(i).getEntity().equalsIgnoreCase(item)) {
				cap(dim).get(ck).whitelist.remove(i);
				return "WLitem Removed";
			}
		}
		return "Item Not Found";
	}
	
	public List<WhitelistItem> getWhitelist(ChunkPos ck, RegistryKey<World> dim) {return cap(dim).get(ck).whitelist;}
	
	public String addPlayer(ChunkPos ck, UUID player, RegistryKey<World> dim) {		
		cap(dim).get(ck).permittedPlayers.put(player, server.getPlayerProfileCache().getProfileByUUID(player).getName());
		return "Player Added";
	}
	
	public String removePlayer(ChunkPos ck, UUID player, RegistryKey<World> dim) {
		cap(dim).get(ck).permittedPlayers.remove(player);
		return "Player Removed";
	}
	
	public Map<UUID, String> getPlayers(ChunkPos ck, RegistryKey<World> dim) {return cap(dim).get(ck).permittedPlayers;}
	
	public void saveChunkData(ServerWorld world, RegistryKey<World> dim) {
		for (Map.Entry<ChunkPos, ChunkData> map : cap(dim).entrySet()) {
			WorldWSD.get(world).getChunks(dim).put(map.getKey(), map.getValue());
		}
		WorldWSD.get(world).markDirty();
	}
	
	public void loadChunkData(ChunkPos ck, ServerWorld world, RegistryKey<World> dim) {
		ChunkData cnk = WorldWSD.get(world).getChunks(dim).getOrDefault(ck, new ChunkData(ck));			
		cap(dim).put(ck, cnk);
	}
	
	//BEGIN GAME LOGIC SECTION
	public String tempClaim(ChunkPos ck, UUID player, RegistryKey<World> dim) {
		if (!cap(dim).get(ck).owner.equals(GnC.NIL) || !cap(dim).get(ck).renter.equals(GnC.NIL)) return "Chunk Already Claimed";
		double balP = GnC.aMgr.getBalance(player);
		if (balP >= cap(dim).get(ck).price*Config.TEMPCLAIM_RATE.get()) {
			GnC.aMgr.changeBalance(player, (-1 * (cap(dim).get(ck).price * Config.TEMPCLAIM_RATE.get())));
			cap(dim).get(ck).renter = player;
			cap(dim).get(ck).permittedPlayers.put(player, server.getPlayerProfileCache().getProfileByUUID(player).getName());
			cap(dim).get(ck).rentEnd = System.currentTimeMillis()+Config.TEMPCLAIM_DURATION.get();
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
	public String guildClaim(ChunkPos ck, UUID guild, RegistryKey<World> dim) {
		int buyType = purchaseType(ck, guild, dim);
		double outpostFee = (buyType == 1 ? Config.OUTPOST_CREATE_COST.get() : 0d);
		if (!cap(dim).get(ck).owner.equals(GnC.NIL)) return "Chunk Already Claimed";
		double balG = GnC.aMgr.getBalance(guild);
		if (balG >= cap(dim).get(ck).price + outpostFee) {
			GnC.aMgr.changeBalance(guild, (-1 * (cap(dim).get(ck).price)));
			if (!cap(dim).get(ck).renter.equals(GnC.NIL)) {
				GnC.aMgr.changeBalance(cap(dim).get(ck).renter, cap(dim).get(ck).price*Config.TEMPCLAIM_RATE.get());
				cap(dim).get(ck).renter = GnC.NIL;
				cap(dim).get(ck).permittedPlayers = new HashMap<UUID, String>();
			}
			cap(dim).get(ck).owner = guild;
			cap(dim).get(ck).leasePrice = -1;
			cap(dim).get(ck).leaseDuration = 0;
			cap(dim).get(ck).permMin = GnC.gMgr.getBottomRank(guild);
			cap(dim).get(ck).rentEnd = System.currentTimeMillis();
			cap(dim).get(ck).isPublic = false;
			cap(dim).get(ck).isForSale = false;
			cap(dim).get(ck).isOutpost = (buyType != 0);
			cap(dim).get(ck).canExplode = false;
			cap(dim).get(ck).whitelist = new ArrayList<WhitelistItem>();
			cap(dim).get(ck).permittedPlayers = new HashMap<UUID, String>();
		}
		else return "Insufficient Guild Funds";
		updateOutpostToCore(ck, guild, dim);
		return "Claim Successful";
	}
	
	/**
	 * Returns a value based on the type of claim.
	 * @param ck the chunk being checked
	 * @param guild the guild whose ownership is being evaluated
	 * @return 0=normal claim, 1=new outpost, 2=outpostclaim
	 */
	private int purchaseType(ChunkPos ck, UUID guild, RegistryKey<World> dim) {
		if ((cap(dim).get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x+1, ck.z)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x+1, ck.z)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) 
			return 0;
		if ((cap(dim).get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && cap(dim).get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x+1, ck.z)).owner.equals(guild) && cap(dim).get(new ChunkPos(ck.x+1, ck.z)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && cap(dim).get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
			(cap(dim).get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && cap(dim).get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) 
				return 2;
		return 1;
	}
	
	private void updateOutpostToCore(ChunkPos ck, UUID guild, RegistryKey<World> dim) {
		if (cap(dim).get(ck).isOutpost && cap(dim).get(ck).owner.equals(guild)) {
			if ((cap(dim).get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x-1, ck.z)).isOutpost) ||
				(cap(dim).get(new ChunkPos(ck.x-1, ck.z)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x+1, ck.z)).isOutpost)	||
				(cap(dim).get(new ChunkPos(ck.x, ck.z-1)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x, ck.z-1)).isOutpost) ||
				(cap(dim).get(new ChunkPos(ck.x, ck.z+1)).owner.equals(guild) && !cap(dim).get(new ChunkPos(ck.x, ck.z+1)).isOutpost)) {
				cap(dim).get(ck).isOutpost = false;
				//TODO: Remove after testing
				System.out.println("Updated to Core: "+ck.toString());
				updateOutpostToCore(new ChunkPos(ck.x-1, ck.z), guild, dim);
				updateOutpostToCore(new ChunkPos(ck.x+1, ck.z), guild, dim);
				updateOutpostToCore(new ChunkPos(ck.x, ck.z-1), guild, dim);
				updateOutpostToCore(new ChunkPos(ck.x, ck.z+1), guild, dim);
			}
		}
	}
	
	public String extendClaim(ChunkPos ck, UUID player, RegistryKey<World> dim) {
		double balP = GnC.aMgr.getBalance(player);
		double cost = (cap(dim).get(ck).price*Config.TEMPCLAIM_RATE.get()*cap(dim).get(ck).permittedPlayers.size());
		if (balP >= cost) {
			GnC.aMgr.changeBalance(player, (-1 * cost));
			cap(dim).get(ck).rentEnd += Config.TEMPCLAIM_DURATION.get();
			return "Claim Extended";
		}
		return "Insufficient Funds";
	}
	
	public String publicToggle(ChunkPos ck, boolean value, RegistryKey<World> dim) {
		cap(dim).get(ck).isPublic = value;
		return "Access Updated";
	}
}
