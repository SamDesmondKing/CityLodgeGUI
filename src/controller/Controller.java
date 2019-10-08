package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.CityLodge;
import model.database.CityLodgeDB;
import view.AlertMessage;

//TODO addRoomDB()
//TODO removeRoomDB()
//TODO writeDatabaseToCityLodge()
//TODO handleClickEvents() etc. 

public class Controller {

	private CityLodgeDB database;
	private CityLodge citylodge;

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

	public void handleClickEvents(int menuInput) {

			// Add room
		if (menuInput == 1) {

			ButtonType buttonOne = new ButtonType("Room");
			ButtonType buttonTwo = new ButtonType("Suite");
			AlertMessage roomOrSuite = new AlertMessage(AlertType.CONFIRMATION, "Please choose room or suite",
					buttonOne, buttonTwo);
			Optional<ButtonType> result = roomOrSuite.showAndWait();

			if (result.get() == buttonOne) {
				this.citylodge.addRoom();
				
			} else if (result.get() == buttonTwo) {
				this.citylodge.addSuite();
			}

			// Rent room
		} else if (menuInput == 2) {

			this.citylodge.callRentRoom();

			// Return room
		} else if (menuInput == 3) {

			this.citylodge.callReturnRoom();

			// Begin maintenance
		} else if (menuInput == 4) {

			this.citylodge.callPerformMaintenance();

			// End maintenance
		} else if (menuInput == 5) {

			this.citylodge.callCompleteMaintenance();

			// Display rooms
		} else if (menuInput == 6) {
			this.citylodge.displayRooms();

		}

	}
}
