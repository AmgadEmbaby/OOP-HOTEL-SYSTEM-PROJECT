import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddRoomTypeDialogController {

    @FXML private TextField nameField;
    @FXML private TextField bedsField;
    @FXML private TextField capacityField;
    @FXML private TextField priceField;
    @FXML private TextField descField;
    @FXML private Label errorLabel;

    private ManageRoomTypesController parent;

    public void setParent(ManageRoomTypesController parent) {
        this.parent = parent;
    }

    @FXML
    private void confirm() {
        try {
            String name = nameField.getText().trim();
            int beds     = Integer.parseInt(bedsField.getText().trim());
            int capacity = Integer.parseInt(capacityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String desc  = descField.getText().trim();

            if (name.isEmpty()) {
                errorLabel.setText("Name cannot be empty.");
                return;
            }

            parent.addRoomTypeFromDialog(name, beds, capacity, desc, price);

        } catch (NumberFormatException ex) {
            errorLabel.setText("Beds, capacity and price must be valid numbers.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void cancel() {
        parent.hideOverlay();
    }
}