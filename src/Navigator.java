import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigator {

    public void guestChoice(ActionEvent event)throws IOException {
        navigateTo(event, "GuestMenu.fxml");
    }

    public void switchBakcToChoices(ActionEvent event)throws IOException{
        navigateTo(event, "StartScreen.fxml");
    }
    public void adminChoice(ActionEvent event) throws IOException {
        navigateTo(event, "AdminLogin.fxml");
    }

    public void ReceptionistChoice(ActionEvent event)throws IOException {
        navigateTo(event, "ReceptionistScreen.fxml");
    }

    public void GobackToGuestMenu (ActionEvent event) throws IOException {
        navigateTo(event, "GuestMenu.fxml");
    }


    public void navigateTo(ActionEvent event, String fxmlFile) throws IOException {
        // Use getClass() to load the FXML relative to the current class
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

        // Get the current stage from the event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new scene and set it on the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        MainGui.makeDraggable(root, stage);
    }

    public void exitApp(ActionEvent event) {
        System.exit(0);
    }

}
