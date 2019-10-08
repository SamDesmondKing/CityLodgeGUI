package view;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import controller.Controller;
import controller.CustomHandler;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.CityLodge;
import model.database.CityLodgeDB;
import model.exceptions.DatabaseException;

public class CityLodgeMain extends Application {

	private Controller controller;

	private Button btAdd;
	private Button btRent;
	private Button btReturn;
	private Button btMaint;
	private Button btMaintComplete;
	private Button btExport;
	private Button btImport;
	private Button btQuit;
	
	private GridPane pane;

	@Override
	public void start(Stage primaryStage) {

		//Connect CityLodgeBD
		this.connectDatabase();

		// Create a pane and set its properties
		this.createPane();
		// Add buttons
		this.createButtons();
		// Adding event handlers to buttons
		this.addHandlers();

		// Place list of rooms in the pane

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane);
		primaryStage.setTitle("CityLodge");
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.show();

		/*
		 * Scene switching example Button btOK = new Button("Click me"); Scene scene =
		 * new Scene(btOK, 200, 250); btOK.setOnAction(new CustomHandler(primaryStage,
		 * scene)); primaryStage.setTitle("MyJavaFX"); // Set the stage title
		 * primaryStage.setScene(scene); // Place the scene in the stage
		 * primaryStage.show(); // Display the stage
		 */

	}

	// Sends menu option to controller
	public void addHandlers() {
		
		//Add room
		this.btAdd.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(1);
		});
		//Rent room
		this.btRent.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(2);
		});
		//Return room
		this.btReturn.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(3);
		});
		//Room maintenance
		this.btMaint.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(4);
		});
		//Complete maintenance
		this.btMaintComplete.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(5);
		});
		//Export data
		this.btExport.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(6);
		});
		//Import Data
		this.btImport.setOnAction((ActionEvent e) -> {
			this.controller.handleClickEvents(7);
		});
		//Exit program
		this.btQuit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});
		
	}
	
	//Creates pane for main display and sets locally
	public void createPane() {
		
		this.pane = new GridPane();
		this.pane.setAlignment(Pos.TOP_LEFT);
		this.pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		this.pane.setHgap(5.5);
		this.pane.setVgap(5.5);
		
	}

	//Creates buttons for main display and sets locally
	public void createButtons() {

		this.btAdd = new Button("Add Room");
		this.btRent = new Button("Rent Room");
		this.btReturn = new Button("Return Room");
		this.btMaint = new Button("Room Maintenance");
		this.btMaintComplete = new Button("Complete Maintenance");
		this.btExport = new Button("Export Data");
		this.btImport = new Button("Import Data");
		this.btQuit = new Button("Quit");
		
		// Place menu buttons in the pane
		this.pane.add(this.btAdd, 0, 0);
		this.pane.add(this.btRent, 0, 1);
		this.pane.add(this.btReturn, 0, 2);
		this.pane.add(this.btMaint, 0, 3);
		this.pane.add(this.btMaintComplete, 0, 4);
		this.pane.add(this.btExport, 0, 6);
		this.pane.add(this.btImport, 0, 7);
		this.pane.add(this.btQuit, 0, 12);

	}

	// Initialises and connects Database, if sucessful, initialise rest of program.
	public void connectDatabase() {
		
		try {
			CityLodgeDB database = new CityLodgeDB();
			
			// On database sucess
			AlertMessage success = new AlertMessage(AlertType.INFORMATION, "Database Status", "Connection to Database Successful!");
			success.showAndWait();
			
			// Initialising program
			CityLodge citylodge = new CityLodge();
			this.controller = new Controller(citylodge, database);
			citylodge.setController(this.controller);

		} catch (Exception e) {

			Alert failure = new Alert(AlertType.WARNING);
			failure.setTitle("Database Status");
			failure.setHeaderText("Database Status");
			failure.setContentText("Connection to Database Failed. Program Aborting.");
			failure.showAndWait();

			e.printStackTrace();
			System.exit(1);
		}

		// Add all database tables here once, then never do it again.
		// Manipulate the database through the methods in the Controller class.

	}
	
	//Main method
	public static void main(String[] args) {

		Application.launch(args);
	}
}
