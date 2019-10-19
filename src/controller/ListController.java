package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.Room;
import model.exceptions.InvalidInputException;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ListController {

	private Controller controller;
	private ArrayList<Room> roomArray;
	private Stage stage;
	private Scene scene;
	private MenuBar menu;

	public ListController(Controller controller, Stage stage) {

		this.stage = stage;
		this.controller = controller;

	}

	public ListView<String> createListView() throws InvalidInputException {

		ListView<String> listView = new ListView<String>();
		ObservableList<String> roomList = FXCollections.observableArrayList();
		this.roomArray = new ArrayList<Room>();

		// Update local roomArray from controller
		this.roomArray.clear();
		for (Room i : this.controller.getCityLodge().getRoomArray()) {
			this.roomArray.add(i);
		}

		// Add to ObservableList from localRoomArray
		for (Room i : this.roomArray) {
			roomList.add(i.getRoomID());
		}

		listView.setItems(roomList);

		listView.setCellFactory(value -> new CustomListCell(this.roomArray, stage, scene));
		
		return listView;
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	static class CustomListCell extends ListCell<String> {

		private ArrayList<Room> roomArray;
		private ImageView imageView = new ImageView();
		private Image image = null;
		private FileInputStream imageFile = null;
		private BorderPane pane = new BorderPane();
		private Label label = new Label();
		private Button button = new Button("View Room");
		private Stage stage;
		private Scene scene;
		
		public CustomListCell(ArrayList<Room> roomArray, Stage stage, Scene scene) {
			
			this.stage = stage;
			this.scene = scene;
			this.roomArray = roomArray;
			this.imageView.setFitWidth(200);
			this.imageView.setPreserveRatio(true);
			this.pane.setLeft(this.imageView);
			this.pane.setCenter(this.label);
			this.pane.setRight(this.button);
			BorderPane.setAlignment(this.button, Pos.CENTER);
			
		}

		@Override
		public void updateItem(String name, boolean empty) {
			String type = null, status = null, features = null;
			
			if (!empty) {
				//Adding event handling to button
				this.button.setOnAction(new SwitchHandler(this.stage, this.scene, name, this.roomArray));
				
				//Adding Image
				for (Room i : this.roomArray) {
					if (name.equals(i.getRoomID())) {
						try {
							imageFile = new FileInputStream(i.getImagePath().trim());
						} catch (FileNotFoundException e) {
							return;
						}
						image = new Image(imageFile);
						type = i.getRoomType();
						status = i.getRoomStatus();
						features = i.getFeatureSummary();
					}
				}
				imageView.setImage(image);
				label.setText("  "+ name + " - Room Type: " + type + ". Room Status: " + status + ". Room Features: " + features + ".  ");
				setGraphic(pane);

			} else {
				setText(null);
				setGraphic(null);
			}
		}
	}
}
