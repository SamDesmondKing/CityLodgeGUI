
package controller;

import java.io.File;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DateTime;
import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.exceptions.InvalidInputException;

public class ImportHandler implements EventHandler<ActionEvent> {

	private Controller controller;
	private Stage primaryStage;
	private String roomID;
	private Room room;

	public ImportHandler(Controller controller, Stage primaryStage) {

		this.controller = controller;
		this.primaryStage = primaryStage;

	}

	@Override
	public void handle(ActionEvent event) {

		FileChooser fc = new FileChooser();
		File targetFile = fc.showOpenDialog(this.primaryStage);

		// Room / Suite
		String ID, type, status, features, imagePath;
		DateTime maintDate;
		int numBeds;

		String recordID, returnDate, rentalFee, lateFee;

		DateTime rentDate, estReturnDate;
		
		if (targetFile != null) {

			try {

				Scanner input = new Scanner(targetFile);
				input.useDelimiter(":|\\n");
				
				// Main loop
				while (input.hasNext()) {
					ID = input.next().trim();
					if (ID.length() <= 10) {
						// It's a Room Record
						this.roomID = ID;
						numBeds = Integer.parseInt(input.next().trim());
						type = input.next().trim();
						status = input.next().trim();
						if (type.equals("Suite")) {
							maintDate = this.controller.stringToDateTime(input.next().trim());
							features = input.next().trim();
							imagePath = input.next().trim();
							if (!this.controller.getCityLodge().checkRoomID(ID)) {
								throw new InvalidInputException("Error: Duplicate RoomID Detected.","Room not imported.");
							}
							this.room = new Suite(this.roomID, numBeds, features, maintDate);
							this.room.setImagePath(imagePath);
							this.room.setRoomStatus(status);
							// String roomID, int numBeds, String featureSummary, DateTime maintenanceDate
							this.controller.getCityLodge().addToArray(this.room);
						} else {
							features = input.next().trim();
							System.out.println("standardroom features" + features);
							imagePath = input.next().trim();
							System.out.println("standardroom image" + imagePath);
							if (!this.controller.getCityLodge().checkRoomID(ID)) {
								throw new InvalidInputException("Error: Duplicate RoomID Detected.","Room not imported.");
							}
							this.room = new StandardRoom(roomID, numBeds, features);
							this.room.setImagePath(imagePath);
							this.room.setRoomStatus(status);
							this.controller.getCityLodge().addToArray(this.room);
						}
					} else {
						// It's a hiring record
						rentDate = this.controller.stringToDateTime(input.next().trim());
						estReturnDate = this.controller.stringToDateTime(input.next().trim());
						returnDate = input.next().trim();
						rentalFee = input.next().trim();
						lateFee = input.next().trim();
						double rentalRate = this.room.getRentalRate();

						if (returnDate instanceof String) {
							// Open record
							HiringRecord HR = new HiringRecord(ID, rentDate, estReturnDate, rentalRate);
							//Adding to this room's HR array - room saved above. 
							this.room.addHiringRecord(HR);

						} else {
							// Closed record
							DateTime returnDateTime = this.controller.stringToDateTime(returnDate);
							double rentalFeeDouble = Double.parseDouble(rentalFee);
							double lateFeeDouble =  Double.parseDouble(lateFee);
							
							HiringRecord HR = new HiringRecord(ID, rentDate, estReturnDate, rentalRate, returnDateTime, rentalFeeDouble, lateFeeDouble, true);
							//Adding to this room's HR array - room saved above. 
							this.room.addHiringRecord(HR);
						}
					}
				}

			} catch (InvalidInputException e) {
				e.getError();
			} catch (Exception e) {
				InvalidInputException b = new InvalidInputException("Error: Problem With Import File", "");
				b.getError();
				e.printStackTrace();
			}

		} else {
			return;
		}
	}
}