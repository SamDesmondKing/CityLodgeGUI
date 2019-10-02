package model.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.exceptions.DatabaseException;

public class CityLodgeDB {
	
	//TODO: Add query method
	//TODO: Add update method

	private Connection con;
	
	public CityLodgeDB(String db_file) throws Exception {

		final String DB_NAME = "CityLodgeDB";

		// Connect database		
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		this.con = DriverManager.getConnection("jdbc:hsqldb:file:" + db_file, "SA", "");
		
	}
	
	public void shutDown() throws SQLException {
		
		//Shuts down the database gracefully by excecuting SHUTDOWN command. 
		Statement statement = con.createStatement();
		statement.execute("SHUTDOWN");
		this.con.close();
		
	}
	
	public Connection getConnection() {
		return this.con;
	}


}