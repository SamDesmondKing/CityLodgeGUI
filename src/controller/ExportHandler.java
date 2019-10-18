package controller;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Room;
import model.exceptions.InvalidInputException;

public class ExportHandler implements EventHandler<ActionEvent> {

	private Controller controller;
	private Stage primaryStage;

	public ExportHandler(Controller controller, Stage primaryStage) {

		this.controller = controller;
		this.primaryStage = primaryStage;

	}

	@Override
	public void handle(ActionEvent event) {

		DirectoryChooser dc = new DirectoryChooser();
		String roomID;
		Room targetRoom = null;

		try {
			roomID = this.controller.takeID();
			targetRoom = this.controller.getCityLodge().searchRoomByID(roomID);
		} catch (InvalidInputException e1) {
			e1.getError();
		}

		if (targetRoom != null) {
			
			File resultFile = dc.showDialog(primaryStage);

			if (resultFile != null) {
				try {
					this.controller.exportData(resultFile, targetRoom);
				} catch (FileNotFoundException e) {
					InvalidInputException b = new InvalidInputException("Error: File Not Found", "");
					b.getError();
				} catch (InvalidInputException e) {
					e.getError();
				}
			} else {
				return;
			}
		}
	}
}
