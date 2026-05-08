import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuestLoginController extends Navigator {

    // 1. Grab the UI elements from the FXML
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleSignIn(ActionEvent event) {
        String enteredUser = usernameField.getText();
        String enteredPass = passwordField.getText();

        try {
            Guests loggedInGuest = GuestValidation.validateGuestLogin(enteredUser, enteredPass);

            errorLabel.setStyle("-fx-text-fill: #768064;");
            errorLabel.setText("Login Successful! Welcome " + loggedInGuest.getUserName());

            try{
                navigateTo(event,"GuestDashboard.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IllegalArgumentException e) {
            errorLabel.setStyle("-fx-text-fill: #d6b8b8;");
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void createAcc(ActionEvent event) throws Exception {
        // Switch to the Registration Screen
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        // Return to Start Screen
        Parent root = FXMLLoader.load(getClass().getResource("GuestMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }
}