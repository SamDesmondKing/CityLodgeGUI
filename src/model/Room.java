package model;
import java.util.ArrayList;

import model.exceptions.MaintenanceException;
import model.exceptions.RentalException;
import model.exceptions.ReturnException;

public abstract class Room {

	// Fixed
	private String roomID;
	private int numBeds;
	private String featureSummary;
	private double rentalRate;
	private String roomType;
	private String imagePath;
	
	// Flexible
	private String roomStatus;

	// Storage
	private ArrayList<HiringRecord> hiringRecords = new ArrayList<HiringRecord>();

	// Constructor
	public Room(String roomID, int numBeds, String featureSummary) {

		this.roomID = roomID;
		this.numBeds = numBeds;
		this.featureSummary = featureSummary;
		this.roomStatus = "Available";
		this.imagePath = "images/defaultImage";

		// Set rental rates and room type based on number of beds
		if (numBeds == 1) {
			this.rentalRate = 59;
			this.roomType = "Standard Room";
		} else if (numBeds == 2) {
			this.rentalRate = 99;
			this.roomType = "Standard Room";
		} else if (numBeds == 4) {
			this.rentalRate = 199;
			this.roomType = "Standard Room";
		} else {
			this.rentalRate = 999;
			this.roomType = "Suite";
		}
	}

	//Abstract methods
	abstract public void rent(String customerID, DateTime rentDate, int numOfRentDays) throws RentalException;
	abstract public String toString();
	abstract public String getDetails();

	//Checks conditions and sets roomStatus to 'Maintenance'
	public void performMaintenance() throws MaintenanceException {

		if (this.getRoomStatus().equalsIgnoreCase("Available")) {
			this.setRoomStatus("Maintenance");
		} else {
			throw new MaintenanceException("Error: Room Not Avialable for Maintenance.","Returning to main menu.");
		}
	}

	//Checks conditions and sets roomStatus to 'Available'.
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {

		DateTime today = new DateTime();

		//If room is under maintenance and completionDate is not in the future
		if (this.getRoomStatus().equalsIgnoreCase("Maintenance") && DateTime.diffDays(today, completionDate) >= 1) {
			this.setRoomStatus("Available");
		} else {
			throw new MaintenanceException("Error: Room Currently Under Maintenance or Completion Date is in the Future","Returning to main menu.");
		}
	}

	// Returns room/suite, updates hiring record of that Room will all relevant
	// fees. 
	public void returnRoom(DateTime returnDate) throws ReturnException {

		// If room is not currently rented out, you can't return it.
		if (!this.roomStatus.equalsIgnoreCase("Rented")) {
			throw new ReturnException("Error: Room is not currently rented and therefore cannot be returned.","Returning to main menu.");
		}

		// If returnDate is before rentDate, or in the past, you can't return it.
		DateTime today = new DateTime();
		if ((DateTime.diffDays(this.getCurrentHiringRecord().getRentDate(), returnDate) >= 1) || DateTime.diffDays(today, returnDate) >= 1) {
			throw new ReturnException("Error: Return Date invalid. Room not returned.","Returning to main menu.");
		}

		// If we made it to here, calculate and set all fees in the hiring record.
		this.getCurrentHiringRecord().squareOff(returnDate);

		// Officially 'un-rent' room.
		this.setRoomStatus("Available");
	}

	// Add hiring records to the HiringRecord array of this room object.
	public void addHiringRecord(HiringRecord thisRecord) {

		if (hiringRecords.size() < 10) {

			this.hiringRecords.add(thisRecord);
		} else {

			this.hiringRecords.remove(0);
			this.hiringRecords.add(thisRecord);
		}
	}

	// Gets rental records from the HiringRecord array of this room object. ************************need a way to limit this to 10
	public String getAllHiringRecords() {

		String rentalRecord = "";

		if (hiringRecords.size() == 0) {
			rentalRecord = "empty";
		} else {
			for (HiringRecord i : this.hiringRecords) {
				rentalRecord += i.getDetails();
			}
		}
		return rentalRecord;
	}

	// Returns current hiring record
	public HiringRecord getCurrentHiringRecord() {

		return this.hiringRecords.get(this.hiringRecords.size() - 1);

	}

	public double getRentalRate() {
		return this.rentalRate;
	}

	public String getRoomID() {
		return this.roomID;
	}

	public int getNumBeds() {
		return this.numBeds;
	}

	public String getRoomType() {
		return this.roomType;
	}

	public String getRoomStatus() {
		return this.roomStatus;
	}

	public String getFeatureSummary() {
		return this.featureSummary;
	}

	public void setRoomStatus(String status) {
		this.roomStatus = status;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public ArrayList<HiringRecord> getHiringRecords() {
		return this.hiringRecords;
	}
}