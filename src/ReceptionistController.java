import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

public class ReceptionistController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void showCheckIn(ActionEvent event) {
        loadSubScreen("Rec_CheckIn.fxml");
    }

    @FXML
    public void showCheckOut(ActionEvent event) {
        loadSubScreen("Rec_CheckOut.fxml");
    }

    @FXML
    public void showViewRooms(ActionEvent event) {
        loadSubScreen("Rec_ViewRooms.fxml");
    }

    private void loadSubScreen(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            System.out.println("Could not load: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}