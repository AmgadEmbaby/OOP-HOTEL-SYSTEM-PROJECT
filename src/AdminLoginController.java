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




public class AdminLoginController {




        @FXML private TextField usernameField;
        @FXML private PasswordField passwordField;
        @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) throws Exception {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (Database.authenticateAdmin(username, password)) {
            Parent root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            // NO makeDraggable here
        } else {
            errorLabel.setText("Invalid username or password.");
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
