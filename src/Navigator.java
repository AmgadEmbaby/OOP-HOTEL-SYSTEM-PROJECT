import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigator {
    @FXML private Button receptionistBtn;
    public void guestChoice(ActionEvent event)throws IOException {
        navigateTo(event, "GuestMenu.fxml");
    }

    public void switchBakcToChoices(ActionEvent event)throws IOException{
        navigateTo(event, "StartScreen.fxml");
    }
    public void adminChoice(ActionEvent event) throws IOException {
        navigateTo(event, "AdminLogin.fxml");
    }



    public void GobackToGuestMenu (ActionEvent event) throws IOException {
        navigateTo(event, "GuestMenu.fxml");
    }


    public void navigateTo(ActionEvent event, String fxmlFile) throws IOException {
        // Use getClass() to load the FXML relative to the current class
        Parent root = FXMLLoader.load(getClass().getResource("/" + fxmlFile));

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

    @FXML
    private void handleReceptionistChoice(ActionEvent event) {
        try {
            // This loads the username/password entry screen
            Parent root = FXMLLoader.load(getClass().getResource("ReceptionistLogin.fxml"));
            Stage stage = (Stage) receptionistBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
