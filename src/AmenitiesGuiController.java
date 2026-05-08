import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AmenitiesGuiController implements Initializable {

    // ---- ADD AMENITY ----
    @FXML private TextField amenityNameField;
    @FXML private TextField amenityCostField;
    @FXML private Label amenityNameError;
    @FXML private Label amenityCostError;
    @FXML private Label addAmenityMessage;

    // ---- AMENITIES LIST ----
    @FXML private VBox amenitiesContainer;
    @FXML private Label amenityCountLabel;

    // ---- UPDATE COST ----
    @FXML private ComboBox<String> updateAmenityCombo;
    @FXML private TextField updateCostField;
    @FXML private Label updateCostMessage;

    // ---- TOGGLE AVAILABILITY ----
    @FXML private ComboBox<String> toggleAmenityCombo;
    @FXML private ComboBox<String> availabilityCombo;
    @FXML private Label toggleMessage;

    // ---- DELETE ----
    @FXML private ComboBox<String> deleteAmenityCombo;
    @FXML private Label deleteAmenityMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availabilityCombo.getItems().addAll("AVAILABLE", "UNAVAILABLE");
        availabilityCombo.getSelectionModel().selectFirst();

        refreshAllCombos();
        refreshAmenityList();
    }

    // ────────────────────────────────────────────────────
    //  ADD AMENITY
    // ────────────────────────────────────────────────────

    @FXML
    private void handleAddAmenity(ActionEvent event) {
        clearMessages();

        String name = amenityNameField.getText().trim();
        String costText = amenityCostField.getText().trim();
        boolean valid = true;

        if (name.isEmpty()) {
            amenityNameError.setText("Name is required.");
            valid = false;
        }

        double cost = 0;
        if (costText.isEmpty()) {
            amenityCostError.setText("Cost is required.");
            valid = false;
        } else {
            try {
                cost = Double.parseDouble(costText);
            } catch (NumberFormatException e) {
                amenityCostError.setText("Enter a valid number.");
                valid = false;
            }
        }

        if (!valid) return;

        try {
            Database.getAdmin().createAmenity(name, cost);
            addAmenityMessage.setStyle("-fx-text-fill: #768064;");
            addAmenityMessage.setText("✓  Amenity \"" + name + "\" added successfully.");
            amenityNameField.clear();
            amenityCostField.clear();
            refreshAllCombos();
            refreshAmenityList();
        } catch (Exception e) {
            addAmenityMessage.setText("Error: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────
    //  UPDATE COST
    // ────────────────────────────────────────────────────

    @FXML
    private void handleUpdateCost(ActionEvent event) {
        clearMessages();

        String selectedName = updateAmenityCombo.getValue();
        String costText = updateCostField.getText().trim();

        if (selectedName == null) {
            updateCostMessage.setText("Select an amenity.");
            return;
        }
        if (costText.isEmpty()) {
            updateCostMessage.setText("Enter a new cost.");
            return;
        }

        try {
            double newCost = Double.parseDouble(costText);
            Database.getAdmin().updateAmenityCost(newCost, selectedName);
            updateCostMessage.setStyle("-fx-text-fill: #768064;");
            updateCostMessage.setText("✓  Cost updated for \"" + selectedName + "\".");
            updateCostField.clear();
            refreshAmenityList();
        } catch (NumberFormatException e) {
            updateCostMessage.setText("Enter a valid number.");
        } catch (Exception e) {
            updateCostMessage.setText("Error: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────
    //  TOGGLE AVAILABILITY
    // ────────────────────────────────────────────────────

    @FXML
    private void handleToggleAvailability(ActionEvent event) {
        clearMessages();

        String selectedName = toggleAmenityCombo.getValue();
        String statusValue  = availabilityCombo.getValue();

        if (selectedName == null) {
            toggleMessage.setText("Select an amenity.");
            return;
        }

        try {
            boolean isAvailable = "AVAILABLE".equals(statusValue);
            Database.getAdmin().updateAmenityAvailability(isAvailable, selectedName);
            toggleMessage.setStyle("-fx-text-fill: #768064;");
            toggleMessage.setText("✓  \"" + selectedName + "\" marked as " + statusValue + ".");
            refreshAmenityList();
        } catch (Exception e) {
            toggleMessage.setText("Error: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────
    //  DELETE AMENITY
    // ────────────────────────────────────────────────────

    @FXML
    private void handleDeleteAmenity(ActionEvent event) {
        clearMessages();

        String selectedName = deleteAmenityCombo.getValue();

        if (selectedName == null) {
            deleteAmenityMessage.setText("Select an amenity to remove.");
            return;
        }

        try {
            Database.getAdmin().deleteAmenity(selectedName);
            deleteAmenityMessage.setStyle("-fx-text-fill: #768064;");
            deleteAmenityMessage.setText("✓  Amenity \"" + selectedName + "\" removed.");
            refreshAllCombos();
            refreshAmenityList();
        } catch (Exception e) {
            deleteAmenityMessage.setText("Error: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────
    //  HELPERS
    // ────────────────────────────────────────────────────

    private void refreshAllCombos() {
        updateAmenityCombo.getItems().clear();
        toggleAmenityCombo.getItems().clear();
        deleteAmenityCombo.getItems().clear();

        for (Amenity a : Database.getamenitiesList()) {
            String name = a.getAmenityName();
            updateAmenityCombo.getItems().add(name);
            toggleAmenityCombo.getItems().add(name);
            deleteAmenityCombo.getItems().add(name);
        }
    }

    private void refreshAmenityList() {
        amenitiesContainer.getChildren().clear();

        int count = Database.getamenitiesList().size();
        amenityCountLabel.setText(count + (count == 1 ? " AMENITY" : " AMENITIES"));

        for (Amenity amenity : Database.getamenitiesList()) {

            HBox row = new HBox();
            row.getStyleClass().add("activity-item");
            row.setPadding(new Insets(14, 16, 14, 16));
            row.setAlignment(Pos.CENTER_LEFT);

            boolean available = amenity.isAvailable();
            String availText  = available ? "AVAILABLE" : "UNAVAILABLE";
            String availColor = available ? "#768064" : "#8B6060";

            Label nameLbl  = styledLabel(amenity.getAmenityName(), 260, "#DADED8");
            Label costLbl  = styledLabel("$ " + String.format("%.2f", amenity.getAmenityCost()), 160, "#959581");
            Label availLbl = styledLabel(availText, 160, availColor);

            row.getChildren().addAll(nameLbl, costLbl, availLbl);
            amenitiesContainer.getChildren().add(row);
        }

        if (count == 0) {
            Label empty = new Label("No amenities yet. Add one above.");
            empty.getStyleClass().add("explanation-text");
            empty.setPadding(new Insets(20, 16, 20, 16));
            amenitiesContainer.getChildren().add(empty);
        }
    }

    private Label styledLabel(String text, double width, String color) {
        Label lbl = new Label(text);
        lbl.setPrefWidth(width);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-family: 'Montserrat Light'; -fx-font-size: 12px;");
        return lbl;
    }

    private void clearMessages() {
        amenityNameError.setText("");
        amenityCostError.setText("");
        addAmenityMessage.setText("");
        updateCostMessage.setText("");
        toggleMessage.setText("");
        deleteAmenityMessage.setText("");
    }
}