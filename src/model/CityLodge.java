package model;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class CityLodge {

	private ArrayList<Room> roomArray = new ArrayList<Room>();

	// Sub-menu to check if the user would like to enter a Standard Room or a Suite.
	public String checkRoomOrSuite() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Please select room type:\nStandard Room:			1\nSuite:				2");
		int input = sc.nextInt();
		sc.close();
		
		if (input == 1) {
			return "Room";			
		} else if (input == 2) {
			return "Suite";
		} else {
			System.out.println("Error: invalid selection. Returning to main menu.");
			return "";
		}
	}
	

	// Takes and validates input to create StandardRoom, adds to roomArray
	public void addRoom() {

		Scanner sc = new Scanner(System.in);

		// Taking RoomID
		System.out.println("Enter room ID, beginning with 'R_'. Leave empty to generate random ID.");
		String roomID = sc.nextLine().trim().toUpperCase();
		
		//If empty, generate random ID
		if (roomID.isEmpty()) {
			Random random = new Random();
			int roomNo = random.nextInt(99999);
			roomID = "R_" + roomNo;
		}
		
		// RoomID format check
		if (roomID.charAt(0) != 'R' || roomID.charAt(1) != '_') {
			System.out.println("Invalid room ID, room not created. Returning to main menu.");
			sc.close();
			return;
		}

		// RoomID uniqueness check
		if (!this.checkRoomID(roomID)) {
			System.out.println(
					"Error - a room with that RoomID already exists. Room not created, returning to main menu.");
			sc.close();
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
		
		//Closing scanner
		sc.close();
	}

	// Takes and validates input to create Suite, adds to roomArray
	public void addSuite() {

		Scanner sc = new Scanner(System.in);

		// Taking roomID
		System.out.println("Enter the room ID, beginning with 'S_' :");
		String roomID = sc.nextLine().trim().toUpperCase();

		//If empty, generate random ID
		if (roomID.isEmpty()) {
			Random random = new Random();
			int roomNo = random.nextInt(99999);
			roomID = "S_" + roomNo;
		}
		
		// RoomID validity check
		if (roomID.charAt(0) != 'S' || roomID.charAt(1) != '_') {
			System.out.println("Invalid room ID, room not created. Returning to main menu.");
			sc.close();
			return;
		}

		// RoomID uniqueness check
		if (!this.checkRoomID(roomID)) {
			System.out.println(
					"Error - a room with that RoomID already exists. Room not created, returning to main menu.");
			sc.close();
			return;
		}

		// Taking feature summary.
		System.out.println("Enter the feature summary:");
		String featureSummary = sc.nextLine();

		// Feature summary validation (20 words or less)
		if (!this.checkFeatureSummary(featureSummary)) {
			System.out.println("Error: feature summary must be 20 words or less. Returning to main menu.");
			sc.close();
			return;
		}

		// Taking last maintenance date.
		System.out.println("Last maintenance date (dd/mm/yyyy): ");
		String dateString = sc.nextLine().trim();
		DateTime lastMaintenanceDate = this.stringToDateTime(dateString);

		// Validating last maintenance date
		DateTime today = new DateTime();
		if (DateTime.diffDays(today, lastMaintenanceDate) <= 0) {

			System.out.println("Error: last maintenance date cannot be in the future. Room not created.");
			sc.close();
			return;
		}

		// If we made it to here Suite is good to go.
		Suite thisRoom = new Suite(roomID, 6, featureSummary, lastMaintenanceDate);

		// Adding new Suite to roomArray.
		this.addToArray(thisRoom);
		
		sc.close();
	}

	// Adds Room to roomArray. Deletes oldest Room if full (50 Rooms)
	public void addToArray(Room thisRoom) {

		if (roomArray.size() < 50) {

			this.roomArray.add(thisRoom);
			System.out.println("Room added successfully.");
		} else {

			Room temp = this.roomArray.get(0);
			this.roomArray.remove(0);
			this.roomArray.add(thisRoom);
			System.out.println("Room added successfully. Room: " + temp.getRoomID() + " deleted.");
		}
	}

	// Returns true if roomID is unique in roomArray, false otherwise.
	public boolean checkRoomID(String roomID) {

		for (Room i : roomArray) {

			if (i.getRoomID().equalsIgnoreCase(roomID)) {

				return false;
			}
		}
		return true;
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

	// Takes RoomID as search target, validates and calls rent() on chosen Room.
	public void callRentRoom() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Room ID: ");
		String searchID = sc.nextLine().trim();

		// Checking room exists
		Room searchTarget = this.searchRoomByID(searchID);

		if (searchTarget == null) {
			System.out.println("Error: Room not found. Returning to main menu.");
			sc.close();
			return;
		}

		// Checking room availability
		if (!searchTarget.getRoomStatus().equals("Available")) {
			System.out.println("Error: Room not available. Returning to main menu.");
			sc.close();
			return;
		}

		// Taking customerID
		System.out.println("Customer ID: ");
		String customerID = sc.nextLine().toUpperCase();

		// Taking rent date
		System.out.println("Rent date (dd/mm/yyyy): ");
		String dateString = sc.nextLine().trim();
		DateTime rentDate = this.stringToDateTime(dateString);

		// Taking numOfRentDays
		System.out.println("How many days?: ");
		int numOfRentDays = sc.nextInt();
		sc.close();
		
		if (searchTarget.rent(customerID, rentDate, numOfRentDays)) {
			System.out.println("Room " + searchTarget.getRoomID() + " is now rented by customer " + customerID);
		} else {
			System.out.println("Returning to main menu.");
		}
	}

	// Takes roomID as input and calls returnRoom() on Room object with that ID.
	public void callReturnRoom() {

		Scanner sc = new Scanner(System.in);

		// Taking roomID
		System.out.println("Room ID: ");
		String searchID = sc.nextLine().trim();

		Room thisRoom = this.searchRoomByID(searchID);

		// Validating roomID
		if (thisRoom == null) {
			System.out.println("Error: Room does not exist. Returning to main menu.");
			sc.close();
			return;
		}

		// Taking returnDate
		System.out.println("Return date (dd/mm/yyyy): ");
		String dateString = sc.nextLine().trim();
		DateTime returnDate = this.stringToDateTime(dateString);

		// Calling returnRoom(), where final validation and updating will take place.
		thisRoom.returnRoom(returnDate);
		sc.close();

	}

	// Searches for Room by ID, calls performMaintenance() on Room
	public void callPerformMaintenance() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Room ID: ");
		String searchID = sc.nextLine().trim();

		Room thisRoom = this.searchRoomByID(searchID);

		if (thisRoom == null) {
			System.out.println("Error: Room not found. Returning to main menu.");
			sc.close();
			return;
		}

		if (thisRoom.performMaintenance()) {
			System.out.println(thisRoom.getRoomType() + " " + thisRoom.getRoomID() + " is now under maintenance");
			sc.close();
		} else {
			System.out.println("Returning to main menu.");
			sc.close();
		}
	}

	// Searches for Room by ID, takes completion date and calls
	// completeMaintenance() on Room
	public void callCompleteMaintenance() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Room ID: ");
		String searchID = sc.nextLine().trim();

		Room thisRoom = this.searchRoomByID(searchID);

		if (thisRoom == null) {
			System.out.println("Error: Room not found. Returning to main menu.");
			sc.close();
			return;
		}

		System.out.println("Maintenance completion date (dd/mm/yyyy): ");
		String dateString = sc.nextLine().trim();
		DateTime completionDate = this.stringToDateTime(dateString);

		if (thisRoom.completeMaintenance(completionDate)) {
			System.out.println(thisRoom.getRoomType() + " " + thisRoom.getRoomID()
					+ " has all maintenance operations completed and is now ready for rent.");
			sc.close();
		}
	}

	// Calls Room.getDetails() on each Room in roomArray.
	public void displayRooms() {

		for (Room i : this.roomArray) {
			System.out.println(i.getDetails());
		}
	}

	// Searches roomArray by roomID and returns room if found, null otherwise.
	public Room searchRoomByID(String searchRequest) {

		Room result = null;

		for (Room i : roomArray) {

			if (i.getRoomID().equalsIgnoreCase(searchRequest)) {

				result = i;
			}
		}
		return result;
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

	
}
