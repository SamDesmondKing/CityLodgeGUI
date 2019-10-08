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
//TODO saveRooms()

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
				//addRoomDB() TODO
				
			} else if (result.get() == buttonTwo) {
				this.citylodge.addSuite();
				//addRoomDB() TODO
			}

		// Rent room
		} else if (menuInput == 2) {
			this.citylodge.callRentRoom();
			//saveRooms() TODO

		// Return room
		} else if (menuInput == 3) {
			this.citylodge.callReturnRoom();
			//saveRooms() TODO

		// Begin maintenance
		} else if (menuInput == 4) {
			this.citylodge.callPerformMaintenance();
			//saveRooms() TODO
			
		// End maintenance
		} else if (menuInput == 5) {
			this.citylodge.callCompleteMaintenance();
			//saveRooms() TODO
			
		// Export data TODO
		} else if (menuInput == 6) {
			this.citylodge.displayRooms();

		// Import data TODO
		} else if (menuInput == 7) {
			this.citylodge.displayRooms();
		
		} 
	}
}
