package model.exceptions;

import javafx.scene.control.Alert.AlertType;
import view.AlertMessage;

@SuppressWarnings("serial")
public class RentalException extends Exception {
	
	private String errorMessage;
	private String action;
	
	//Constructor
	public RentalException(String errorMessage, String action) {
		this.errorMessage = errorMessage;
		this.action = action;
	}
	
	//Display error message
	public void getError() {
		
		AlertMessage failure = new AlertMessage(AlertType.WARNING, this.errorMessage,this.action);
		failure.showAndWait();
	}
}
