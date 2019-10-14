package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.CityLodge;
import model.DateTime;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.database.CityLodgeDB;
import view.AlertMessage;
import view.CustomChoiceDialog;
import view.CustomInputDialog;

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
				this.addRoom();
				// addRoomDB() TODO

			} else if (result.get() == buttonTwo) {
				this.addSuite(); 
				// addRoomDB() TODO
			}

			// Rent room 
		} else if (menuInput == 2) {
			this.callRentRoom();
			// saveRooms() TODO

			// Return room TODO
		} else if (menuInput == 3) {
			this.citylodge.callReturnRoom();
			// saveRooms() TODO

			// Begin maintenance TODO
		} else if (menuInput == 4) {
			this.citylodge.callPerformMaintenance();
			// saveRooms() TODO

			// End maintenance TODO
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

	// UPDATED - Takes and validates input to create Suite, adds to roomArray
	public void addSuite() {

		// Taking suiteID
		String suiteID = this.takeID("Suite");
		if (suiteID == null) {
			return;
		}
		// Taking feature summary
		String featureSummary = this.takeFeatureSummary();
		if (featureSummary == null) {
			return;
		}
		// Taking last maintenance date
		DateTime maintDate = null;
		CustomInputDialog maintDateDialog = new CustomInputDialog("Enter Last Maintenance Date", "Format dd/mm/yyyy: ");
		Optional<String> maintDateResult = maintDateDialog.showAndWait();

		if (maintDateResult.isPresent()) {
			try {
				maintDate = this.stringToDateTime(maintDateResult.get());
			} catch (Exception e) {
				AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Wrong Date Format",
						"Returning to main menu.");
				failure.showAndWait();
				return;
			}
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Maintenance Date Not Entered",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Validating last maintenance date
		DateTime today = new DateTime();
		if (DateTime.diffDays(today, maintDate) <= 0) {

			AlertMessage failure = new AlertMessage(AlertType.WARNING,
					"Error: Maintenance Date Cannot Be in the Future", "Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// If we made it to here Suite is good to go.
		Suite thisRoom = new Suite(suiteID, 6, featureSummary, maintDate);
		// Adding new Suite to roomArray.
		this.citylodge.addToArray(thisRoom);
	}

	// UPDATED - Verifies and adds room to citylodge room array
	public void addRoom() {

		// Taking RoomID
		String roomID = this.takeID("Standard Room");
		if (roomID == null) {
			return;
		}

		// Taking numBeds
		int numBeds = 0;
		ArrayList<String> numBedChoices = new ArrayList<>();
		numBedChoices.add("One");
		numBedChoices.add("Two");
		numBedChoices.add("Four");

		CustomChoiceDialog numBedsDialog = new CustomChoiceDialog("No. Beds", numBedChoices, "Select Number of Beds",
				"Selection: ");
		Optional<String> bedResult = numBedsDialog.showAndWait();

		if (bedResult.isPresent()) {
			if (bedResult.get() == "One") {
				numBeds = 1;
			} else if (bedResult.get() == "Two") {
				numBeds = 2;
			} else if (bedResult.get() == "Four") {
				numBeds = 4;
			}
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Room Creation Cancelled",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Taking feature summary
		String featureSummary = this.takeFeatureSummary();
		if (featureSummary == null) {
			return;
		}

		// If we made it to here the room is good to go.
		try {
			StandardRoom thisRoom = new StandardRoom(roomID, numBeds, featureSummary);
			citylodge.addToArray(thisRoom);
			AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room Added Successfully",
					"Returning to main menu.");
			success.showAndWait();
			return;
		} catch (Exception e) {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Room Adding Failed",
					"Room not created, returning to main menu.");
			failure.showAndWait();
			return;
		}
	}

	// UPDATED - Takes RoomID as search target, validates and calls rent() on chosen
	// Room.
	public void callRentRoom() {

		CustomInputDialog rentDialog = new CustomInputDialog(
				"Enter ID of Room to Rent\n\nStandard rooms have the format R_000\nSuites have the format S_000",
				"Room ID: ");
		Optional<String> roomResult = rentDialog.showAndWait();
		String roomID;

		if (roomResult.isPresent()) {
			roomID = roomResult.get();
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Rental process cancelled",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Checking room exists
		Room searchTarget = citylodge.searchRoomByID(roomID);

		if (searchTarget == null) {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Room Not Found",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Checking room availability
		if (!searchTarget.getRoomStatus().equals("Available")) {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Room Unavailable",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Taking customerID
		String customerID = null;
		CustomInputDialog customerIDDialog = new CustomInputDialog("Enter customer ID", "C_");
		Optional<String> customerResult = customerIDDialog.showAndWait();

		if (customerResult.isPresent()) {
			customerID = "C_" + customerResult.get().trim().toUpperCase();
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Customer Not Entered",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Taking rent date
		DateTime rentDate = null;
		CustomInputDialog rentDateDialog = new CustomInputDialog("Enter Rent Date", "Format dd/mm/yyyy: ");
		Optional<String> dateResult = rentDateDialog.showAndWait();

		if (dateResult.isPresent()) {
			try {
				rentDate = this.stringToDateTime(dateResult.get());
			} catch (Exception e) {
				AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Wrong Date Format",
						"Returning to main menu.");
				failure.showAndWait();
				return;
			}
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Rent Date Not Entered",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Taking numOfRentDays
		int rentDays = 0;
		CustomInputDialog rentDaysDialog = new CustomInputDialog("Enter Number of Rent Days", "Numeric values only: ");
		Optional<String> daysResult = rentDaysDialog.showAndWait();

		if (dateResult.isPresent()) {
			try {
				rentDays = Integer.parseInt(daysResult.get().trim());
			} catch (Exception e) {
				AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Wrong Day Format",
						"Returning to main menu.");
				failure.showAndWait();
				return;
			}
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Rent Days Not Entered",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}

		// Calling rent()
		if (searchTarget.rent(customerID, rentDate, rentDays)) {
			String roomMessage = "Room " + searchTarget.getRoomID() + " is now rented by customer " + customerID;
			AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room Rented Successfully!", roomMessage);
			success.showAndWait();
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Error: Room Renting Failed",
					"Returning to main menu.");
			failure.showAndWait();
			return;
		}
	}

	// UPDATED - Checks String is less than 20 words by counting spaces.
	public boolean checkFeatureSummary(String featureSummary) {

		int spaceCount = 0;
		for (char i : featureSummary.toCharArray()) {
			if (i == ' ') {
				spaceCount++;
			}
		}

		if (spaceCount > 19) {
			return false;
		} else {
			return true;
		}
	}

	// UPDATED - Takes date as string and converts it to DateTime, returns DateTime.
	public DateTime stringToDateTime(String dateString) {

		String intValue = dateString.replaceAll("[^0-9]", "");
		int day = Integer.parseInt(intValue.substring(0, 2));
		int month = Integer.parseInt(intValue.substring(2, 4));
		int year = Integer.parseInt(intValue.substring(4, 8));
		DateTime Date = new DateTime(day, month, year);

		return Date;

	}

	public String takeFeatureSummary() {

		CustomInputDialog featDialog = new CustomInputDialog("Enter feature summary (20 words or less)", "");
		Optional<String> featResult = featDialog.showAndWait();
		String featureSummary = null;

		if (featResult.isPresent()) {
			featureSummary = featResult.get();
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Room Creation Cancelled",
					"Returning to main menu.");
			failure.showAndWait();
			return featureSummary;
		}

		// Feature summary validation (20 words or less)
		if (!this.checkFeatureSummary(featureSummary)) {
			featureSummary = null;
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Feature Summary Invalid",
					"Room not created, returning to main menu.");
			failure.showAndWait();
		}

		return featureSummary;
	}

	public String takeID(String type) {

		// Taking RoomID
		String roomID = null;
		String typeCode = null;
		
		if (type == "Standard Room") {
			typeCode = "R_";
		} else if (type == "Suite") {
			typeCode = "S_";
		}

		CustomInputDialog roomIDDialog = new CustomInputDialog("Enter " + type + " ID" + "\nLeave blank for random ID", typeCode);
		Optional<String> result = roomIDDialog.showAndWait();

		if (result.isPresent()) {
			if (type == "Standard Room") {
				roomID = "R_" + result.get().trim().toUpperCase();
				if (result.get().trim().isEmpty()) {
					Random random = new Random();
					int roomNo = random.nextInt(999);
					roomID = "R_" + roomNo;
				}
			} else if (type == "Suite") {
				roomID = "S_" + result.get().trim().toUpperCase();
				if (result.get().trim().isEmpty()) {
					Random random = new Random();
					int roomNo = random.nextInt(999);
					roomID = "S_" + roomNo;
				}
			}
		} else {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Room Creation Cancelled",
					"Room not created, returning to main menu.");
			failure.showAndWait();
			return roomID;
		}

		// RoomID uniqueness check
		if (!this.citylodge.checkRoomID(roomID)) {
			roomID = null;
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Duplicate Room ID",
					"Room not created, returning to main menu.");
			failure.showAndWait();
			return roomID;
		}
		
		AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room ID set to " + roomID,"");
		success.showAndWait();
		return roomID;
	}
}
