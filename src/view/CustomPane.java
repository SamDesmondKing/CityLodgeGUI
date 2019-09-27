package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CustomPane extends BorderPane {

	public CustomPane(Stage primaryStage, Scene previousScene) {
		Button backButton = new Button("Go Back");
		backButton.setOnAction(e -> primaryStage.setScene(previousScene));
		setCenter(backButton);

	}
}
