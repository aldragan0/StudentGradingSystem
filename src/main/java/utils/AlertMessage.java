package utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertMessage {
    /**
     * creates a new alertMessage and displays it on the screen
     * @param owner stage which owns the message
     * @param type type of the alert message
     * @param header title of the alert message
     * @param text content of the alert message
     */
    public static void showMessage(Stage owner, Alert.AlertType type,
                                   String header, String text){
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
