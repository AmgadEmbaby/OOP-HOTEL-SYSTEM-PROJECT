import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuestMenuController extends Navigator {

    @FXML
    private void openRegister(ActionEvent event) throws IOException {

        navigateTo(event, "Register.fxml");
    }

    @FXML
    private void openSignIn(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Guest Login..fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {

        navigateTo(event, "StartScreen.fxml");
    }



    @FXML
    private void exitApp() {

        System.exit(0);
    }

}
