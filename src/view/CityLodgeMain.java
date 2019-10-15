package view;

import java.sql.SQLException;
import controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
import model.exceptions.InvalidInputException;

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
	private Button btDisplay;

	private GridPane pane;
	
	//TODO
	//
	/*	
	 *  THURSDAY
	 *  - Add ability to export data
	 *  - Add ability to import data
	 *  SATURDAY
	 *  - Create final GUI design
	 *  - Test.
	 */

	@Override
	public void start(Stage primaryStage) {

		// Connect CityLodgeBD
		try {
			CityLodgeDB database = new CityLodgeDB();
			// Initialise program
			CityLodge citylodge = new CityLodge();
			this.controller = new Controller(citylodge, database);
			citylodge.setController(this.controller);
			database.setController(this.controller);
			
			//Load data from database into citylodge array
			database.initialise();
			
		} catch (InvalidInputException e) {
			e.getError();

		} catch (Exception e) {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Database Status", "Connection to Database Failed. Program Aborting.");
			failure.showAndWait();
			e.printStackTrace();
			System.exit(1);
		}

		// ---- GUI ----
		
        primaryStage.setTitle("CityLodge Room Manager");

        ListView listView = new ListView();

        listView.getItems().add("Item 1");
        listView.getItems().add("Item 2");
        listView.getItems().add("Item 3");
        
		MenuBar menuBar = new MenuBar();
		Menu menuIcon = new Menu("Menu");
		
		MenuItem addRoom = new MenuItem("Add Room");
		MenuItem rentRoom = new MenuItem("Rent Room");
		MenuItem returnRoom = new MenuItem("Return Room");
		MenuItem beginMaint = new MenuItem("Begin Maintenance");
		MenuItem endMaint = new MenuItem("End Maintenance");
		MenuItem export = new MenuItem("Export Data");
		MenuItem inport = new MenuItem("Import Data");
		MenuItem display = new MenuItem("Display Room Data");
		MenuItem quit = new MenuItem("Quit");
		
        menuIcon.getItems().addAll(addRoom, rentRoom, returnRoom, beginMaint, endMaint, export, inport, display, quit);
        menuBar.getMenus().addAll(menuIcon);

		addRoom.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(1);
		});
		rentRoom.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(2);
		});
		returnRoom.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(3);
		});
		beginMaint.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(4);
		});
		endMaint.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(5);
		});
		export.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(6);
		});
		inport.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(7);
		});
		quit.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(8);
		});
		display.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(9);
		});
        
        BorderPane bpane = new BorderPane(listView);
        
        bpane.setTop(menuBar);

        Scene scene = new Scene(bpane, 800, 400);
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
	
	//Saves program to database on closing window.
	@Override
	public void stop() {
		
		System.out.println("Saving");
		try {
			this.controller.getDatabase().saveData();
			System.out.println("Saved");
			this.controller.getDatabase().shutdown();
			System.out.println("Shutting down");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Main method
	public static void main(String[] args) {

		Application.launch(args);
	}
}
