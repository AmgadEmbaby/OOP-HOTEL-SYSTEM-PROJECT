import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import java.io.IOException;

public class ReceptionistDashboardController {

// as if I am telling the program " look at the FXML file , find the box named "contentArea" and let me control it here .
    @FXML private StackPane contentArea;

    // This runs automatically when the dashboard opens
    // Inside ReceptionistDashboardController.java
    @FXML private Label receptionistNameLabel;
    //these variables are created to store the screens in memory so they load when clicked
    private Parent checkInView;
    private Parent checkOutView;
    private Parent ViewRooms;

    public void initialize() {
        Staff current = Database.getLoggedInStaff();
        if (current != null && receptionistNameLabel != null) {
            receptionistNameLabel.setText("HELLO, " + current.getName().toUpperCase());
        }

        // Pre-load all views once — background never reloads again
        checkInView  = preload("CheckInView.fxml");
        checkOutView = preload("CheckOutView.fxml");
        ViewRooms    = preload("ViewRooms.fxml");

        // Show check-in by default
        if (checkInView != null) {
            contentArea.getChildren().setAll(checkInView);
        }
    }

    @FXML
    private void showCheckIn() {
        loadView("CheckInView.fxml");
    }

    @FXML
    private void showCheckOut() {
        loadView("CheckOutView.fxml");
    }

    @FXML
    private void showRooms() {
        loadView("ViewRooms.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            // 1. Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReceptionistLogin.fxml"));
            Parent root = loader.load();

            // 2. Get the current Stage (Window)
            javafx.stage.Stage stage = (javafx.stage.Stage) contentArea.getScene().getWindow();

            // 3. REVERT TO STARTING SIZE
            stage.setMaximized(false); // Disable full screen
            stage.setWidth(1000);      // Set back to your original width
            stage.setHeight(600);     // Set back to your original height
            stage.centerOnScreen();

            // 4. Switch the scene
            stage.getScene().setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadView(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
            Parent root = loader.load();
            contentArea.getChildren().setAll(root);
        } catch (IOException e) {
            System.err.println("Failed to load " + fxmlName);
            e.printStackTrace();
        }
    }

    private void show(Parent view) {
        if (view != null) {
            contentArea.getChildren().setAll(view);
        }
    }

    private Parent preload(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
            return loader.load();
        } catch (IOException e) {
            System.err.println("Failed to preload " + fxmlName);
            e.printStackTrace();
            return null;
        }
    }

}