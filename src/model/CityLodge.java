package model;

import controller.Controller;
import model.exceptions.InvalidInputException;
import java.util.ArrayList;

public class CityLodge {

	private ArrayList<Room> roomArray = new ArrayList<Room>();
	private Controller controller;

	// Adds Room to roomArray. Deletes oldest Room if full (50 Rooms)
	public void addToArray(Room thisRoom) {

		if (roomArray.size() < 50) {
			this.roomArray.add(thisRoom);
		} else {
			this.roomArray.remove(0);
			this.roomArray.add(thisRoom);
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

	// Calls Room.getDetails() on each Room in roomArray. TODO - prints to console
	public void displayRooms() {

		for (Room i : this.roomArray) {
			System.out.println(i.getDetails());
		}
	}

	// Searches roomArray by roomID 
	public Room searchRoomByID(String searchRequest) throws InvalidInputException {

		Room result = null;
		for (Room i : roomArray) {
			if (i.getRoomID().equalsIgnoreCase(searchRequest)) {
				result = i;
			}
		}
		
		if (result == null) {
			throw new InvalidInputException("Error: Room Not Found","Returning to main menu.");
		}
		return result;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public DateTime stringToDateTime(String dateString) throws InvalidInputException {

		DateTime Date;
		String intValue = dateString.replaceAll("[^0-9]", "");
		int day = Integer.parseInt(intValue.substring(0, 2));
		int month = Integer.parseInt(intValue.substring(2, 4));
		int year = Integer.parseInt(intValue.substring(4, 8));

		try {
			Date = new DateTime(day, month, year);
			} catch (Exception e) {
				throw new InvalidInputException("Error with DateTime conversion","Contact Sam for details.");
			}

		return Date;

	}

	public ArrayList<Room> getRoomArray() {
		return this.roomArray;
	}
	
}

