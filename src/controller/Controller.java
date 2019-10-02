package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

import model.CityLodge;
import model.database.CityLodgeDB;
import model.exceptions.DatabaseException;

public class Controller {

	private CityLodgeDB database;
	private CityLodge citylodge;

	private final String DB_NAME = "CityLodgeDB";

	public Controller() {

		//Initialise CityLodge model
		this.citylodge = new CityLodge();
		
		// Initialise and connect Database
		try {
			this.database = new CityLodgeDB();
			// Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Connection to database successful");

		} catch (Exception e) {
			// Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Connection to database failed");
			e.printStackTrace();
		}
	}

	public void addRoomTable() throws DatabaseException {

		// use try-with-resources Statement
		try (Connection connection = database.getConnection();
				Statement thisStatement = connection.createStatement();) {

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

		try (Connection connection = database.getConnection();
				Statement thisStatement = connection.createStatement();) {

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

		try (Connection connection = database.getConnection();
				Statement thisStatement = connection.createStatement();) {

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

		try (Connection connection = database.getConnection();
				Statement thisStatement = connection.createStatement();) {

			result = thisStatement.executeQuery("SELECT id, title, author, submission_date FROM ROOMS");

			while (result.next()) {
				System.out.println(
						result.getInt("id") + " | " + result.getString("title") + " | " + result.getString("author"));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void dropAllTables() {

		ResultSet result = null;

		try (Connection connection = database.getConnection();
				Statement thisStatement = connection.createStatement();) {

			result = thisStatement.executeQuery("DROP SCHEMA PUBLIC CASCADE");
			System.out.println("Tables dropped");

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

}
