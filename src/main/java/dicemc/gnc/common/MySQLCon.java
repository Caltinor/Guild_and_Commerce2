package dicemc.gnc.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLCon {
	private final String port = "3306";
	private final String name = "new_schema";
	private final String host = "jdbc:mysql://localhost:" + port + "/" + name;
	private final String user = "gncLink";
	private final String pass = "gncAdmin1!";
	
	private ResultSet rs;
	private Statement stmt;
	
	public MySQLCon(){
		try {
			Connection con = DriverManager.getConnection(host, user, pass);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM new_schema.tbl_test");
			System.out.println("DB Connection Successful");
		} catch (SQLException e) {e.printStackTrace();}		
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
