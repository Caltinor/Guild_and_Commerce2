package dicemc.gnc.datastorage.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import dicemc.gnc.setup.Config;

public class DatabaseManager {
	private Statement stmt;
	private Connection con;
	private String suffix;
	public TableDefinitions td;
	
	public DatabaseManager(boolean usePrimary) {
		String port = usePrimary ? Config.DB_PORT.get() : Config.DB2_PORT.get();
		String name = usePrimary ? Config.DB_NAME.get() : Config.DB2_NAME.get();
		String url  = usePrimary ? Config.DB_URL.get() : Config.DB2_URL.get();
		String host = "jdbc:mysql://" + url + ":" + port + "/" + name;;
		String user = usePrimary ? Config.DB_USER.get() : Config.DB2_USER.get();
		String pass = usePrimary ? Config.DB_PASS.get() : Config.DB2_PASS.get();
		suffix = usePrimary ? Config.DB_SUFFIX.get() : Config.DB2_SUFFIX.get();
		
		td = new TableDefinitions(suffix);
		try {
			System.out.println("Attempting DB Connection");
			con = DriverManager.getConnection(host, user, pass);
			stmt = con.createStatement();
			System.out.println("DB Connection Successful");
		} catch (SQLException e) {e.printStackTrace();}	
		for (Map.Entry<String, String> entry : td.tableDef.entrySet()) {
			//TODO add a check in table definitions for whether a table should be checked on DB1 or DB2
			confirmTable(con, entry);
		}
	}
	
	private void confirmTable(Connection con, Map.Entry<String, String> tbl) {
		try {
			Statement st = con.createStatement();
			DatabaseMetaData dbm = con.getMetaData();
			ResultSet rs = dbm.getTables(null, null, tbl.getKey(), null);
			if (rs.isBeforeFirst()) {return; } else {
				String sqlSTR = "CREATE TABLE "+ tbl.getKey() + tbl.getValue();
				System.out.println(sqlSTR);
				st.executeUpdate(sqlSTR);			
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	public ResultSet executeQuery(String sqlSTR) {
		try {return stmt.executeQuery(sqlSTR);} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
	public int executeUpdate(String sqlSTR) {
		try {return stmt.executeUpdate(sqlSTR);} catch (SQLException e) {e.getStackTrace();}
		return 0;
	}
}
