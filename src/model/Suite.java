package model;

import model.exceptions.MaintenanceException;
import model.exceptions.RentalException;

public class Suite extends Room {

	private DateTime lastMaintenanceDate;

	// Constructor
	public Suite(String roomID, int numBeds, String featureSummary, DateTime maintenanceDate) {

		super(roomID, numBeds, featureSummary);

		this.lastMaintenanceDate = maintenanceDate;
	}

	// Done (Version 2 - untested)
	public void rent(String customerID, DateTime rentDate, int numOfRentDays) throws RentalException {

		// Checks that rental period doesn't clash with maintenance schedule
		if (this.checkMaintenanceSchedule(rentDate, numOfRentDays)) {

			this.setRoomStatus("Rented");

			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);

			// Create hiring record and calls method to add it to this object's ArrayList.
			String recordID = this.getRoomID() + customerID + rentDate.getEightDigitDate();
			HiringRecord thisRecord = new HiringRecord(recordID, rentDate, estimatedReturnDate, this.getRentalRate());
			this.addHiringRecord(thisRecord);
		
		} else {
			throw new RentalException("Error: rental period clashes with maintenance schedule.", "Returning to main menu.");
		}
	}

	// Returns custom room String, overrides Room.toString().
	public String toString() {

		String record = this.getRoomID() + ":" + this.getNumBeds() + ":" + this.getRoomType() + ":"
				+ this.getRoomStatus() + ":" + this.lastMaintenanceDate + ":" + this.getFeatureSummary();

		return record;
	}

	// Returns human-readable details String, overrides Room.getDetails().
	public String getDetails() {

		String rentalRecord = this.getAllHiringRecords();

		String record = "\nRoom ID:			" + this.getRoomID() + "\nNumber of beds:			" + this.getNumBeds()
				+ "\nType:				" + this.getRoomType() + "\nStatus:				" + this.getRoomStatus()
				+ "\nFeature summary:		" + this.getFeatureSummary() + "\nRENTAL RECORD			"
				+ rentalRecord;

		return record;
	}

	// Overrides Room.completeMaintenance(). Checks conditions and changes Room status from 'Maintenance' to 'Available'.
	// Updates lastMaintenanceDate. Returns true if successful.
	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {

		DateTime today = new DateTime();
		
		//If room is under maintenance and completion date is not in the future. 
		if (this.getRoomStatus().equalsIgnoreCase("Maintenance") && DateTime.diffDays(today, completionDate) >= 1) {

			this.setRoomStatus("Available");
			this.lastMaintenanceDate = completionDate;
			
		} else {
			throw new MaintenanceException("Error: Room status is currently set to " + this.getRoomStatus(), "Returning to main menu.");
		}
	}

	// Returns false if there's a clash, true otherwise.
	public boolean checkMaintenanceSchedule(DateTime rentDate, int numOfRentDays) {

		DateTime nextMaintenanceDate = new DateTime(this.lastMaintenanceDate, 10);
		DateTime returnDate = new DateTime(rentDate, numOfRentDays);

		// If there isn't at least one day between returnDate and nextMaintenanceDate
		if (DateTime.diffDays(nextMaintenanceDate, returnDate) <= -1) {

			return false;
		} else {
			return true;
		}
	}

	public DateTime getMaintenanceDate() {
		return this.lastMaintenanceDate;
	}

	public void setLastMaintenanceDate(DateTime lastMaintenanceDate) {
		this.lastMaintenanceDate = lastMaintenanceDate;
	}
	
	public DateTime getLastMaintDate() {
		return this.lastMaintenanceDate;
	}
}
