package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import model.CityLodge;
import model.StandardRoom;
import model.database.CityLodgeDB;
import view.AlertMessage;
import view.CustomDialog;

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
			AlertMessage roomOrSuite = new AlertMessage(AlertType.CONFIRMATION, "CityLodge",
					"Please choose room or suite", buttonOne, buttonTwo);
			Optional<ButtonType> result = roomOrSuite.showAndWait();

			if (result.get() == buttonOne) {
				this.addRoom();

			} else if (result.get() == buttonTwo) {
				this.citylodge.addSuite();
				// addRoomDB() TODO
			}

			// Rent room
		} else if (menuInput == 2) {
			this.citylodge.callRentRoom();
			// saveRooms() TODO

			// Return room
		} else if (menuInput == 3) {
			this.citylodge.callReturnRoom();
			// saveRooms() TODO

			// Begin maintenance
		} else if (menuInput == 4) {
			this.citylodge.callPerformMaintenance();
			// saveRooms() TODO

			// End maintenance
		} else if (menuInput == 5) {
			this.citylodge.callCompleteMaintenance();
			// saveRooms() TODO

			// Export data TODO
		} else if (menuInput == 6) {
			this.citylodge.displayRooms();

			// Import data TODO
		} else if (menuInput == 7) {
			this.citylodge.displayRooms();

		}
	}

	public void addRoom() {

		// Taking RoomID
		String roomID = null;
		CustomDialog roomIDDialog = new CustomDialog("Adding Room","Enter room ID, beginning with 'R_'\nLeave empty to generate random ID.");
		Optional<String> result = roomIDDialog.showAndWait();
		
		if (result.isPresent()) {
			roomID = result.get().trim().toUpperCase();
			if (roomID.trim().isEmpty()) {
				Random random = new Random();
				int roomNo = random.nextInt(99999);
				roomID = "R_" + roomNo;
			}
		}

	
		/*
		

		// RoomID format check
		if (roomID.charAt(0) != 'R' || roomID.charAt(1) != '_') {
			System.out.println("Invalid room ID, room not created. Returning to main menu.");
			return;
		}

		// RoomID uniqueness check
		if (!citylodge.checkRoomID(roomID)) {
			System.out.println(
					"Error - a room with that RoomID already exists. Room not created, returning to main menu.");
			return;
		}

		
		// Taking numBeds
		System.out.println("Enter the number of beds: 1, 2 or 4");
		int numBeds = sc.nextInt();
		sc.nextLine();

		// numBeds validity check
		if (numBeds != 1 && numBeds != 2 && numBeds != 4) {
			System.out.println("Error: Invalid bed selection, returning to main menu.");
			sc.close();
			return;
		}

		// Taking feature summary
		System.out.println("Enter the feature summary:");
		String featureSummary = sc.nextLine().trim();

		// Feature summary validation (20 words or less)
		if (!this.checkFeatureSummary(featureSummary)) {
			System.out.println("Error: feature summary must be 20 words or less. Returning to main menu.");
			sc.close();
			return;
		}

		// If we made it to here the room is good to go.
		StandardRoom thisRoom = new StandardRoom(roomID, numBeds, featureSummary);

		// Adding new StandardRoom to roomArray.
		this.addToArray(thisRoom);

		*/
		// this.addRoomDB() TODO
	}

}
