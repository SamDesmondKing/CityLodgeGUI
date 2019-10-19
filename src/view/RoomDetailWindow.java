package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RoomDetailWindow extends GridPane {

	public RoomDetailWindow(Stage primaryStage, Scene previousScene) {
		Button backButton = new Button("Go Back");
		backButton.setOnAction(e -> primaryStage.setScene(previousScene));
		setPadding(new Insets(10, 10, 10, 10));
		add(backButton, 0, 2);
	}
}
