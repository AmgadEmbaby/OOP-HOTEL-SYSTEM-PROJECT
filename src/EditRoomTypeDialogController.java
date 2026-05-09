import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditRoomTypeDialogController {

    @FXML private Label typeNameLabel;
    @FXML private TextField priceField;
    @FXML private Label errorLabel;

    private ManageRoomTypesController parent;
    private RoomType roomType;

    public void setParent(ManageRoomTypesController parent) {
        this.parent = parent;
    }

    public void setRoomType(RoomType rt) {
        this.roomType = rt;
        typeNameLabel.setText(rt.getTypeName().toUpperCase());
        priceField.setText(String.valueOf(rt.getPricePerNight()));
    }

    @FXML
    private void confirm() {
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            parent.updateRoomTypeFromDialog(roomType, price);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Price must be a valid number.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void cancel() {
        parent.hideOverlay();
    }
}