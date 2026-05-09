import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReceptionistLoginController {

    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = userField.getText().trim();
        String pass = passField.getText().trim();

        // Check against the staffList in Database
        boolean authenticated = false;
        for (Staff s : Database.getStaffList()) {
            if (s.getUserName().equals(user) && s.getPassWord().equals(pass)) {
                if (s.getRole() == Staff.Role.RECEPTIONIST) {
                    Database.setLoggedInStaff(s);
                    // Store the current user in the session
                    UserSession.currentReceptionist = (Receptionist) s;
                    authenticated = true;
                    break;
                }
            }
        }

        if (authenticated) {
            loadDashboard(event);
        } else {
            errorLabel.setText("Invalid Username or Password.");
        }
    }

    private void loadDashboard(ActionEvent event) {
        loadView("ReceptionistDashboard");
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    private void loadView(String fxmlName) {
        try {
            String path = "/" + fxmlName + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Stage stage = (Stage) userField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setMaximized(true);

            MainGui.makeDraggable(root, stage);

        } catch (IOException e) {
            System.err.println("Could not find file: " + fxmlName);
            errorLabel.setText("System Error: Dashboard file not found.");
            e.printStackTrace();
        }
    }


}
