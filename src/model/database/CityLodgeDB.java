package model.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import controller.Controller;
import model.DateTime;
import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.exceptions.InvalidInputException;

public class CityLodgeDB {

	private Connection con;
	private Controller controller;

	public CityLodgeDB() throws Exception {

		final String DB_NAME = "CityLodgeDB";

		// GUI connection command: java -cp libraries/hsqldb.jar org.hsqldb.util.DatabaseManagerSwing

		// Connect database
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		this.con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + DB_NAME, "SA", "");

	}

	// Shuts down the database gracefully by excecuting SHUTDOWN command.
	public void shutdown() throws SQLException {

		Statement statement = con.createStatement();
		statement.execute("SHUTDOWN");
		this.con.close();
	}

	// Add data from database to citylodge array
	public void initialise() throws Exception {

		// Create and execute statement
		Statement stmt = con.createStatement();
		ResultSet rooms = stmt.executeQuery("select * from room;");
		ResultSet hiringRecords = stmt.executeQuery("select * from hiringrecord");

		// Loop through the Room data
		while (rooms.next()) {

			String roomID = rooms.getString("ROOMID").trim();
			int numBeds = rooms.getInt("NUMBEDS");
			String featureSummary = rooms.getString("FEATURESUMMARY").trim();
			String imagePath = rooms.getString("IMAGEPATH").trim();
			String lastMaintenanceDate = rooms.getString("LASTMAINTENANCEDATE").trim();

			if (numBeds == 6) {
				DateTime maintDate = stringToDateTime(lastMaintenanceDate);
				Room newSuite = new Suite(roomID, numBeds, featureSummary, maintDate);
				newSuite.setImagePath(imagePath);
				this.controller.getCityLodge().addToArray(newSuite);
			} else {
				Room newRoom = new StandardRoom(roomID, numBeds, featureSummary);
				newRoom.setImagePath(imagePath);
				this.controller.getCityLodge().addToArray(newRoom);
			}
		}

		while (hiringRecords.next()) {

			String recordID = hiringRecords.getString("RECORDID").trim();
			String rentDate = hiringRecords.getString("RENTDATE").trim();
			String estReturnDate = hiringRecords.getString("ESTIMATEDRETURNDATE").trim();
			double rentalRate = hiringRecords.getDouble("RENTALRATE");
			String returnDate = hiringRecords.getString("RETURNDATE").trim();
			double rentalFee = hiringRecords.getDouble("RENTALFEE");
			double lateFee = hiringRecords.getDouble("LATEFEE");
			Boolean returned = hiringRecords.getBoolean("RETURNED");
			String roomID = hiringRecords.getString("ROOMID").trim();

			DateTime estReturnDateTime = stringToDateTime(estReturnDate);
			DateTime rentDateTime = stringToDateTime(rentDate);
			
			HiringRecord newHR = null;
			
			if (returned == true) {
				DateTime returnDateTime = stringToDateTime(returnDate);
				newHR = new HiringRecord(recordID, rentDateTime, estReturnDateTime, rentalRate, returnDateTime,
					rentalFee, lateFee, returned);
			} else {
				newHR = new HiringRecord(recordID, rentDateTime, estReturnDateTime, rentalRate);
			}
			
			//String recordID, DateTime rentDate, DateTime estimatedReturnDate, double rentalRate
			
			// Again, throws invalid imput exception to main if error - main will print
			// specific error message.
			Room owningRoom = this.controller.getCityLodge().searchRoomByID(roomID);

			// Add this hiring record to the Room it belongs to.
			owningRoom.addHiringRecord(newHR);
		}

		// Clean up
		rooms.close();
		stmt.close();
		hiringRecords.close();
	}

	// Write data from citylodge array to database - can be done after every
	// main menu action and on shutdown.
	public void saveData() throws SQLException {

		// Drop all tables
		Statement drop = con.createStatement();
		drop.executeQuery("TRUNCATE SCHEMA public AND COMMIT");

		// Update ROOM with current state of citylodge array
		Statement update = con.createStatement();
		for (Room i : this.controller.getCityLodge().getRoomArray()) {

			String maintDateString;
			try {
				DateTime maintDate = ((Suite) i).getMaintenanceDate();
				maintDateString = maintDate.getFormattedDate();
			} catch (Exception e) {
				maintDateString = null;
			}

			update.executeQuery("insert into ROOM values ('" + i.getRoomID() + "'," + i.getNumBeds() + ",'"
					+ i.getFeatureSummary() + "'," + i.getRentalRate() + ",'" + i.getRoomType() + "','"
					+ i.getImagePath() + "','" + maintDateString + "')");
		}

		//Update HIRINGRECORD with contents of each room's hiring record array.
		Statement updateHR = con.createStatement();
		for (Room i : this.controller.getCityLodge().getRoomArray()) {
			for (HiringRecord j : i.getHiringRecords()) {

				String rentDateString;
				String estReturnDateString;
				String returnDateString;

				DateTime rentDate = j.getRentDate();
				rentDateString = rentDate.getFormattedDate();

				try {
					DateTime estReturnDate = j.getEstimatedReturnDate();
					estReturnDateString = estReturnDate.getFormattedDate();
				} catch (Exception e) {
					estReturnDateString = null;
				}

				try {
					DateTime returnDate = j.getReturnDate();
					returnDateString = returnDate.getFormattedDate();
				} catch (Exception e) {
					returnDateString = null;
				}

				updateHR.executeQuery("insert into HIRINGRECORD values ('" + j.getRecordID() + "','" + rentDateString
						+ "','" + estReturnDateString + "'," + j.getRentalRate() + ",'" + returnDateString + "',"
						+ j.getRentalFee() + "," + j.getLateFee() + "," + j.getReturned() + ",'" + i.getRoomID()
						+ "')");
			}
			drop.close();
			update.close();
		}
	}

	public Connection getConnection() {
		return this.con;
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
			throw new InvalidInputException("Error with DateTime conversion", "Contact Sam for details.");
		}
		return Date;

	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}