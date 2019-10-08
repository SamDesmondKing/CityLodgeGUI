package view;

import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

public class CustomDialog extends TextInputDialog {

	//Text input dialogue
	public CustomDialog(String context, String question) {
		
		this.setTitle("CityLodge");
		this.setHeaderText(context);
		this.setContentText(question);
		this.initStyle(StageStyle.UTILITY);
		
	}
	
}
