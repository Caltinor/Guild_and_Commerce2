package dicemc.gnc.testmaterial;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLCon {
	private final String port = Config.DB_PORT.get();
	private final String name = Config.DB_NAME.get();
	private final String url  = Config.DB_URL.get();
	private final String host = "jdbc:mysql://" + url + ":" + port + "/" + name;
	private final String user = Config.DB_USER.get();
	private final String pass = Config.DB_PASS.get();
	
	private ResultSet rs;
	private Statement stmt;
	private Connection con;
	private Map<String, String> tableRef = new HashMap<String, String>();
	
	public MySQLCon(){
		tableRef = defineTables();
		try {
			con = DriverManager.getConnection(host, user, pass);
			stmt = con.createStatement();
			System.out.println("DB Connection Successful");
		} catch (SQLException e) {e.printStackTrace();}	
		for (Map.Entry<String, String> entry : tableRef.entrySet()) {
			confirmTable(con, entry);
		}
	}		
	
	private void confirmTable(Connection con, Map.Entry<String, String> tbl) {
		try {
			Statement st = con.createStatement();
			DatabaseMetaData dbm = con.getMetaData();
			rs = dbm.getTables(null, null, tbl.getKey(), null);
			if (rs.next()) {return; } else {
				String sqlSTR = "CREATE TABLE "+ tbl.getKey() + tbl.getValue();
				System.out.println(sqlSTR);
				st.executeUpdate(sqlSTR);			
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	private Map<String, String> defineTables() {
		/*TODO: incorporate server world name into the table names
		 * 		This allows for use of the same database with different worlds
		 * 		this also allows for singleplayer use within the same DB
		 */
		Map<String, String> map = new HashMap<String, String>();
		String sqlSTR = "";
		String tblSTR = "";
		//Guild Table
		tblSTR = "tbl_guild";
		sqlSTR = " (GuildID INT NOT NULL AUTO_INCREMENT, " + 
				"Name VARCHAR(32) NOT NULL, " + 
				"Open TINYINT(1) NOT NULL, " + 
				"Tax DOUBLE NOT NULL, " + 
				"PRIMARY KEY (GuildID))";
		map.put(tblSTR, sqlSTR);
		//Other table
		//repeat
		//
		return map;
	}
	
	public List<String> getResults(){
		List<String> list = new ArrayList<String>();
		try {
			while(rs.next()) {
				list.add(rs.getString("Price") + " : " + rs.getString("Item"));
				System.out.println("record added");
			}
		} catch (SQLException e) {e.printStackTrace();}
		return list;
	}
	
	public void callSortedPriceList(boolean ASC) {
		String sqlSTR = "SELECT * FROM " + name + ".tbl_markets order by Price " + (ASC ? "ASC" : "DESC");
		System.out.println(sqlSTR);
		try {rs = stmt.executeQuery(sqlSTR);
		System.out.println("Markets Sorted");
		} catch (SQLException e) {e.printStackTrace();}
	}
}
