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
