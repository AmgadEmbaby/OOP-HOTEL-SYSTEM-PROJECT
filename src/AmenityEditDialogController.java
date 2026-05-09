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


    public void setParent(ManageAmenitiesController parent) {
        this.parent = parent;
    }


    public void setAmenity(Amenity a) {
        this.amenity = a;


        costField.setText(String.valueOf(a.getAmenityCost()));


        ToggleGroup tg = new ToggleGroup();
        availBtn.setToggleGroup(tg);
        unavailBtn.setToggleGroup(tg);

        if (a.isAvailable()) availBtn.setSelected(true);
        else                 unavailBtn.setSelected(true);

        String currentPath = a.getImagePath();
        if (currentPath == null || currentPath.equals("/fallback.jpg")) {
            imgPathLabel.setText("Using fallback image");
        } else {
            imgPathLabel.setText("Current image set");
        }
    }



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



    @FXML
    private void cancel() {
        parent.hideOverlay();
    }
}
