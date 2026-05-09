import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

public class ManageGuestsController {

    @FXML private VBox guestsContainer;
    @FXML private VBox guestReservationsContainer;
    @FXML private TextField searchField;

    @FXML private Label avatarLabel;
    @FXML private Label detailName;
    @FXML private Label detailCity;
    @FXML private Label detailDob;
    @FXML private Label detailGender;
    @FXML private Label detailBalance;
    @FXML private Label detailReservations;

    private List<Guests> allGuests = new ArrayList<>();

    @FXML
    public void initialize() {
        allGuests.addAll(Database.getGuestList());
        loadGuests(allGuests);
    }



    private void loadGuests(List<Guests> list) {
        guestsContainer.getChildren().clear();
        for (Guests g : list) {
            guestsContainer.getChildren().add(createRow(g));
        }
    }



    private HBox createRow(Guests g) {


        Label avatar = new Label(g.getUserName().substring(0, 1).toUpperCase());
        avatar.setStyle("""
            -fx-background-color: rgba(90,140,90,0.20);
            -fx-background-radius: 50;
            -fx-min-width: 44; -fx-min-height: 44;
            -fx-max-width: 44; -fx-max-height: 44;
            -fx-alignment: center;
            -fx-font-family: 'Zaslia';
            -fx-font-size: 20px;
            -fx-text-fill: #d7dfd0;
            -fx-border-color: rgba(118,128,100,0.25);
            -fx-border-radius: 50;
            -fx-border-width: 1;
        """);


        Label name = new Label(g.getUserName().toUpperCase());
        name.setStyle("""
            -fx-font-family: 'Cinzel';
            -fx-font-size: 13px;
            -fx-text-fill: #F3EFE6;
            -fx-letter-spacing: 1px;
        """);

        Label city = new Label(g.getAddress() + "  ·  " +
                g.getGender().toString().toUpperCase());
        city.setStyle("""
            -fx-font-family: 'Montserrat Light';
            -fx-font-size: 11px;
            -fx-text-fill: rgba(218,222,216,0.55);
        """);

        VBox nameBox = new VBox(4, name, city);
        nameBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameBox, Priority.ALWAYS);


        long resCount = Database.getReservationsList().stream()
                .filter(r -> r.getGuest().getUserName()
                        .equalsIgnoreCase(g.getUserName()))
                .count();

        Label badge = new Label(resCount + " RES");
        badge.setStyle("""
            -fx-background-color: rgba(90,140,90,0.18);
            -fx-background-radius: 20;
            -fx-border-color: rgba(118,128,100,0.25);
            -fx-border-radius: 20;
            -fx-border-width: 0.7;
            -fx-text-fill: #8FAF7A;
            -fx-font-family: 'Cinzel';
            -fx-font-size: 10px;
            -fx-padding: 4 10 4 10;
        """);

        HBox row = new HBox(14, avatar, nameBox, badge);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(118,128,100,0.14);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """);

        row.setOnMouseEntered(e -> row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(218,222,216,0.18);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """));

        row.setOnMouseExited(e -> row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 14;
            -fx-border-radius: 14;
            -fx-border-color: rgba(118,128,100,0.14);
            -fx-border-width: 0.7;
            -fx-cursor: hand;
        """));

        row.setOnMouseClicked(e -> showProfile(g));

        return row;
    }


    private void showProfile(Guests g) {
        avatarLabel.setText(g.getUserName().substring(0, 1).toUpperCase());
        detailName.setText(g.getUserName());
        detailCity.setText(g.getAddress());
        detailDob.setText(g.getDateOfBirth().toString());
        detailGender.setText(g.getGender().toString().toUpperCase());
        detailBalance.setText("$" + String.format("%.2f", g.getBalance()));

        long resCount = Database.getReservationsList().stream()
                .filter(r -> r.getGuest().getUserName()
                        .equalsIgnoreCase(g.getUserName()))
                .count();
        detailReservations.setText(resCount + " reservations");


        guestReservationsContainer.getChildren().clear();
        for (Reservations r : Database.getReservationsList()) {
            if (r.getGuest().getUserName().equalsIgnoreCase(g.getUserName())) {
                guestReservationsContainer.getChildren().add(createMiniResRow(r));
            }
        }

        if (guestReservationsContainer.getChildren().isEmpty()) {
            Label none = new Label("No reservations found.");
            none.setStyle("""
                -fx-text-fill: rgba(218,222,216,0.4);
                -fx-font-family: 'Montserrat Light';
                -fx-font-size: 12px;
                -fx-padding: 8 0 0 0;
            """);
            guestReservationsContainer.getChildren().add(none);
        }
    }



    private HBox createMiniResRow(Reservations r) {
        Label type = new Label(r.getTypeDesired().getTypeName().toUpperCase());
        type.setStyle("""
            -fx-font-family: 'Cinzel';
            -fx-font-size: 11px;
            -fx-text-fill: #d7dfd0;
        """);

        Label dates = new Label(r.getCheckin() + " → " + r.getCheckout());
        dates.setStyle("""
            -fx-font-family: 'Montserrat Light';
            -fx-font-size: 11px;
            -fx-text-fill: rgba(218,222,216,0.5);
        """);
        HBox.setHgrow(dates, Priority.ALWAYS);

        Label status = new Label(r.getStatus().toString());
        status.getStyleClass().add(
                r.getStatus() == Reservations.ReservationStatus.CONFIRMED
                        ? "status-available" : "status-occupied"
        );

        HBox row = new HBox(12, type, dates, status);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: rgba(118,128,100,0.12);
            -fx-border-width: 0.7;
        """);
        return row;
    }



    @FXML
    private void filterGuests() {
        String text = searchField.getText().trim().toLowerCase();
        if (text.isBlank()) {
            loadGuests(allGuests);
            return;
        }
        List<Guests> filtered = new ArrayList<>();
        for (Guests g : allGuests) {
            if (g.getUserName().toLowerCase().contains(text) ||
                    g.getAddress().toLowerCase().contains(text) ||
                    g.getGender().toString().toLowerCase().contains(text)) {
                filtered.add(g);
            }
        }
        loadGuests(filtered);
    }
}