package model;

import model.exceptions.RentalException;

public class StandardRoom extends Room {

	public StandardRoom(String roomID, int numBeds, String featureSummary) {

		super(roomID, numBeds, featureSummary);

	}

	// Checks requirements and rents room
	public void rent(String customerID, DateTime rentDate, int numOfRentDays) throws RentalException {

		// Checks that rental period meets requirements
		if (this.checkRentDays(rentDate, numOfRentDays)) {

			this.setRoomStatus("Rented");
			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);

			// Create hiring record and calls method to add it to this object's ArrayList.
			String recordID = this.getRoomID() + customerID + rentDate.getEightDigitDate();
			HiringRecord thisRecord = new HiringRecord(recordID, rentDate, estimatedReturnDate, this.getRentalRate());
			this.addHiringRecord(thisRecord);

		} else {
			throw new RentalException("Error: room status not available or rental period invalid.", "Returning to main menu.");
		}
	}

	// Returns detail String
	public String toString() {

		String record = this.getRoomID() + ":" + this.getNumBeds() + ":" + this.getRoomType() + ":"
				+ this.getRoomStatus() + ":" + this.getFeatureSummary();

		return record;
	}

	// Returns human-readable detail String
	public String getDetails() {

		String rentalRecord = this.getAllHiringRecords();

		String record = "\nRoom ID:			" + this.getRoomID() + "\nNumber of beds:			" + this.getNumBeds()
				+ "\nType:				" + this.getRoomType() + "\nStatus:				" + this.getRoomStatus()
				+ "\nFeature summary:		" + this.getFeatureSummary() + "\nRENTAL RECORD			"
				+ rentalRecord;

		return record;
	}

	// Returns true if rent period meets specified requirements. 
	public boolean checkRentDays(DateTime rentDate, int numOfRentDays) {

		DateTime today = new DateTime();
		boolean result = false;

		String rentDay = rentDate.getNameOfDay().toUpperCase().trim();	
		
		// If rentDate is in the future and numOfRentDays is less than or equal to 10 
		if (DateTime.diffDays(rentDate, today) >= -1 && numOfRentDays <= 10) {

			// If rentDate is Mon-Fri, numOfRentDays must be >= 2. If Sat/Sun, >=3.
			if (rentDay.equals("SATURDAY") || rentDay.equals("SUNDAY")) {
	
				if (numOfRentDays >= 3) {
					result = true;
				}

			} else {
				if (numOfRentDays >= 2) {
					result = true;
				}
			}
		}
		return result;
	}
}
