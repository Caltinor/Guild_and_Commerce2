package dicemc.gnc.guild;

import java.util.UUID;

import javax.annotation.Nullable;

import dicemc.gnc.GnC;
import dicemc.gnc.datastorage.wsd.MarketWSD;
import dicemc.gnc.datastorage.wsd.WorldWSD;
import dicemc.gnc.guild.Guild.permKey;
import dicemc.gnc.land.ChunkData;
import dicemc.gnc.land.ChunkManager;
import dicemc.gnc.setup.Config;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.Map;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildManager {
	private Map<UUID, Guild> gmap = new HashMap<UUID, Guild>();
	private MinecraftServer server;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	
	public GuildManager() {}
	
	public void setServer(MinecraftServer server) {this.server = server;}

    /**
    *creates a new record in the map for a guild with default
    *values and the name provided.
    *
    *@return returns a single string of what was executed
    *
    *@param name the guild name as it should appear textually in game
    */
    public String createGuild(String name, boolean isAdmin) {
    	UUID id = unrepeatedUUIDs();
    	gmap.put(id, new Guild(name, id, isAdmin));
    	for (permKey perms : permKey.values()) {addPermission(id, perms, 0);}
    	return "Guild "+name+" Created";
    }
    
    public UUID unrepeatedUUIDs() {
    	UUID out = UUID.randomUUID();
    	if (gmap.get(out) == null) return out;
    	else return unrepeatedUUIDs();
    }
    
    /**
    *calls update member.
    *
    *@return returns a single string of what was executed
    *
    *@param guildID the guild's unique ID
    *@param playerID the player's UniqueID
    *@param rank the rank to be assigned.  -1 is reserved for invited players while values >= 0 are ranks.
    */
    public String addMember(UUID guildID, UUID playerID, int rank) {return updateMember(guildID, playerID, rank);}
    
    /**
    *looks for the players record for the given guildID and sets the rank to the value entered.  Rank
    * values are checked to confirm the guild has a rank at that value.  if not, this method returns
    * a string indicating that rank value as invalid.
    *
    *@return returns a single string of waht was exectued
    *
    *@param guildID the guild's unique ID
    *@param playerID the player's UniqueID
    *@param rank the rank to be assigned.  -1 is reserved for invited players while values > 0 are ranks.
    */
    public String updateMember(UUID guildID, UUID playerID, int rank) {
    	if (gmap.get(guildID).permLevels.getOrDefault(rank, "N/A").equalsIgnoreCase("N/A")) return "Rank Locked";
    	if (gmap.get(guildID).members.getOrDefault(playerID, -2) == -2) return "Member Not Found";
    	gmap.get(guildID).members.put(playerID, rank);
    	return "Member Rank Updated";
    }
    
    /**
    *removes the given player's record for the guild specified.
    *
    *@return returns a single string of what was exectued
    *
    *@param guildID the guild's unique ID
    *@param playerID the player's UniqueID
    */
    public String removeMember(UUID guildID, UUID playerID) {
    	if (gmap.get(guildID).members.remove(playerID) != null) return "Player Removed";
    	else return "Player Not Found In Guild";
    }
    
    /**
    *obtains all member records for a given guild.  this also includes invited players.
    *
    *@return returns a map of all players in the guild
    *
    *@param guildID the guild's unique ID
    */
    public Map<UUID, Integer> getMembers(UUID guildID) {return gmap.get(guildID).members;}
    
    /**
    *gets the record for the given guild ID and returns it as a Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided
    *
    *@param guildID the guild's unique ID
    */
    public Guild getGuildByID(UUID guildID) {return gmap.get(guildID);}
    
    /**
    *gets the record for the given guild by Name and returns it as a Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided. null if unable to find.
    *
    *@param guildName the guild's textual name
    */
    @Nullable
    public Guild getGuildByName(String guildName) {
    	if (gmap.size() > 0) {
    		for (Map.Entry<UUID, Guild> entry : gmap.entrySet()) {
    			if (entry.getValue().name.equalsIgnoreCase(guildName)) return entry.getValue();
    		}
    	}
    	return null;
    }
    
    /**
    *locates a member record with a rank value not equal to -1, then passes the 
    * associated GuildID field to getGuildByID() and returns the Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided. null if unable to find.
    *
    *@param playerID the member's UniqueID
    */
    @Nullable
    public Guild getGuildByMember(UUID playerID) {
    	if (gmap.size() > 0) {
    		for (Map.Entry<UUID, Guild> entry : gmap.entrySet()) {
    			if (entry.getValue().members.get(playerID) != null) return entry.getValue();
    		}
    	}
    	return null;
    }
    
    /**
    *uses the SQL builder to build an UPDATE statement that includes only the value of
    * the fields included in the List object parameter.
    *
    *@return returns a single string of the execution result
    *
    *@param changes the list of enumerated items being updated. all other fields are ignored
    *@param guild the guild object whose ID is being reference and whose values are being passed to the SQL builder
    */
    public String setGuild(Guild guild, List<guildUpdates> changes) {
    	Guild ogg = gmap.get(guild.guildID);
    	for (int i = 0; i < changes.size(); i++) {
    		switch (changes.get(i)) {
    		case NAME: { ogg.name = guild.name;	break; }
    		case OPEN :{ ogg.open = guild.open; break; }
    		case TAX: { ogg.tax = guild.tax; break;}
    		case PERMLVLS: { ogg.permLevels = guild.permLevels; break;}
    		case PERMS: { ogg.permissions = guild.permissions; break;}
    		case MEMBERS: { ogg.members = guild.members; break;}
    		default:
    		}
    	}
    	gmap.put(ogg.guildID, ogg);
    	return "";
    }
    
    public enum guildUpdates {NAME, OPEN, TAX, PERMLVLS, PERMS, MEMBERS}
    
    /**
    *obtains the provided guilds highest current rank in the table, then creates a new record with
    * a rank value 1 greater and using the title provided.
    *
    *@return returns a single string of the execution result
    *
    *@param guildID the guild's unique ID
    *@param title The name this rank will display
    */
    public String addRank(UUID guildID, String title) {
    	int highest = 0;
    	while (highest >= 0) {
    		highest++;
    		if (gmap.get(guildID).permLevels.get(highest) == null) break;
    	}
    	gmap.get(guildID).permLevels.put(highest, title);
    	return "Rank Added";
    }
    
    /**
    *assigns the provided title to the rank specified for the guild specified
    *
    *@return returns a single string of the execution result
    *
    *@param guildID the guild's unique ID
    *@param rank the rank number to be modified
    *@param newTitle the Title to replace the existing one.
    */
    public String setRankTitle(UUID guildID, int rank, String newTitle) {
    	if (gmap.get(guildID).permLevels.getOrDefault(rank, "N/A").equalsIgnoreCase("N/A")) return "Rank Locked";
    	gmap.get(guildID).permLevels.put(rank, newTitle);
    	return "Rank Updated";
    }
    
    /**
    *@return a Map of the ranks for the guild
    *
    *@param guildID the guild's unique ID
    */
    public Map<Integer, String> getRanks(UUID guildID) {return gmap.get(guildID).permLevels;}
    
    public int getBottomRank(UUID guildID) {
    	int highest = 0;
    	while (highest >= 0) {
    		highest++;
    		if (gmap.get(guildID).permLevels.get(highest) == null) break;
    	}
    	return highest;
    }

    /**
    * used to add new permission records to a new guild.  should not be called elsewhere.
    *
    *@return returns a string of the result (for use in logger/system.out)
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission as used in other methods
    *@param value the rank value of the permission (Default = 1)
    */
    protected String addPermission(UUID guildID, permKey permTag, int value) { return setPermission(guildID, permTag, value); }
    
    /**
    *changes the rank value of a specific permission
    *
    *@return resturns a string of the result
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission to be changed
    *@param value the rank value of the permission. must be >= 0
    */
    public String setPermission(UUID guildID, permKey permTag, int value) {
    	gmap.get(guildID).permissions.put(permTag, value);
    	return "Permission Set";
    }
    
    /**
    *@return gets the rank value for a specific permission
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission queried
    */
    public int getPermission(UUID guildID, permKey permTag) { return gmap.get(guildID).permissions.getOrDefault(permTag, -2); }
    
    /**
    *gets all permissions and values for a specific guild
    *
    *@return a Map object of the permissions with their values
    *
    *@param guildID the id of the guild being queried.
    */
    public Map<permKey, Integer> getPermissions(UUID guildID) {return gmap.get(guildID).permissions;}

    //GAME LOGIC SECTION
    public String joinGuild(UUID guildID, UUID playerID) {
    	addMember(guildID, playerID, getBottomRank(guildID));
    	return "You have Joined "+gmap.get(guildID).name;
    }
    
    public String createNewGuild(String name, UUID playerID, boolean isAdmin) {
    	GnC.aMgr.changeBalance(playerID, -1 * Config.GUILD_CREATE_COST.get());
    	createGuild(name, isAdmin);
    	addMember(getGuildByName(name).guildID, playerID, 0);
    	return "Guild "+name+" Created";
    }
    
    public String rejectInvite(UUID guildID, UUID playerID) {
    	gmap.get(guildID).members.remove(playerID);
    	return "Rejected invite from "+gmap.get(guildID).name;
    }
    
    public List<String> getInvitedGuilds(UUID playerID) {
    	List<String> list = new ArrayList<String>();
    	for (Map.Entry<UUID, Guild> entry : gmap.entrySet()) {
    		if (entry.getValue().members.getOrDefault(playerID, -2) == -1) list.add(entry.getValue().name);
    	}
    	return list;
    }
    
    public List<String> getOpenGuilds(UUID playerID) {
    	List<String> list = new ArrayList<String>();
    	for (Map.Entry<UUID, Guild> entry : gmap.entrySet()) {
    		if (entry.getValue().open) list.add(entry.getValue().name);
    	}
    	return list;
    }
    
    public void applyTaxes() {
    	if (gmap.size() == 0) return;
    	for (Map.Entry<UUID, Guild> guilds : gmap.entrySet()) {
    		//1st apply member taxes
    		for (Map.Entry<UUID, Integer> mbrs : guilds.getValue().members.entrySet()) {
    			if (mbrs.getValue() >= 0) {
    				double change = (GnC.aMgr.getBalance(mbrs.getKey()) * guilds.getValue().tax);
	    			GnC.aMgr.changeBalance(mbrs.getKey(), (-1 * change));
	    			GnC.aMgr.changeBalance(guilds.getKey(), change);
    			}
    		}
    		//2nd apply guild taxes
    		double debt = MarketWSD.get(server.getWorld(World.field_234918_g_)).getDebt().getOrDefault(guilds.getKey(), 0d);
    		double taxes = taxableWorth(guilds.getKey());
    		double balG = GnC.aMgr.getBalance(guilds.getKey());
			if (taxes >= balG) {
				taxes -= balG;
				GnC.aMgr.setBalance(guilds.getKey(), 0);
				MarketWSD.get(server.getWorld(World.field_234918_g_)).getDebt().put(guilds.getKey(), taxes + debt);
				continue;
			}
			else if (taxes <= balG) {
				GnC.aMgr.changeBalance(guilds.getKey(), (-1 * taxes));
				balG -= taxes;
				if (debt <= balG) {
					GnC.aMgr.changeBalance(guilds.getKey(), (-1 * debt));
					MarketWSD.get(server.getWorld(World.field_234918_g_)).getDebt().remove(guilds.getKey());
					continue;
				}
				else {
					debt -= balG;
					GnC.aMgr.setBalance(guilds.getKey(), 0);
					MarketWSD.get(server.getWorld(World.field_234918_g_)).getDebt().put(guilds.getKey(), debt);
					continue;
				}
			}		
    	}
    }
    
    public void printTaxInfo() {
    	Map<UUID, Double> debt = MarketWSD.get(server.getWorld(World.field_234918_g_)).getDebt();
    	for (Map.Entry<UUID, Double> map : debt.entrySet()) {
    		String notice = "Debt for: "+ gmap.get(map.getKey()).name +" = $"+ df.format(map.getValue());
    		DecimalFormat pf = new DecimalFormat("#0.00");
    		double percent = map.getValue() / (guildWorth(map.getKey())/2);
    		String detail = "Percent to Bankruptcy: "+pf.format(percent)+"%";
    		for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
    			server.sendMessage(new StringTextComponent(notice), player.getUniqueID());
    			server.sendMessage(new StringTextComponent(detail), player.getUniqueID());
    		}
    	}
    }
    
    public double taxableWorth(UUID guildID) {
    	int memberCount = 0;
    	for (Map.Entry<UUID, Integer> mbrs : gmap.get(guildID).members.entrySet()) {if (mbrs.getValue() >= 0) memberCount++;}
    	int taxableLand = landCoreCount(guildID) - (memberCount*Config.CHUNKS_PER_MEMBER.get());
    	taxableLand = taxableLand > 0 ? taxableLand + landOutpostCount(guildID) : landOutpostCount(guildID);
    	double proportion = taxableLand == 0 ? 0 : taxableLand/(landCoreCount(guildID)+landOutpostCount(guildID));
    	return (guildWorth(guildID) * proportion);
    }
    
    public double guildWorth(UUID guildID) {
    	double totalValue = 0;
    	for (Map.Entry<RegistryKey<World>, ChunkManager> wld : GnC.ckMgr.entrySet()) {
	    	for (Map.Entry<ChunkPos, ChunkData> map : WorldWSD.get(server.getWorld(wld.getKey())).getChunks().entrySet()) {
	    		if (map.getValue().owner.equals(guildID)) totalValue += map.getValue().price;
	    	}
    	}
    	return totalValue;
    }
    
    private int landCoreCount(UUID guildID) {
    	int count = 0;
    	for (Map.Entry<RegistryKey<World>, ChunkManager> wld : GnC.ckMgr.entrySet()) {
	    	for (Map.Entry<ChunkPos, ChunkData> map : WorldWSD.get(server.getWorld(wld.getKey())).getChunks().entrySet()) {
	    		if (map.getValue().owner.equals(guildID) && !map.getValue().isOutpost) count++;
	    	}
    	}
    	return count;
    }
    
    private int landOutpostCount(UUID guildID) {
    	int count = 0;
    	for (Map.Entry<RegistryKey<World>, ChunkManager> wld : GnC.ckMgr.entrySet()) {
	    	for (Map.Entry<ChunkPos, ChunkData> map : WorldWSD.get(server.getWorld(wld.getKey())).getChunks().entrySet()) {
	    		if (map.getValue().owner.equals(guildID) && map.getValue().isOutpost) count++;
	    	}
    	}
    	return count;
    }
}
