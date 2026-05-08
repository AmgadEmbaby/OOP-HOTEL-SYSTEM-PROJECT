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
    private void openRegister(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("Register.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void openSignIn(ActionEvent event) throws IOException {
        //navigateTo(event,"GuestLogin.fxml");

        Parent root = FXMLLoader.load(getClass().getResource("Guest Login..fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

    }

    @FXML
    private void backToMainMenu(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("StartScreen.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void exitApp() {

        System.exit(0);
    }

}
