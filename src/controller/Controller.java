package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.CityLodge;
import model.DateTime;
import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.database.CityLodgeDB;
import model.exceptions.InvalidInputException;
import model.exceptions.MaintenanceException;
import model.exceptions.RentalException;
import model.exceptions.ReturnException;
import view.AlertMessage;
import view.CustomChoiceDialog;
import view.CustomInputDialog;

public class Controller {

	private CityLodgeDB database;
	private CityLodge citylodge;

	public Controller(CityLodge citylodge, CityLodgeDB database) {

		this.citylodge = citylodge;
		this.database = database;
	}

	public void mainMenu(int menuInput) {

		// Add room
		if (menuInput == 1) {
			int result = this.getRoomOrSuite();
			if (result == 1) {
				try {
					this.addRoom();
				} catch (InvalidInputException e) {
					e.getError();
				}
			} else if (result == 2) {
				try {
					this.addSuite();
				} catch (InvalidInputException e) {
					e.getError();
				}
			}
		// Rent room
		} else if (menuInput == 2) {
			try {
				this.callRentRoom();
			} catch (InvalidInputException e) {
				e.getError();
			} catch (RentalException e) {
				e.getError();
			}
		// Return room
		} else if (menuInput == 3) {
			try {
				this.callReturnRoom();
			} catch (InvalidInputException e) {
				e.getError();
			} catch (ReturnException e) {
				e.getError();
			}
		// Begin maintenance
		} else if (menuInput == 4) {
			try {
				this.callPerformMaintenance();
			} catch (InvalidInputException e) {
				e.getError();
			} catch (MaintenanceException e) {
				e.getError();
			}
		// End maintenance
		} else if (menuInput == 5) {
			try {
				this.callCompleteMaintenance();
			} catch (InvalidInputException e) {
				e.getError();
			} catch (MaintenanceException e) {
				e.getError();
			}
		// Quit
		} else if (menuInput == 8) {
			try {
				this.database.saveData();
				this.database.shutdown();
				System.exit(0);
			} catch (SQLException e) {
				System.out.println("SQL exception");
			}
		//Display for testing ****************************DELETE LATER
		} else if (menuInput == 9) {
			citylodge.displayRooms();
		}
	}

	// Searches for Room by ID, calls performMaintenance() on Room
	public void callPerformMaintenance() throws InvalidInputException, MaintenanceException {

		// Taking room ID
		String roomID = this.takeID();

		// Throws InvalidInputException
		Room thisRoom = citylodge.searchRoomByID(roomID);

		// Throws MaintenanceException
		thisRoom.performMaintenance();

		AlertMessage success = new AlertMessage(AlertType.INFORMATION,
				thisRoom.getRoomType() + " " + thisRoom.getRoomID() + " is now under maintenance.",
				"Returning to main menu.");
		success.showAndWait();
	}

	// Searches for Room by ID, takes completion date and calls
	// completeMaintenance() on Room
	public void callCompleteMaintenance() throws InvalidInputException, MaintenanceException {

		// Taking room ID
		String roomID = this.takeID();

		// Searchs for room by ID, throws InvalidInputException
		Room thisRoom = citylodge.searchRoomByID(roomID);

		// Throws InvalidInputException
		DateTime completionDate = this.takeDate("Completion");

		// Throws MaintenanceException
		thisRoom.completeMaintenance(completionDate);

		AlertMessage success = new AlertMessage(AlertType.INFORMATION,
				thisRoom.getRoomType() + " " + thisRoom.getRoomID()
						+ " has had all maintenance operations completed and is now ready for rent.",
				"Returning to main menu.");
		success.showAndWait();
	}

	// Calls return room on room or suite object
	public void callReturnRoom() throws InvalidInputException, ReturnException {

		// Taking roomID
		String roomID = this.takeID();

		// Getting room from citylodge array
		Room thisRoom = citylodge.searchRoomByID(roomID);

		// Validating roomID
		if (thisRoom == null) {
			throw new InvalidInputException("Error: Room Does Not Exist", "Returning to main menu.");
		}

		// Taking returnDate
		DateTime returnDate = this.takeDate("Return");

		// Calling returnRoom(), where final validation and updating will take place.
		thisRoom.returnRoom(returnDate);

		AlertMessage success = new AlertMessage(AlertType.INFORMATION,
				thisRoom.getRoomType() + " " + thisRoom.getRoomID() + " has been returned successfully.\n",
				"Returning to main menu.");
		success.showAndWait();
	}

