import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuestMenuController {

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
    private void openSignIn(ActionEvent event) {

        System.out.println("Sign In screen not created yet.");
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("Start.fxml")
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
    private void goBack(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

}
