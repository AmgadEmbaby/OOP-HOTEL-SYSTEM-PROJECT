import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddAmenityDialogController {

    @FXML private TextField nameField;
    @FXML private TextField costField;
    @FXML private Label     imgPathLabel;
    @FXML private Label     errorLabel;

    private ManageAmenitiesController parent;
    private String pickedImagePath = null;

    public void setParent(ManageAmenitiesController parent) {
        this.parent = parent;
    }

    @FXML
    private void pickImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Amenity Image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) nameField.getScene().getWindow();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            pickedImagePath = file.getAbsolutePath();
            imgPathLabel.setText(file.getName());
            imgPathLabel.setStyle(
                    "-fx-text-fill: #8FAF7A; -fx-font-size: 11px; -fx-font-family: 'Montserrat Light';"
            );
        }
    }

    @FXML
    private void confirm() {
        String name      = nameField.getText().trim();
        String costInput = costField.getText().trim();

        if (name.isEmpty()) {
            errorLabel.setText("Amenity name cannot be empty.");
            return;
        }

        try {
            double cost = Double.parseDouble(costInput);
            parent.addAmenityFromDialog(name, cost, pickedImagePath);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Cost must be a valid number.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void cancel() {
        parent.hideOverlay();
    }
}
