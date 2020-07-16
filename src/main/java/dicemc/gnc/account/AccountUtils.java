package dicemc.gnc.account;

public class AccountUtils {

    /**
    *This method checks if an account exists and adds one if it does not.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    */
    public static String accountExists(String ID) {return "";}
    
    /**
    *Returns the player or guild balance
    *
    *@return the balance of the account passed in the parameter, 0.0 if none exists
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    */
    public static double getBalance(String ID) {return 0.0;}
    
    /**
    *Overwrites the player/guild balance with the value passed
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param value the amount the account will be set to
    */
    public static String setBalance(String ID, double value) {return "";}
    
    /**
    *modifies the player/guild's existing balance by the amount passed.  negative
    *values decrease the balance, and positive values are added to the balance.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param value the amount the account will be changed by
    */
    public static String changeBalance(String ID, double value) {return "";}
    
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
    public static String transferFunds(String fromID, String toID, double value) {return "";}
    
    /**
    *internal method to create a new account.
    *
    *@return returns a single string of what was executed
    *
    *@param ID guild IDs should be Integer.toString() and player IDs should be UUID.toString()
    *@param balance the balance to be set initially
    */
    private static String addAccount(String ID, double balance) {return "":}
}
