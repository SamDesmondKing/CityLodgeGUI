package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.CustomPane;

public class CustomHandler implements EventHandler<ActionEvent> {

	private Stage primaryStage;
	private Scene previousScene;

	public CustomHandler(Stage primaryStage, Scene previousScene) {
		this.primaryStage = primaryStage;
		this.previousScene = previousScene;
	}

	@Override
	public void handle(ActionEvent event) {
		primaryStage.setScene(new Scene(new CustomPane(primaryStage, previousScene), 400, 450));
	}

}
