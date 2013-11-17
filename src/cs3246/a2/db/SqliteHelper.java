package cs3246.a2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Base SQLite helper class
 * @author jason
 *
 */
public class SqliteHelper {
	
	protected Connection mConnection;

	/**
	 * Instantiate SQL helper with a SQLite connection
	 * @param dbName relative/absolute db filename
	 * @throws ClassNotFoundException
	 */
	public SqliteHelper(String dbName) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		mConnection = null;
		
		try {
			mConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	protected String generateCommaSeperatedString(List<Integer> ids) {
		if (ids.size()==0) {
			return "";
		}
		
		String s = "";
		for (int i=0; i<ids.size(); i++) {
			s += ids.get(i).toString();
			if (i < ids.size()-1) {
				s += ",";
			}
		}
		
		return s;
	}
	
}