	// Takes and validates input to create Suite, adds to roomArray
	public void addSuite() throws InvalidInputException {

		// Taking suiteID
		String suiteID = this.takeNewID("Suite");

		// Taking feature summary
		String featureSummary = this.takeFeatureSummary();

		// Taking last maintenance date
		DateTime maintDate = this.takeDate("Last Maintenance");

		// Validating last maintenance date
		DateTime today = new DateTime();
		if (DateTime.diffDays(today, maintDate) <= 0) {
			throw new InvalidInputException("Error: Maintenance Date Cannot Be in the Future",
					"Returning to main menu.");
		}

		// If we made it to here Suite is good to go.
		Suite thisRoom = new Suite(suiteID, 6, featureSummary, maintDate);
		// Adding new Suite to roomArray.
		this.citylodge.addToArray(thisRoom);
	}

	// Verifies and adds room to citylodge room array
	public void addRoom() throws InvalidInputException {

		// Taking RoomID - throws InvalidInputException
		String roomID = this.takeNewID("Standard Room");

		// Taking numBeds
		int numBeds = 0;
		ArrayList<String> numBedChoices = new ArrayList<>();
		numBedChoices.add("One");
		numBedChoices.add("Two");
		numBedChoices.add("Four");

		CustomChoiceDialog numBedsDialog = new CustomChoiceDialog("No. Beds", numBedChoices, "Select Number of Beds",
				"Selection: ");
		Optional<String> bedResult = numBedsDialog.showAndWait();

		if (bedResult.get() == "One") {
			numBeds = 1;
		} else if (bedResult.get() == "Two") {
			numBeds = 2;
		} else if (bedResult.get() == "Four") {
			numBeds = 4;
		} else {
			throw new InvalidInputException("Room Creation Cancelled", "Room not created, returning to main menu.");
		}

		// Taking feature summary - throws InvalidInputException
		String featureSummary = this.takeFeatureSummary();

		// If we made it to here the room is good to go.
		try {
			StandardRoom thisRoom = new StandardRoom(roomID, numBeds, featureSummary);
			citylodge.addToArray(thisRoom);
			AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room Added Successfully",
					"Returning to main menu.");
			success.showAndWait();
			return;
		} catch (Exception e) {
			throw new InvalidInputException("Error: Adding Room Failed", "Room not created, returning to main menu.");
		}
	}

	// Takes RoomID as search target, validates and calls rent() on chosen
	// Room.
	public void callRentRoom() throws InvalidInputException, RentalException {

		CustomInputDialog rentDialog = new CustomInputDialog(
				"Enter ID of Room to Rent\n\nStandard rooms have the format R_000\nSuites have the format S_000",
				"Room ID: ");
		Optional<String> roomResult = rentDialog.showAndWait();
		String roomID;

		if (roomResult.isPresent()) {
			roomID = roomResult.get();
		} else {
			throw new InvalidInputException("Error: ID Not Entered.", "Room not created, returning to main menu.");
		}

		// Checking room exists
		Room searchTarget = citylodge.searchRoomByID(roomID);

		if (searchTarget == null) {
			throw new InvalidInputException("Error: Room Not Found", "Room not created, returning to main menu.");
		}

		// Checking room availability
		if (!searchTarget.getRoomStatus().equals("Available")) {
			throw new InvalidInputException("Error: Room Not Available", "Room not created, returning to main menu.");
		}

		// Taking customerID
		String customerID = null;
		CustomInputDialog customerIDDialog = new CustomInputDialog("Enter customer ID", "C_");
		Optional<String> customerResult = customerIDDialog.showAndWait();

		if (customerResult.isPresent()) {
			customerID = "C_" + customerResult.get().trim().toUpperCase();
		} else {
			throw new InvalidInputException("Error: Customer ID Not Entered.",
					"Room not created, returning to main menu.");
		}

		// Taking rent date
		DateTime rentDate = this.takeDate("Rent");
		if (rentDate == null) {
			return;
		}

		// Taking numOfRentDays
		int rentDays = 0;
		CustomInputDialog rentDaysDialog = new CustomInputDialog("Enter Number of Rent Days", "Numeric values only: ");
		Optional<String> daysResult = rentDaysDialog.showAndWait();

		if (daysResult.isPresent()) {
			try {
				rentDays = Integer.parseInt(daysResult.get().trim());
			} catch (Exception e) {
				throw new InvalidInputException("Error: Wrong Date Format",
						"Room not created, returning to main menu.");
			}
		} else {
			throw new InvalidInputException("Error: Rent Days Not Entered.",
					"Room not created, returning to main menu.");
		}

		// Calling rent() - throws Rental Exception
		searchTarget.rent(customerID, rentDate, rentDays);

		// If no exception
		String roomMessage = "Room " + searchTarget.getRoomID() + " is now rented by customer " + customerID;
		AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room Rented Successfully!", roomMessage);
		success.showAndWait();
	}

	public void exportData(File target, Room thisRoom) throws InvalidInputException, FileNotFoundException {

		String path = target.getPath();

		File file1 = new File(path + "\\" + thisRoom.getRoomID() + "-export-data.txt");

		PrintWriter output = new PrintWriter(file1);
		output.write(thisRoom.toString() + ":" + thisRoom.getImagePath());
		for (HiringRecord i : thisRoom.getHiringRecords()) {
			output.write("\n" + i.toString());
		}
		output.close();
		
		AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Data Exported Successfully.","");
		success.showAndWait();
	}

	// Checks String is less than 20 words by counting spaces.
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

	// Takes date as string and converts it to DateTime, returns DateTime.
	public DateTime stringToDateTime(String dateString) {

		String intValue = dateString.replaceAll("[^0-9]", "");
		int day = Integer.parseInt(intValue.substring(0, 2));
		int month = Integer.parseInt(intValue.substring(2, 4));
		int year = Integer.parseInt(intValue.substring(4, 8));
		DateTime Date = new DateTime(day, month, year);

		return Date;

	}

	// Takes a date input from user - reduces code repitition - consider letting
	// datetime exception propogate through****
	public DateTime takeDate(String context) throws InvalidInputException {

		DateTime date = null;
		CustomInputDialog rentDateDialog = new CustomInputDialog("Enter " + context + " Date", "Format dd/mm/yyyy: ");
		Optional<String> dateResult = rentDateDialog.showAndWait();

		if (dateResult.isPresent()) {
			try {
				date = this.stringToDateTime(dateResult.get());
			} catch (Exception e) {
				throw new InvalidInputException("Error: Wrong Date Format",
						"Room not created, returning to main menu.");
			}
		} else {
			throw new InvalidInputException("Error: Rent Date Not Entered",
					"Room not created, returning to main menu.");
		}
		return date;
	}

	// Takes feature summary from user
	public String takeFeatureSummary() throws InvalidInputException {

		CustomInputDialog featDialog = new CustomInputDialog("Enter feature summary (20 words or less)", "");
		Optional<String> featResult = featDialog.showAndWait();
		String featureSummary = null;

		if (featResult.isPresent()) {
			featureSummary = featResult.get();
		} else {
			throw new InvalidInputException("Room Creation Cancelled", "Room not created, returning to main menu.");
		}

		// Feature summary validation (20 words or less)
		if (!this.checkFeatureSummary(featureSummary)) {
			throw new InvalidInputException("Feature Summary Invalid", "Room not created, returning to main menu.");
		}

		return featureSummary;
	}

	// Takes a new room ID for adding a room
	public String takeNewID(String type) throws InvalidInputException {

		// Taking RoomID
		String roomID = null;
		String typeCode = null;

		if (type == "Standard Room") {
			typeCode = "R_";
		} else if (type == "Suite") {
			typeCode = "S_";
		}

		CustomInputDialog roomIDDialog = new CustomInputDialog("Enter " + type + " ID" + "\nLeave blank for random ID",
				typeCode);
		Optional<String> result = roomIDDialog.showAndWait();

		if (result.isPresent() && result.get().trim().length() < 9) {
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
			throw new InvalidInputException("Room Creation Cancelled", "Room not created, returning to main menu.");
		}

		// RoomID uniqueness check
		if (!this.citylodge.checkRoomID(roomID)) {
			throw new InvalidInputException("Duplicate Room ID", "Room not created, returning to main menu.");
		}

		AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Room ID set to " + roomID, "");
		success.showAndWait();
		return roomID;
	}

	// Takes existing room ID from user
	public String takeID() throws InvalidInputException {

		// Taking roomID
		String roomID = null;
		CustomInputDialog customerIDDialog = new CustomInputDialog(
				"Enter Room ID\nStandard rooms have the format R_000\nSuites have the format S_000", "");
		Optional<String> roomResult = customerIDDialog.showAndWait();

		if (roomResult.isPresent()) {
			roomID = roomResult.get().trim().toUpperCase();
		} else {
			throw new InvalidInputException("Room ID Not Entered", "Room not created, returning to main menu.");
		}

		return roomID;
	}

	public int getRoomOrSuite() {

		int choice = 0;
		ButtonType buttonOne = new ButtonType("Room");
		ButtonType buttonTwo = new ButtonType("Suite");
		AlertMessage roomOrSuite = new AlertMessage(AlertType.CONFIRMATION, "Please choose room or suite", buttonOne,
				buttonTwo);
		Optional<ButtonType> result = roomOrSuite.showAndWait();

		if (result.get() == buttonOne) {
			choice = 1;

		} else if (result.get() == buttonTwo) {
			choice = 2;
		}

		return choice;
	}

	public CityLodge getCityLodge() {
		return this.citylodge;
	}

	public CityLodgeDB getDatabase() {
		return this.database;
	}
}
