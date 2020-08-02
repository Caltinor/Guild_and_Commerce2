package dicemc.gnc.guild;

import java.util.UUID;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class GuildManager {
	private Map<UUID, Guild> gmap = new HashMap<UUID, Guild>();
	
	public GuildManager() {}

    /**
    *creates a new record in the map for a guild with default
    *values and the name provided.
    *
    *@return returns a single string of what was executed
    *
    *@param name the guild name as it should appear textually in game
    */
    public String createGuild(String name) {
    	UUID id = unrepeatedUUIDs();
    	gmap.put(id, new Guild(name, unrepeatedUUIDs()));
    	return "Guild Created";
    }
    
    public UUID unrepeatedUUIDs() {
    	UUID out = UUID.randomUUID();
    	if (gmap.get(out) == null) return out;
    	else return unrepeatedUUIDs();
    }
    
    /**
    *this first checks that no record exists for the provided guildID 
    * and playerID.  if none exists, a new record is inserted with the provided
    * value.  This method also confirms that the guild has a rank value for the
    * rank entered.
    *
    *@return returns a single string of what was executed
    *
    *@param guildID the guild's unique ID
    *@param playerID the player's UniqueID
    *@param rank the rank to be assigned.  -1 is reserved for invited players while values > 0 are ranks.
    */
    public String addMember(UUID guildID, UUID playerID, int rank) {return "";}
    
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
    public String updateMember(UUID guildID, UUID playerID, int rank) {return "";}
    
    /**
    *removes the given player's record for the guild specified.
    *
    *@return returns a single string of what was exectued
    *
    *@param guildID the guild's unique ID
    *@param playerID the player's UniqueID
    */
    public String removeMember(UUID guildID, UUID playerID) {return "";}
    
    /**
    *obtains all member records for a given guild.  this also includes invited players.
    *
    *@return returns a map of all players in the guild
    *
    *@param guildID the guild's unique ID
    */
    public Map<UUID, Integer> getMembers(int guildID) {return new HashMap<UUID, Integer>();}
    
    /**
    *gets the record for the given guild ID and returns it as a Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided
    *
    *@param guildID the guild's unique ID
    */
    public Guild getGuildByID(UUID guildID) {return new Guild("", guildID);}
    
    /**
    *gets the record for the given guild by Name and returns it as a Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided. null if unable to find.
    *
    *@param guildName the guild's textual name
    */
    @Nullable
    public Guild getGuildByName(String guildName) {return null;}
    
    /**
    *locates a member record with a rank value not equal to -1, then passes the 
    * associated GuildID field to getGuildByID() and returns the Guild object
    *
    *@returns a {@link dicemc.gnc.guild.Guild} object for the guild ID provided. null if unable to find.
    *
    *@param playerID the member's UniqueID
    */
    @Nullable
    public Guild getGuildByMember(UUID playerID) {return null;}
    
    /**
    *uses the SQL builder to build an UPDATE statement that includes only the value of
    * the fields included in the List object parameter.
    *
    *@return returns a single string of the execution result
    *
    *@param changes the list of enumerated items being updated. all other fields are ignored
    *@param guild the guild object whose ID is being reference and whose values are being passed to the SQL builder
    */
    public String setGuild(Guild guild, List<guildUpdates> changes) {return "";}
    
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
    public String addRank(UUID guildID, String title) {return "";}
    
    /**
    *assigns the provided title to the rank specified for the guild specified
    *
    *@return returns a single string of the execution result
    *
    *@param guildID the guild's unique ID
    *@param rank the rank number to be modified
    *@param newTitle the Title to replace the existing one.
    */
    public String setRankTitle(UUID guildID, int rank, String newTitle) {return "";}
    
    /**
    *@return a Map of the ranks for the guild
    *
    *@param guildID the guild's unique ID
    */
    public Map<Integer, String> getRanks(UUID guildID) {return new HashMap<Integer, String>();}
    
    /**
    * used to add new permission records to a new guild.  should not be called elsewhere.
    *
    *@return returns a string of the result (for use in logger/system.out)
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission as used in other methods
    *@param value the rank value of the permission (Default = 1)
    */
    protected String addPermission(UUID guildID, String permTag, int value) {return "";}
    
    /**
    *changes the rank value of a specific permission
    *
    *@return resturns a string of the result
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission to be changed
    *@param value the rank value of the permission. must be >= 0
    */
    public String setPermission(UUID guildID, String permTag, int value) {return "";}
    
    /**
    *@return gets the rank value for a specific permission
    *
    *@param guildID the id of the guild.
    *@param permTag the tag for the permission queried
    */
    public int getPermission(UUID guildID, String permTag) {return 0;}
    
    /**
    *gets all permissions and values for a specific guild
    *
    *@return a Map object of the permissions with their values
    *
    *@param guildID the id of the guild being queried.
    */
    public Map<String, Integer> getPermissions(UUID guildID) {return new HashMap<String, Integer>();}
}
