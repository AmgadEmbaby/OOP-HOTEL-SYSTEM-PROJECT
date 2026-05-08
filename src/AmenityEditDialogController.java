import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AmenityEditDialogController {

    @FXML private TextField    costField;
    @FXML private ToggleButton availBtn;
    @FXML private ToggleButton unavailBtn;
    @FXML private Label        imgPathLabel;
    @FXML private Label        errorLabel;

    private ManageAmenitiesController parent;
    private Amenity amenity;
    private String  pickedImagePath = null;

    // ================= WIRING =================

    public void setParent(ManageAmenitiesController parent) {
        this.parent = parent;
    }

    /**
     * Called by ManageAmenitiesController right after load()
     * to inject the amenity whose data we are editing.
     */
    public void setAmenity(Amenity a) {
        this.amenity = a;

        // Pre-fill fields
        costField.setText(String.valueOf(a.getAmenityCost()));

        // Wire the two toggle buttons as a group
        ToggleGroup tg = new ToggleGroup();
        availBtn.setToggleGroup(tg);
        unavailBtn.setToggleGroup(tg);

        if (a.isAvailable()) availBtn.setSelected(true);
        else                 unavailBtn.setSelected(true);

        // Show current image state
        String currentPath = a.getImagePath();
        if (currentPath == null || currentPath.equals("/fallback.jpg")) {
            imgPathLabel.setText("Using fallback image");
        } else {
            imgPathLabel.setText("Current image set");
        }
    }

    // ================= IMAGE PICKER =================

    @FXML
    private void pickImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Amenity Image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) costField.getScene().getWindow();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            pickedImagePath = file.getAbsolutePath();
            imgPathLabel.setText(file.getName());
            imgPathLabel.setStyle(
                    "-fx-text-fill: #8FAF7A; -fx-font-size: 11px; -fx-font-family: 'Montserrat Light';"
            );
        }
    }

    // ================= CONFIRM =================

    @FXML
    private void confirm() {
        try {
            double newCost  = Double.parseDouble(costField.getText().trim());
            boolean available = availBtn.isSelected();
            parent.updateAmenityFromDialog(amenity, newCost, available, pickedImagePath);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Cost must be a valid number.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    // ================= CANCEL =================

    @FXML
    private void cancel() {
        parent.hideOverlay();
    }
}
