package model;

public class HiringRecord {

	// On renting
	private String recordID;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private double rentalRate;
	
	// On returning
	private DateTime returnDate;
	private double rentalFee;
	private double lateFee;
	private boolean returned;

	// Constructor
	public HiringRecord(String recordID, DateTime rentDate, DateTime estimatedReturnDate, double rentalRate) {

		this.recordID = recordID;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.rentalRate = rentalRate;

	}
	
	//Calculates and sets returnDate, lateFee (if any) and rentalFee (total due including lateFee).
	public void squareOff(DateTime returnDate) {
		
		double rent;
		
		this.returnDate = returnDate;
		this.returned = true;
		
		int bookedDays = DateTime.diffDays(estimatedReturnDate, this.rentDate);
		int actualDays = DateTime.diffDays(returnDate, this.rentDate);
		
		//If returned on time
		if (actualDays <= bookedDays) {
			
			this.rentalFee = (this.rentalRate * actualDays);
			
		//If late
		} else {
			
			rent = (this.rentalRate * bookedDays);
			
			//For Suite
			if (this.rentalRate == 999) {
				
				this.lateFee = (actualDays - bookedDays) * 1099;
				
			//For StandardRoom
			} else {
				
				this.lateFee = (actualDays - bookedDays) * (this.rentalRate * 1.35);
			}
			
			this.rentalFee = rent + this.lateFee;
		}
	}

	// Returns custom info String
	public String toString() {

		String tempReturn = "none";
		String tempRentalFee = "none";
		String tempLateFee = "none";

		String record = this.recordID + ":" + this.rentDate + ":" + this.estimatedReturnDate + ":" + tempReturn + ":"
				+ tempRentalFee + ":" + tempLateFee;

		if (this.returned) {

			record = this.recordID + ":" + this.rentDate + ":" + this.estimatedReturnDate + ":" + this.returnDate + ":"
					+ String.format("%.2f", this.rentalFee) + ":" + String.format("%.2f", this.lateFee);
		}
		return record;
	}

	// Returns custom human-readable info String
	public String getDetails() {
		
		String details = "\nRecord ID:			" + this.recordID + "\nRent Date:			" + this.rentDate
				+ "\nEstimated Return Date:  	" + this.estimatedReturnDate;

		if (this.returned) {

			details += "\nActual Return Date:		"
					+ this.returnDate + "\nRental Fee:			" + String.format("%.2f", this.rentalFee) + "\nLate Fee:			"
					+ String.format("%.2f", this.lateFee);
		}
		
		//Divider line
		details += "\n--------------------------------------------";
		
		return details;
	}

	// Gets recordID
	public String getRecordID() {
		return this.recordID;
	}
	
	public DateTime getRentDate() {
		return this.rentDate;
	}
	
	public DateTime getEstimatedReturnDate() {
		return this.estimatedReturnDate;
	}
	
	public DateTime getReturnDate() {
		return this.returnDate;
	}
	
	public double getRentalFee() {
		return this.rentalFee;
	}
	
	public double getLateFee() {
		return this.lateFee;
	}
	
	public void setReturnDate(DateTime returnDate) {
		this.returnDate = returnDate;
	}
}
