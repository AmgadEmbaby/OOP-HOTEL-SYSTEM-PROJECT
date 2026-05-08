import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        guestNameLabel.setText(currentGuest.getUserName());
        roomTypeLabel.setText(selectedRoomType.getTypeName().toUpperCase());
        checkInLabel.setText(checkIn.toString());
        checkOutLabel.setText(checkOut.toString());
        nightsLabel.setText(nights + (nights == 1 ? " Night" : " Nights"));

        recalcTotals(nights);
    }

    private void recalcTotals(long nights) {
        double roomCost     = nights * selectedRoomType.getPricePerNight();
        double amenityCost  = chosenAmenities.stream().mapToDouble(Amenity::getAmenityCost).sum();
        double subtotal     = roomCost + amenityCost;
        double tax          = subtotal * 0.14;
        double total        = subtotal + tax;

        roomCostLabel.setText(String.format("$%.2f", roomCost));
        amenitiesCostLabel.setText(String.format("$%.2f", amenityCost));
        taxLabel.setText(String.format("$%.2f", tax));
        totalLabel.setText(String.format("$%.2f", total));
    }


    private void populateAmenities() {
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
        selectedAmenitiesContainer.getChildren().clear();

        if (chosenAmenities.isEmpty()) {
            selectedAmenitiesSection.setVisible(false);
            selectedAmenitiesSection.setManaged(false);
            return;
        }

        selectedAmenitiesSection.setVisible(true);
        selectedAmenitiesSection.setManaged(true);

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
                // find and un-select the matching amenity button in FlowPane
                amenitiesContainer.getChildren().forEach(node -> {
                    if (node instanceof Button b &&
                        b.getText().startsWith(a.getAmenityName().toUpperCase())) {
                        b.setStyle("");
                        b.getStyleClass().setAll("action-button");
                    }
                });
                toggleAmenity(a, new Button()); // removes from list + recalcs
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

        // Reset all 3 buttons
        for (Button b : new Button[]{cashBtn, onlineBtn, creditBtn}) {
            b.setStyle("");
            b.getStyleClass().setAll("action-button");
        }
        clicked.setStyle(BTN_SELECTED);
        clicked.getStyleClass().clear();

        selectedMethodLabel.setText("Selected: " + method.name().replace("_", " "));
        selectedMethodLabel.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 12px; -fx-text-fill: #768064;");
    }
    @FXML
    private void goBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
        Parent root = loader.load();

        GuestDashboardController dc = loader.getController();
        dc.initData(currentGuest);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        errorLabel.setText("");

        if (selectedMethod == null) {
            errorLabel.setText("Please select a payment method.");
            return;
        }

        try {
            currentGuest.makeReservation(currentGuest, selectedRoomType, checkIn, checkOut, selectedMethod);


            Reservations newRes = currentGuest.getGuestReservations()
                    .get(currentGuest.getGuestReservations().size() - 1);



            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestDashboard.fxml"));
            Parent root = loader.load();

            GuestDashboardController dashboard = loader.getController();
            dashboard.initData(currentGuest);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage() != null ? ex.getMessage() : "Reservation failed.");
        }

    }


}