package view;

import java.sql.SQLException;
import java.util.ArrayList;

import controller.Controller;
import controller.ExportHandler;
import controller.ImportHandler;
import controller.ListController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.CityLodge;
import model.Room;
import model.database.CityLodgeDB;
import model.exceptions.InvalidInputException;

public class CityLodgeMain extends Application {

	private Controller controller;
	private ListController lc;
	private ListView<String> listView;

	// TODO
	/*
	 * SATURDAY - Create final GUI design - Test. (Go over imports again - Suites).
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

			// Load data from database into citylodge array
			database.initialise();

		} catch (InvalidInputException e) {
			e.getError();

		} catch (Exception e) {
			AlertMessage failure = new AlertMessage(AlertType.WARNING, "Database Status",
					"Connection to Database Failed. Program Aborting.");
			failure.showAndWait();
			e.printStackTrace();
			System.exit(1);
		}

		// ---- GUI ----

		primaryStage.setTitle("CityLodge Room Manager");
		
		this.listView = null;
		this.lc = new ListController(this.controller, primaryStage);
		
		try {
			this.listView = this.lc.createListView();
		} catch (InvalidInputException e1) {
			e1.printStackTrace();
		}
		
	
		MenuBar menuBar = new MenuBar();
		Menu menuIcon = new Menu("Menu");
		MenuItem addRoom = new MenuItem("Add Room");
		MenuItem rentRoom = new MenuItem("Rent Room");
		MenuItem returnRoom = new MenuItem("Return Room");
		MenuItem beginMaint = new MenuItem("Begin Maintenance");
		MenuItem endMaint = new MenuItem("End Maintenance");
		MenuItem export = new MenuItem("Export Data");
		MenuItem importData = new MenuItem("Import Data");
		MenuItem display = new MenuItem("Display Room Data");
		MenuItem quit = new MenuItem("Quit");

		menuIcon.getItems().addAll(addRoom, rentRoom, returnRoom, beginMaint, endMaint, export, importData, display,
				quit);
		menuBar.getMenus().addAll(menuIcon);

		// Using lambda expressions to send button clicks through
		// to the main Controller class, which calls CityLodge methods.
		// Knowledge of custom handler classes also demonstrated below.
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
		quit.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(8);
		});

		display.setOnAction((ActionEvent e) -> {
			this.controller.mainMenu(9);
		});

		// Export data custom handler
		export.setOnAction(new ExportHandler(this.controller, primaryStage));

		// Import data custom handler
		importData.setOnAction(new ImportHandler(this.controller, primaryStage));

		// Rest of the GUI.
		
		BorderPane bpane = new BorderPane(listView);
		bpane.setTop(menuBar);
		Scene scene = new Scene(bpane, 820, 400);
		lc.setScene(scene);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.show();

	}
	
	// Saves program to database on closing window.
	@Override
	public void stop() {

		try {
			this.controller.getDatabase().saveData();
			this.controller.getDatabase().shutdown();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	// Main method
	public static void main(String[] args) {

		Application.launch(args);
	}
}
