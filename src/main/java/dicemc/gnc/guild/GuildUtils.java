package dicemc.gnc.guild;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

public class GuildUtils {

    public static String createGuild(String name) {return "";}
    
    public static String addMember(int guildID, UUID playerID, int rank) {return "";}
    
    public static String updateMember(int guildID, UUID playerID, int rank) {return "";}
    
    public static String removeMember(int guildID, UUID playerID) {return "";}
    
    public static Map<UUID, Integer> getMembers(int guildID) {return new HashMap<UUID, Integer>();}
    
    public static Guild getGuild(int guildID) {return new Guild("");}
    
    public static String setGuild(int guildID, Map<String, String> changes) {return "";}
    
    public static String addRank(int guildID, String title) {return "";}
    
    public static String setRankTitle(int guildID, int rank, String newTitle) {return "";}
    
    public static Map<Integer, String> getRanks(int guildID) {return new HashMap<Integer, String>();}
    
    public static String addPermission(int guildID, String permTag, int value) {return "";}
    
    public static String setPermission(int guildID, String permTag, int value) {return "";}
    
    public static int getPermission(int guildID, String permTag) {return 0;}
    
    public static Map<String, Integer> getPermissions(int guildID) {return new HashMap<String, Integer>();}
}
