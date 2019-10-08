package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;

public class AlertMessage extends Alert  {

	//Confirmation with two buttons
	public AlertMessage(AlertType alertType, String message, ButtonType one, ButtonType two) {
		super(alertType);
		
		this.setTitle(message);
		this.setHeaderText(message);
		this.setContentText("Choose your option.");

		ButtonType bCancel = new ButtonType("Cancel");

		this.getButtonTypes().setAll(one, two, bCancel);
		
		
	}


	
	
	
	
	
}
