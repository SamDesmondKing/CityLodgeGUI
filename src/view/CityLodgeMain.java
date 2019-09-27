package view;

import controller.Controller;
import controller.CustomHandler;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CityLodge;

public class CityLodgeMain extends Application{
	
	
	@Override
	public void start(Stage primaryStage) {
		
		/*
		Pane menu = new VBox();
		menu.getChildren().add(new Button("Rent Room"));
		menu.getChildren().add(new Button("Return Room"));
		menu.getChildren().add(new Button("Perform Maintenance"));
		menu.getChildren().add(new Button("Complete Maintenance"));
		menu.getChildren().add(new Button("Import Room Data"));
		menu.getChildren().add(new Button("Export Room Data"));
		menu.getChildren().add(new Button("Save and Exit"));
		
		Scene scene = new Scene(menu, 250, 200);
		primaryStage.setTitle("CityLodge");
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
		
		Button btOK = new Button("Click me");
		Scene scene = new Scene(btOK, 200, 250);
		btOK.setOnAction(new CustomHandler(primaryStage, scene));
		primaryStage.setTitle("MyJavaFX"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}
	
	public static void main (String[] args) {
		
		Application.launch(args);
		
	}


}
