import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainDashboardController {

    // These must match the fx:id you set in Scene Builder for your buttons
    @FXML
    private Button guestBtn;
    @FXML private Button receptionistBtn;
    @FXML private Button adminBtn;

    // The shared "Teleporter" method
    private void navigateTo(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile + ".fxml"));
            Stage stage = (Stage) receptionistBtn.getScene().getWindow(); // Use any button to get the stage
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error: Could not load " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReceptionistChoice() {
        navigateTo("ReceptionistLogin", "Receptionist Portal");
    }

    @FXML
    private void handleGuestChoice() {
        navigateTo("GuestLogin", "Guest Portal");
    }

    @FXML
    private void handleAdminChoice() {
        navigateTo("AdminLogin", "Admin Portal");
    }
}