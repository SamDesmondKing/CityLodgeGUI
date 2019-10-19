package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.HiringRecord;
import model.Room;
import view.RoomDetailWindow;

public class SwitchHandler implements EventHandler<ActionEvent> {

	private Stage primaryStage;
	private Scene previousScene;
	private String roomID;
	private RoomDetailWindow detailWindow;
	private ArrayList<Room> roomArray;

	public SwitchHandler(Stage primaryStage, Scene previousScene, String roomID, ArrayList<Room> roomArray) {
		this.primaryStage = primaryStage;
		this.previousScene = previousScene;
		this.roomID = roomID;
		this.roomArray = roomArray;

		this.buildDetailWindow(roomID);
	}

	public void buildDetailWindow(String roomID) {

		int numBeds = 0;
		String featureSummary = null;
		double rentalRate = 0;
		String roomType = null;
		String imagePath = null;
		String roomStatus = null;
		String hiringRecords = null;
		ListView<String> listView = new ListView<String>();
		ObservableList<String> recordList = FXCollections.observableArrayList();

		for (Room i : this.roomArray) {
			if (roomID.equals(i.getRoomID())) {
				numBeds = i.getNumBeds();
				featureSummary = i.getFeatureSummary();
				rentalRate = i.getRentalRate();
				roomType = i.getRoomType();
				imagePath = i.getImagePath();
				roomStatus = i.getRoomStatus();
				hiringRecords = i.getAllHiringRecords();
			}
		}

		this.detailWindow = new RoomDetailWindow(primaryStage, previousScene);
		FileInputStream imageFile = null;
		try {
			imageFile = new FileInputStream(imagePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Image image = new Image(imageFile);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(600);
		imageView.setPreserveRatio(true);

		recordList.add(hiringRecords);
		listView.setItems(recordList);
		listView.setMinWidth(300);

		Label label = new Label();
		label.setMinWidth(150);
		label.setText(" - Room Details -\nRoom ID: " + roomID + "\nNumber of Beds: " + numBeds + "\nFeatures: "
				+ featureSummary + "\nRental Rate: " + rentalRate + "\nRoom Type: " + roomType + "\nRoom Status: "
				+ roomStatus);

		detailWindow.add(imageView, 0, 0);
		detailWindow.add(label, 1, 0);
		detailWindow.add(listView, 2, 0);

		detailWindow.setAlignment(Pos.TOP_LEFT);

	}

	@Override
	public void handle(ActionEvent event) {
		primaryStage.setScene(new Scene(this.detailWindow, 1100, 500));
	}

}