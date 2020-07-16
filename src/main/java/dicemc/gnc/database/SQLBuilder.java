package dicemc.gnc.database;

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
		return SQL;
	}
	
	/**
	 * Constructs a SQL INSERT statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param fieldsAndValues a map of fields and all their associated values.  
	 * each map list should be in sequence with all keys.
	 * @return a complete SQL string.
	 */
	public static String buildINSERT(String table, Map<String, List<String>> fieldsAndValues) {
		return "";
	}
	
	/**
	 * Constructs a SQL UPDATE statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param fieldsAndValues
	 * @param conditions a SQL string for what follows "WHERE ".
	 * @return a complete SQL string
	 */
	public static String buildUPDATE(String table, Map<String, String> fieldsAndValues, String conditions) {
		return "";
	}
	
	/**
	 * Constructs a SQL DELETE statement for use with a {@link DatabaseManager}
	 * @param table a string of the table nominal
	 * @param conditions a SQL string for what follows "WHERE ".
	 * @return a complete SQL string
	 */
	public static String buildDELETE(String table, String conditions) {
		return "";
	}
}
