package net.malangbaram.DevLauncher.Util.javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlterUtil {

	public static void showInformationAlter(String title, String contents) {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(contents);
		alert.showAndWait();
	}
	
	public static void showErrorAlter(String title, String contents) {
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(contents);
		alert.showAndWait();
	}
}
