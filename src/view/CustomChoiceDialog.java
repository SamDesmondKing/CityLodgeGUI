package view;

import java.util.ArrayList;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.StageStyle;

public class CustomChoiceDialog extends ChoiceDialog<String> {
	
	//Strings
	public CustomChoiceDialog(String firstChoice, ArrayList<String> choices, String context, String question) {
		
		super(firstChoice, choices);
		
		this.setTitle("CityLodge");
		this.setHeaderText(context);
		this.setContentText(question);
		this.initStyle(StageStyle.UTILITY);
		
	}
	
}
