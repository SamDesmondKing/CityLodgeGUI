package main;
import java.util.ArrayList;
import util.DateTime;

public abstract class Room {

	// Fixed
	private String roomID;
	private int numBeds;
	private String featureSummary;
	private double rentalRate;
	private String roomType;

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
	abstract public boolean rent(String customerID, DateTime rentDate, int numOfRentDays);
	abstract public String toString();
	abstract public String getDetails();

	//Checks conditions and sets roomStatus to 'Maintenance'.
	public boolean performMaintenance() {

		if (this.getRoomStatus().equalsIgnoreCase("Available")) {

			this.setRoomStatus("Maintenance");
			return true;
		} else {

			System.out.println("Error: Room status is currently set to " + this.getRoomStatus());
			return false;
		}

	}

	//Checks conditions and sets roomStatus to 'Available'.
	public boolean completeMaintenance(DateTime completionDate) {

		DateTime today = new DateTime();

		//If room is under maintenance and completionDate is not in the future
		if (this.getRoomStatus().equalsIgnoreCase("Maintenance") && DateTime.diffDays(today, completionDate) >= 1) {

			this.setRoomStatus("Available");
			return true;
		} else {

			System.out.println("Error: Room status is currently set to " + this.getRoomStatus());
			return false;
		}

	}

	// Returns room/suite, updates hiring record of that Room will all relevant
	// fees.
	public boolean returnRoom(DateTime returnDate) {

		// If room is not currently rented out, you can't return it.
		if (!this.roomStatus.equalsIgnoreCase("Rented")) {

			System.out.println("Error: Room is not currently rented and therefore cannot be returned.");
			return false;
		}

		// If returnDate is before rentDate, or in the past, you can't return it.
		DateTime today = new DateTime();
		if ((DateTime.diffDays(this.getCurrentHiringRecord().getRentDate(), returnDate) >= 1) || DateTime.diffDays(today, returnDate) >= 1) {

			System.out.println("Error: Return Date invalid. Room not returned.");
			return false;
		}

		// If we made it to here, calculate and set all fees in the hiring record.
		this.getCurrentHiringRecord().squareOff(returnDate);

		// Officially 'un-rent' room.
		this.setRoomStatus("Available");
		
		System.out.println(this.getRoomType() + " " + this.getRoomID() + " has been returned successfully.\n" + this.getCurrentHiringRecord().getDetails());

		return true;

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

	// Gets rental records from the HiringRecord array of this room object.
	public String getAllHiringRecords() {

		String rentalRecord = "";

		if (hiringRecords.size() == 0) {
			rentalRecord = "empty";
		} else {
			for (HiringRecord i : hiringRecords) {
				rentalRecord += i.getDetails();
			}
		}
		return rentalRecord;
	}

	// Returns current hiring record
	public HiringRecord getCurrentHiringRecord() {

		return this.hiringRecords.get(this.hiringRecords.size() - 1);

	}

	// Gets rentalRate
	public double getRentalRate() {
		return this.rentalRate;
	}

	// Gets roomID
	public String getRoomID() {
		return this.roomID;
	}

	// Gets numBeds
	public int getNumBeds() {
		return this.numBeds;
	}

	// Gets roomType
	public String getRoomType() {
		return this.roomType;
	}

	// Gets roomStatus
	public String getRoomStatus() {
		return this.roomStatus;
	}

	// Gets featureSummary
	public String getFeatureSummary() {
		return this.featureSummary;
	}

	// Sets room status
	public void setRoomStatus(String status) {

		this.roomStatus = status;
	}

}