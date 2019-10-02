package view;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import controller.Controller;
import controller.CustomHandler;
import controller.DatabaseController;
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
import model.database.CityLodgeDB;
import model.exceptions.DatabaseException;

public class CityLodgeMain extends Application{
	
	private CityLodgeDB DB;
	
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
		
		
		//Connect Database
		try {
			this.DB = new CityLodgeDB("src/database/CityLodgeDB.script/");
			//Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Connection to database successful");
			
		} catch (Exception e) {
			//Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Connection to database failed");
			e.printStackTrace();
		}
		
		// Initialise DBController
		DatabaseController DBController = new DatabaseController(this.DB);
		
		
		//TODO Add tables
		/*
		try {
			DBController.addRoomTable();
			//DBController.addSuiteTable();
			//DBController.addHiringRecordTable();
			//Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Tables added successfully");
		} catch (DatabaseException e) {
			//Change to javaFX alert
			JOptionPane.showMessageDialog(null, "Table adding failed");
			e.printStackTrace();
		}
		*/
		
		//TODO This doesn't work yet. 
		//DBController.showRoomTable();
		
		
		//Close program gracefuly
		try {
			System.out.println("Shutting down");
			DB.shutDown();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Shutting down failed");
		}
		
		//Rest of the program... 
		
		/*
		Button btOK = new Button("Click me");
		Scene scene = new Scene(btOK, 200, 250);
		btOK.setOnAction(new CustomHandler(primaryStage, scene));
		primaryStage.setTitle("MyJavaFX"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		*/
		
	}
	
	public static void main (String[] args) {
		
		Application.launch(args);
		
	}

}
