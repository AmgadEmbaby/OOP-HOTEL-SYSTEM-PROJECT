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

        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        MainGui.makeDraggable(root, stage);
    }

    public void exitApp(ActionEvent event) {
        System.exit(0);
    }

}
