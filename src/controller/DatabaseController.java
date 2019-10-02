package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import model.database.CityLodgeDB;
import model.exceptions.DatabaseException;

//TODO: addRoomLine
//TODO: addSuiteLine
//TODO: addHiringRecordLine
//TODO: ShowAllRooms
//TODO: ShowAllSuites
//TODO: ShowAllHiringRecords
//TODO: AddTestData - run this in the main method to populate with test data. 

public class DatabaseController {

	private final String DB_NAME = "CityLodgeDB";
	private CityLodgeDB DB;

	public DatabaseController(CityLodgeDB DB) {
		this.DB = DB;

	}

	public void addRoomTable() throws DatabaseException {

		// use try-with-resources Statement
		try (Connection connection = DB.getConnection(); Statement thisStatement = connection.createStatement();) {

			int result = thisStatement.executeUpdate(
					"CREATE TABLE ROOMS (id INT NOT NULL, title VARCHAR(50) NOT NULL, author VARCHAR(20) NOT NULL, submission_date DATE, PRIMARY KEY (id))");

			if (result == 0) {
				JOptionPane.showMessageDialog(null, "Table ROOMS has been created successfully");
			} else {
				throw new DatabaseException("Table not created");
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	public void addSuiteTable() throws DatabaseException {

		try (Connection connection = DB.getConnection(); Statement thisStatement = connection.createStatement();) {

			int result = thisStatement.executeUpdate(
					"CREATE TABLE tutorials_tbl (id INT NOT NULL, title VARCHAR(50) NOT NULL, author VARCHAR(20) NOT NULL, submission_date DATE, PRIMARY KEY (id))");

			if (result == 0) {
				JOptionPane.showMessageDialog(null, "Table SUITE has been created successfully");
			} else {
				throw new DatabaseException("Table not created");
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	public void addHiringRecordTable() throws DatabaseException {

		try (Connection connection = DB.getConnection(); Statement thisStatement = connection.createStatement();) {

			int result = thisStatement.executeUpdate(
					"CREATE TABLE tutorials_tbl (id INT NOT NULL, title VARCHAR(50) NOT NULL, author VARCHAR(20) NOT NULL, submission_date DATE, PRIMARY KEY (id))");

			if (result == 0) {
				JOptionPane.showMessageDialog(null, "Table HIRINGRECORDS has been created successfully");
			} else {
				throw new DatabaseException("Table not created");
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	public void showRoomTable() {
		
		ResultSet result = null;
		
		try (Connection connection = DB.getConnection(); Statement thisStatement = connection.createStatement();) {
	      
	         result = thisStatement.executeQuery("SELECT id, title, author, submission_date FROM ROOMS");
	         
	         while(result.next()){
	            System.out.println(result.getInt("id")+" | "+
	               result.getString("title")+" | "+
	               result.getString("author"));
	         }
	      } catch (Exception e) {
	         e.printStackTrace(System.out);
	      }
	}

	public void dropAllTables() {

		ResultSet result = null;

		try (Connection connection = DB.getConnection(); Statement thisStatement = connection.createStatement();) {

			result = thisStatement.executeQuery("DROP SCHEMA PUBLIC CASCADE");
			System.out.println("Tables dropped");

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

}
