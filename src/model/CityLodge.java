package model;
import java.util.Random;
import java.util.Scanner;

import controller.Controller;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

public class CityLodge {

	private ArrayList<Room> roomArray = new ArrayList<Room>();
	private Controller controller;

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

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public DateTime stringToDateTime(String dateString) {

		String intValue = dateString.replaceAll("[^0-9]", "");
		int day = Integer.parseInt(intValue.substring(0, 2));
		int month = Integer.parseInt(intValue.substring(2, 4));
		int year = Integer.parseInt(intValue.substring(4, 8));
		DateTime Date = new DateTime(day, month, year);

		return Date;

	}
	
}
