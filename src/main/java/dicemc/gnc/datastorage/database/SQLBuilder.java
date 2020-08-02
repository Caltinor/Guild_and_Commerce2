package dicemc.gnc.datastorage.database;

import java.util.List;
import java.util.Map;

public class SQLBuilder {

	/**
	 * Constructs a SQL SELECT statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param fields a list of all field names to be queried
	 * @param conditions a SQL string for what follows "WHERE ".
	 * @return a complete SQL string.
	 */
	public static String buildSELECT(String table, List<String> fields, String conditions) {
		String SQL = "SELECT ";
		for (int i = 0; i < fields.size(); i++) { 
			SQL += fields.get(i);
			if (i < fields.size() - 1) SQL += ", "; 
		}
		SQL += " FROM " + table;
		SQL += " WHERE " + conditions;
		System.out.println(SQL);
		return SQL;
	}
	
	/**
	 * Constructs a SQL INSERT statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param fieldsAndValues a map of fields and all their associated values.  
	 * each map list should be in sequence with all keys.
	 * @return a complete SQL string.
	 */
	public static String buildINSERT(String table, List<String> fields, List<List<String>> values) {
		String SQL = "INSERT INTO " + table + " (";
		String sub = "VALUES (";
		for (int i = 0; i < fields.size(); i++) {
			SQL += fields.get(i);
			SQL += (i == fields.size()-1) ? ")" : ", ";
		}
		for (int i = 0; i < values.size(); i++) {
			List<String> subList = values.get(i);
			for (int j = 0; j < subList.size(); j++) {
				sub += subList.get(j);
				sub += (j == subList.size()-1) ? ")" : ", ";
			}
			sub += (i == values.size()-1) ? "" : ", ";
		}
		SQL += " " + sub;
		return SQL;
	}
	
	/**
	 * Constructs a SQL UPDATE statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param fieldsAndValues
	 * @param conditions a SQL string for what follows "WHERE ".
	 * @return a complete SQL string
	 */
	public static String buildUPDATE(String table, List<String> fields, Map<String, String> Values, String conditions) {
		String SQL = "UPDATE " + table + " SET ";
		for (Map.Entry<String, String> field : Values.entrySet()) {
			SQL += field.getKey() + " = " + field.getValue() +", ";
		}
		SQL = SQL.length() > 1 ? SQL.substring(0, SQL.length() -2) : SQL;
		SQL += " WHERE " + conditions;
		return SQL;
	}
	
	/**
	 * Constructs a SQL DELETE statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param conditions a SQL string for what follows "WHERE ".
	 * @return a complete SQL string
	 */
	public static String buildDELETE(String table, String conditions) { 
		return "DELETE FROM "+ table + " WHERE " + conditions;
	}
}
