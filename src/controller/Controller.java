package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

import model.CityLodge;
import model.database.CityLodgeDB;
import model.exceptions.DatabaseException;

//TODO addRoomDB()
//TODO removeRoomDB()
//TODO writeDatabaseToCityLodge()
//TODO handleClickEvents() etc. 

public class Controller {

	private CityLodgeDB database;
	private CityLodge citylodge;

	private final String DB_NAME = "CityLodgeDB";

	public Controller(CityLodge citylodge, CityLodgeDB database) {

		this.citylodge = citylodge;
		this.database = database;
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
