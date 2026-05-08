import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookingScreenController implements Initializable {

    @FXML private Label guestNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label checkInLabel;
    @FXML private Label checkOutLabel;
    @FXML private Label nightsLabel;
    @FXML private Label roomCostLabel;
    @FXML private Label amenitiesCostLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;

    @FXML private FlowPane amenitiesContainer;
    @FXML private VBox selectedAmenitiesContainer;
    @FXML private VBox selectedAmenitiesSection;
    @FXML private Label selectedMethodLabel;
    @FXML private Label errorLabel;

    @FXML private Button cashBtn;
    @FXML private Button onlineBtn;
    @FXML private Button creditBtn;

    private Guests currentGuest;
    private RoomType selectedRoomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Invoices.PaymentMethod selectedMethod = null;
    private final List<Amenity> chosenAmenities = new ArrayList<>();

    private static final String BTN_SELECTED =
            "-fx-background-color: #768064; -fx-text-fill: #F5F0E8; " +
                    "-fx-border-color: #768064; -fx-border-width: 0.5; " +
                    "-fx-font-family: 'Cinzel'; -fx-font-size: 10px; " +
                    "-fx-letter-spacing: 2px; -fx-padding: 12 25; -fx-cursor: hand;";

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    public void initData(Guests guest, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        this.currentGuest  = guest;
        this.selectedRoomType = roomType;
        this.checkIn  = checkIn;
        this.checkOut = checkOut;

        populateSummary();
        populateAmenities();
        refreshSelectedAmenities();
    }

    private void populateSummary() {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) nights = 1;

        if(guestNameLabel != null) guestNameLabel.setText(currentGuest.getUserName());
        if(roomTypeLabel != null) roomTypeLabel.setText(selectedRoomType.getTypeName().toUpperCase());
        if(checkInLabel != null) checkInLabel.setText(checkIn.toString());
        if(checkOutLabel != null) checkOutLabel.setText(checkOut.toString());
        if(nightsLabel != null) nightsLabel.setText(nights + (nights == 1 ? " Night" : " Nights"));

        recalcTotals(nights);
    }

    private void recalcTotals(long nights) {
        double roomCost     = nights * selectedRoomType.getPricePerNight();
        double amenityCost  = chosenAmenities.stream().mapToDouble(Amenity::getAmenityCost).sum();
        double subtotal     = roomCost + amenityCost;
        double tax          = subtotal * 0.14;
        double total        = subtotal + tax;

        if(roomCostLabel != null) roomCostLabel.setText(String.format("$%.2f", roomCost));
        if(amenitiesCostLabel != null) amenitiesCostLabel.setText(String.format("$%.2f", amenityCost));
        if(taxLabel != null) taxLabel.setText(String.format("$%.2f", tax));
        if(totalLabel != null) totalLabel.setText(String.format("$%.2f", total));
    }

    private void populateAmenities() {
        if(amenitiesContainer == null) return;
        amenitiesContainer.getChildren().clear();

        for (Amenity a : Database.getamenitiesList()) {
            if (!a.isAvailable()) continue;

            Button btn = new Button(a.getAmenityName().toUpperCase() + "  +$" +
                    String.format("%.0f", a.getAmenityCost()));
            btn.getStyleClass().add("action-button");
            btn.setOnAction(e -> toggleAmenity(a, btn));
            amenitiesContainer.getChildren().add(btn);
        }
    }

    private void toggleAmenity(Amenity a, Button btn) {
        if (chosenAmenities.contains(a)) {
            chosenAmenities.remove(a);
            btn.setStyle("");
            btn.getStyleClass().setAll("action-button");
        } else {
            chosenAmenities.add(a);
            btn.setStyle(BTN_SELECTED);
            btn.getStyleClass().clear();
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) nights = 1;
        recalcTotals(nights);
        refreshSelectedAmenities();
    }

    private void refreshSelectedAmenities() {
        if(selectedAmenitiesContainer == null) return;
        selectedAmenitiesContainer.getChildren().clear();

        if (chosenAmenities.isEmpty()) {
            if(selectedAmenitiesSection != null) {
                selectedAmenitiesSection.setVisible(false);
                selectedAmenitiesSection.setManaged(false);
            }
            return;
        }

        if(selectedAmenitiesSection != null) {
            selectedAmenitiesSection.setVisible(true);
            selectedAmenitiesSection.setManaged(true);
        }

        for (Amenity a : chosenAmenities) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-padding: 10 16; -fx-background-color: #EDE8DC; " +
                    "-fx-border-color: #C5BBA8; -fx-border-width: 1;");

            Label name = new Label(a.getAmenityName().toUpperCase());
            name.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 11px; " +
                    "-fx-text-fill: #2C3424; -fx-letter-spacing: 2px;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            Label cost = new Label(String.format("+$%.2f", a.getAmenityCost()));
            cost.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 12px; -fx-text-fill: #768064;");

            Button remove = new Button("✕");
            remove.setStyle("-fx-background-color: transparent; -fx-text-fill: #8B3A3A; " +
                    "-fx-cursor: hand; -fx-font-size: 12px; -fx-padding: 0 0 0 12;");
            remove.setOnAction(e -> {
                if(amenitiesContainer != null) {
                    amenitiesContainer.getChildren().forEach(node -> {
                        if (node instanceof Button b &&
                                b.getText().startsWith(a.getAmenityName().toUpperCase())) {
                            b.setStyle("");
                            b.getStyleClass().setAll("action-button");
                        }
                    });
                }
                toggleAmenity(a, new Button());
            });

            row.getChildren().addAll(name, spacer, cost, remove);
            selectedAmenitiesContainer.getChildren().add(row);
        }
    }

    @FXML private void selectCash(ActionEvent e)   { setMethod(Invoices.PaymentMethod.CASH, cashBtn); }
    @FXML private void selectOnline(ActionEvent e) { setMethod(Invoices.PaymentMethod.ONLINE, onlineBtn); }
    @FXML private void selectCredit(ActionEvent e) { setMethod(Invoices.PaymentMethod.CREDIT_CARD, creditBtn); }

    private void setMethod(Invoices.PaymentMethod method, Button clicked) {
        selectedMethod = method;

        for (Button b : new Button[]{cashBtn, onlineBtn, creditBtn}) {
            if(b != null) {
                b.setStyle("");
                b.getStyleClass().setAll("action-button");
            }
        }

        if(clicked != null) {
            clicked.setStyle(BTN_SELECTED);
            clicked.getStyleClass().clear();
        }

        if(selectedMethodLabel != null) {
            selectedMethodLabel.setText("Selected: " + method.name().replace("_", " "));
            selectedMethodLabel.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 12px; -fx-text-fill: #768064;");
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        if(loader.getLocation() == null) loader = new FXMLLoader(getClass().getResource("GuestDashboard.fxml"));
        Parent root = loader.load();

        GuestDashboardController dc = loader.getController();
        dc.initData(currentGuest);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        if(errorLabel != null) errorLabel.setText("");

        if (selectedMethod == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a payment method before confirming.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            currentGuest.makeReservation(currentGuest, selectedRoomType, checkIn, checkOut, selectedMethod);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your reservation for a " + selectedRoomType.getTypeName().toUpperCase() + " has been confirmed!", ButtonType.OK);
            alert.setHeaderText("Reservation Successful");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
            if(loader.getLocation() == null) loader = new FXMLLoader(getClass().getResource("GuestDashboard.fxml"));
            Parent root = loader.load();

            GuestDashboardController dashboard = loader.getController();
            dashboard.initData(currentGuest);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Reservation Failed: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}