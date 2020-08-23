package dicemc.gnc.account;

import java.text.DecimalFormat;
import java.util.UUID;

import dicemc.gnc.datastorage.wsd.MarketWSD;
import dicemc.gnc.setup.Config;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class AccountManager {
	private MinecraftServer server;
	private ServerWorld world;
	private DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
	
	public AccountManager() {}
	
	public void setServer(MinecraftServer server) {this.server = server; world = server.getWorld(World.field_234918_g_);}

    /**
    *This method checks if an account exists and adds one if it does not.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    */
    public String accountExists(UUID ID, boolean isPlayer) {
    	String str = "";
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		if (MarketWSD.get(world).getAccounts().containsKey(ID)) str = "Account Exists for "+ID.toString();
    		else if (isPlayer) str = addAccount(ID, Config.STARTING_FUNDS.get());
    		else if (!isPlayer) str = addAccount(ID, Config.GUILD_STARTING_FUNDS.get());
    	}
    	return str;
    }
    
    /**
    *Returns the player or guild balance
    *
    *@return the balance of the account passed in the parameter, 0.0 if none exists
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    */
    public double getBalance(UUID ID) {
    	double bal = 0d;
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		bal = MarketWSD.get(world).getAccounts().get(ID);
    	}
    	return bal;
    }
    
    /**
    *Overwrites the player/guild balance with the value passed
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param value the amount the account will be set to
    */
    public String setBalance(UUID ID, double value) {
    	String str = "";
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		MarketWSD.get(world).getAccounts().put(ID, value);
    		MarketWSD.get(world).markDirty();
    		str = "Player :"+server.getPlayerProfileCache().getProfileByUUID(ID).getName()+ " balance set to $" +df.format(value);
    	}
    	return str;
    }
    
    /**
    *modifies the player/guild's existing balance by the amount passed.  negative
    *values decrease the balance, and positive values are added to the balance.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param value the amount the account will be changed by
    */
    public String changeBalance(UUID ID, double value) {
    	String str = "";
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		double bal = getBalance(ID);
    		bal += value;
    		setBalance(ID, bal);
    		str = "Balance Changed";
    	}
    	return str;
    }
    
    /**
    *Transfers funds between accounts.  This can move between players, guilds, or a 
    *combination.  negative values are made absolute.
    *
    *@return returns a single string of what was executed
    *
    *@param fromID the account holder ID that funds are being pulled from.  guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param toID the account holder ID that funds are bing put into. guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param value the amount to be transferred (values are always absolute)
    */
    public String transferFunds(UUID fromID, UUID toID, double value) {
    	String str = "";
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		if (value <= getBalance(fromID)) {
    			changeBalance(fromID, -1 * value);
    			changeBalance(toID, value);
    			str = "$"+df.format(value)+" transferred from "+server.getPlayerProfileCache().getProfileByUUID(fromID).getName()+ " to "+server.getPlayerProfileCache().getProfileByUUID(toID).getName();
    		}
    		else str = "Insufficient funds in origin account";
    	}
    	return str;
    }
    
    /**
    *internal method to create a new account.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param balance the balance to be set initially
    */
    private String addAccount(UUID ID, double balance) {
    	String str = "";
    	if (Config.MARKET_USE_DB.get()) {
    		//TODO add DB logic
    	}
    	else {
    		MarketWSD.get(world).getAccounts().put(ID, balance);
    		MarketWSD.get(world).markDirty();
    	}
    	return str;
    }
}
