import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewRoomsController {

    @FXML private FlowPane roomsContainer;
    @FXML private StackPane dialogOverlay;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilter;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Label resultsLabel;



    @FXML
    public void initialize() {
        typeFilter.getItems().addAll("ALL TYPES", "Single", "Double", "Suite");
        typeFilter.getSelectionModel().selectFirst();

        statusFilter.getItems().addAll("ALL STATUSES", "AVAILABLE", "OCCUPIED");
        statusFilter.getSelectionModel().selectFirst();

        searchField.textProperty().addListener((obs, o, n) -> applyFilters());
        typeFilter.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> applyFilters());
        statusFilter.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> applyFilters());

        hideOverlay();
        loadRooms(Database.getRoomList());

        roomsContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                roomsContainer.visibleProperty().addListener((o, wasVisible, isNowVisible) -> {
                    if (isNowVisible) loadRooms(Database.getRoomList());
                });
            }
        });
    }



    @FXML
    private void clearFilters() {
        searchField.clear();
        typeFilter.getSelectionModel().selectFirst();
        statusFilter.getSelectionModel().selectFirst();
    }

    private void applyFilters() {
        String search    = searchField.getText().trim().toLowerCase();
        String typeVal   = typeFilter.getValue();
        String statusVal = statusFilter.getValue();

        List<Rooms> filtered = Database.getRoomList().stream()
                .filter(r -> {
                    // Search by room number string
                    if (!search.isEmpty() &&
                            !r.getRoomNumber().toLowerCase().contains(search)) return false;

                    // Type filter
                    if (typeVal != null && !typeVal.equalsIgnoreCase("ALL TYPES") &&
                            !r.getRoomtype().getTypeName().equalsIgnoreCase(typeVal)) return false;

                    // Status filter
                    if (statusVal != null && !statusVal.equalsIgnoreCase("ALL STATUSES") &&
                            !r.getStatus().toString().equalsIgnoreCase(statusVal)) return false;

                    return true;
                })
                .collect(Collectors.toList());

        int count = filtered.size();
        resultsLabel.setText(count + (count == 1 ? " ROOM FOUND" : " ROOMS FOUND"));
        loadRooms(filtered);
    }



    private void loadRooms(List<Rooms> rooms) {
        roomsContainer.getChildren().clear();
        for (Rooms r : rooms) {
            roomsContainer.getChildren().add(createCard(r));
        }
    }

    private VBox createCard(Rooms room) {

        ImageView imageView = new ImageView();
        imageView.setFitWidth(340);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);

        String typeName  = room.getRoomtype().getTypeName().toLowerCase();
        String imagePath = switch (typeName) {
            case "suite"  -> "/images/suite.jpg";
            case "double" -> "/images/double.jpg";
            default       -> "/images/single.jpg";
        };

        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.out.println("Missing image: " + imagePath);
        }

        Rectangle clip = new Rectangle(340, 200);
        clip.setArcWidth(26);
        clip.setArcHeight(26);
        imageView.setClip(clip);

        VBox card = new VBox();
        card.getStyleClass().add("room-card");
        card.setPrefWidth(340);
        card.setMaxWidth(340);
        card.setMinWidth(340);
        card.setCursor(javafx.scene.Cursor.HAND);

        VBox info = new VBox(8);
        info.setPadding(new Insets(16, 18, 16, 18));

        Label title = new Label(room.getRoomtype().getTypeName().toUpperCase());
        title.getStyleClass().add("room-card-title");
        title.setStyle(title.getStyle() +
                "; -fx-font-family: 'Cinzel'; -fx-font-size: 15px; -fx-text-fill: #DADED8;");

        Label num   = new Label("ROOM  " + room.getRoomNumber());
        Label floor = new Label("FLOOR  " + room.getRoomFloor());
        num.getStyleClass().add("room-card-sub");
        floor.getStyleClass().add("room-card-sub");

        // Price comes from RoomType.getPricePerNight()
        Label price = new Label("$" + String.format("%.0f", room.getRoomtype().getPricePerNight()) + " / NIGHT");
        price.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-text-fill: #d4af37;");

        Label status = new Label(room.getStatus().toString());
        status.getStyleClass().add(
                room.getStatus() == Rooms.RoomStatus.AVAILABLE
                        ? "status-available" : "status-occupied"
        );

        Label hint = new Label("CLICK TO VIEW DETAILS  →");
        hint.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 9px; " +
                "-fx-text-fill: rgba(212,175,55,0.5); -fx-letter-spacing: 1.5px; -fx-padding: 6 0 0 0;");

        info.getChildren().addAll(title, num, floor, price, status, hint);
        card.getChildren().addAll(imageView, info);

        // Hover gold glow
        DropShadow hoverGlow = new DropShadow(16, Color.web("#d4af37", 0.0));
        card.setEffect(hoverGlow);
        card.setOnMouseEntered(e ->
                new Timeline(new KeyFrame(Duration.millis(150),
                        new KeyValue(hoverGlow.colorProperty(), Color.web("#d4af37", 0.45)),
                        new KeyValue(hoverGlow.radiusProperty(), 22.0)
                )).play()
        );
        card.setOnMouseExited(e ->
                new Timeline(new KeyFrame(Duration.millis(150),
                        new KeyValue(hoverGlow.colorProperty(), Color.web("#d4af37", 0.0)),
                        new KeyValue(hoverGlow.radiusProperty(), 6.0)
                )).play()
        );

        card.setOnMouseClicked(e -> openRoomDetail(room));
        return card;
    }

    private Image loadRoomImage(String typeName) {
        String imagePath = switch (typeName.toLowerCase()) {
            case "suite"  -> "/suite.jpg";
            case "double" -> "/double.jpg";
            default       -> "/single.jpg";
        };

        // Try 1: with leading slash (classpath root)
        var stream = getClass().getResourceAsStream(imagePath);

        // Try 2: without leading slash
        if (stream == null) {
            stream = getClass().getResourceAsStream(imagePath.substring(1));
        }

        // Try 3: from ClassLoader
        if (stream == null) {
            stream = getClass().getClassLoader().getResourceAsStream(imagePath.substring(1));
        }

        if (stream == null) {
            System.out.println("IMAGE NOT FOUND for type='" + typeName + "' path='" + imagePath + "'");
            System.out.println("Make sure " + imagePath.substring(1) + " is inside your src/ folder and was copied to output by your build tool.");
            return null;
        }

        try {
            return new Image(stream);
        } catch (Exception e) {
            System.out.println("IMAGE LOAD ERROR: " + e.getMessage());
            return null;
        }
    }

    private void showOverlay(javafx.scene.Parent content) {
        StackPane.setAlignment(content, Pos.CENTER);
        dialogOverlay.setAlignment(Pos.CENTER);
        dialogOverlay.getChildren().setAll(content);
        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);

        GaussianBlur blur = new GaussianBlur(0);
        roomsContainer.setEffect(blur);
        new Timeline(new KeyFrame(Duration.millis(180),
                new KeyValue(blur.radiusProperty(), 18))).play();
    }

    @FXML
    public void hideOverlay() {
        roomsContainer.setEffect(null);
        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();
    }



    private void openRoomDetail(Rooms room) {

        VBox panel = new VBox(0);
        panel.setMaxWidth(520);
        panel.setMinWidth(520);
        panel.setStyle(
                "-fx-background-color: rgba(20,26,17,0.97);" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: rgba(212,175,55,0.5);" +
                        "-fx-border-width: 1.5;"
        );
        DropShadow panelShadow = new DropShadow();
        panelShadow.setColor(Color.web("#d4af37", 0.35));
        panelShadow.setRadius(28);
        panel.setEffect(panelShadow);

        String typeName  = room.getRoomtype().getTypeName().toLowerCase();
        String imagePath = switch (typeName) {
            case "suite"  -> "/images/suite.jpg";
            case "double" -> "/images/double.jpg";
            default       -> "/images/single.jpg";
        };
        ImageView hero = new ImageView();
        hero.setFitWidth(520);
        hero.setFitHeight(180);
        hero.setPreserveRatio(false);
        try { hero.setImage(new Image(getClass().getResourceAsStream(imagePath))); }
        catch (Exception ignored) {}
        Rectangle heroClip = new Rectangle(520, 180);
        heroClip.setArcWidth(22);
        heroClip.setArcHeight(22);
        hero.setClip(heroClip);
        panel.getChildren().add(hero);

        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(22, 40, 16, 40));
        titleBox.setStyle("-fx-border-color: transparent transparent rgba(212,175,55,0.15) transparent; " +
                "-fx-border-width: 0 0 1 0;");

        Label typeLabel = new Label(room.getRoomtype().getTypeName().toUpperCase() + "  ROOM");
        typeLabel.setStyle("-fx-font-family: 'Zaslia'; -fx-font-size: 32px; -fx-text-fill: #DADED8;");

        HBox roomMeta = new HBox(14);
        roomMeta.setAlignment(Pos.CENTER);
        Label roomNumLbl = new Label("ROOM " + room.getRoomNumber());
        Label dot        = new Label("·");
        Label floorLbl   = new Label("FLOOR " + room.getRoomFloor());
        roomNumLbl.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-text-fill: #d4af37;");
        floorLbl.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-text-fill: #d4af37;");
        dot.setStyle("-fx-text-fill: rgba(212,175,55,0.35); -fx-font-size: 16px;");

        Label statusBadge = new Label("  " + room.getStatus().toString() + "  ");
        statusBadge.setStyle(
                room.getStatus() == Rooms.RoomStatus.AVAILABLE
                        ? "-fx-background-color: rgba(80,160,80,0.2); -fx-text-fill: #7ec97e; " +
                        "-fx-border-color: rgba(80,160,80,0.4); -fx-border-radius: 20; " +
                        "-fx-background-radius: 20; -fx-font-family: 'Cinzel'; " +
                        "-fx-font-size: 10px; -fx-padding: 4 10;"
                        : "-fx-background-color: rgba(180,80,80,0.2); -fx-text-fill: #e07070; " +
                        "-fx-border-color: rgba(180,80,80,0.4); -fx-border-radius: 20; " +
                        "-fx-background-radius: 20; -fx-font-family: 'Cinzel'; " +
                        "-fx-font-size: 10px; -fx-padding: 4 10;"
        );

        roomMeta.getChildren().addAll(roomNumLbl, dot, floorLbl, statusBadge);
        titleBox.getChildren().addAll(typeLabel, roomMeta);
        panel.getChildren().add(titleBox);

        VBox detailsRow = new VBox(6);
        detailsRow.setPadding(new Insets(14, 40, 14, 40));
        detailsRow.setStyle("-fx-border-color: transparent transparent rgba(212,175,55,0.12) transparent; " +
                "-fx-border-width: 0 0 1 0;");

        HBox bedsAndCap = new HBox(30);
        bedsAndCap.setAlignment(Pos.CENTER_LEFT);

        VBox bedsBox = new VBox(3);
        Label bedsCaption = new Label("BEDS");
        bedsCaption.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 9px; " +
                "-fx-text-fill: #768064; -fx-letter-spacing: 2px;");
        Label bedsVal = new Label(String.valueOf(room.getRoomtype().getNumberOfBeds()));
        bedsVal.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 16px; -fx-text-fill: #DADED8;");
        bedsBox.getChildren().addAll(bedsCaption, bedsVal);

        VBox capBox = new VBox(3);
        Label capCaption = new Label("MAX GUESTS");
        capCaption.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 9px; " +
                "-fx-text-fill: #768064; -fx-letter-spacing: 2px;");
        Label capVal = new Label(String.valueOf(room.getRoomtype().getCapacity()));
        capVal.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 16px; -fx-text-fill: #DADED8;");
        capBox.getChildren().addAll(capCaption, capVal);

        bedsAndCap.getChildren().addAll(bedsBox, capBox);

        Label descLabel = new Label(room.getRoomtype().getRoomDescription());
        descLabel.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 11px; " +
                "-fx-text-fill: #768064;");
        descLabel.setWrapText(true);

        detailsRow.getChildren().addAll(bedsAndCap, descLabel);
        panel.getChildren().add(detailsRow);

        HBox priceBanner = new HBox();
        priceBanner.setAlignment(Pos.CENTER);
        priceBanner.setPadding(new Insets(14, 40, 14, 40));
        priceBanner.setStyle(
                "-fx-background-color: rgba(212,175,55,0.07);" +
                        "-fx-border-color: transparent transparent rgba(212,175,55,0.15) transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );
        Label priceCaption = new Label("RATE PER NIGHT");
        priceCaption.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 9px; " +
                "-fx-text-fill: #768064; -fx-letter-spacing: 2px;");
        Region priceSpring = new Region();
        HBox.setHgrow(priceSpring, Priority.ALWAYS);
        Label priceValue = new Label("$" + String.format("%.2f", room.getRoomtype().getPricePerNight()));
        priceValue.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 22px; -fx-text-fill: #d4af37;");
        priceBanner.getChildren().addAll(priceCaption, priceSpring, priceValue);
        panel.getChildren().add(priceBanner);

        VBox amenitiesSection = new VBox(10);
        amenitiesSection.setPadding(new Insets(18, 40, 14, 40));
        amenitiesSection.setStyle("-fx-border-color: transparent transparent rgba(212,175,55,0.12) transparent; " +
                "-fx-border-width: 0 0 1 0;");

        Label amenHeader = new Label("AMENITIES");
        amenHeader.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 10px; " +
                "-fx-text-fill: #768064; -fx-letter-spacing: 3px;");

        FlowPane amenFlow = new FlowPane(10, 8);
        ArrayList<Amenity> amenities = room.getAmenities();
        if (amenities != null && !amenities.isEmpty()) {
            for (Amenity amenity : amenities) {
                String display = amenity.getAmenityName().toUpperCase();
                // Only show available amenities; optionally show cost
                String costStr = amenity.isAvailable()
                        ? "  $" + String.format("%.0f", amenity.getAmenityCost())
                        : "  UNAVAILABLE";
                Label chip = new Label("✦  " + display + costStr);
                chip.setStyle(
                        amenity.isAvailable()
                                ? "-fx-background-color: rgba(212,175,55,0.08); " +
                                "-fx-border-color: rgba(212,175,55,0.3); " +
                                "-fx-border-radius: 20; -fx-background-radius: 20; " +
                                "-fx-text-fill: #DADED8; " +
                                "-fx-font-family: 'Montserrat Light'; -fx-font-size: 10px; -fx-padding: 5 12;"
                                : "-fx-background-color: rgba(100,100,100,0.08); " +
                                "-fx-border-color: rgba(100,100,100,0.3); " +
                                "-fx-border-radius: 20; -fx-background-radius: 20; " +
                                "-fx-text-fill: #555; " +
                                "-fx-font-family: 'Montserrat Light'; -fx-font-size: 10px; -fx-padding: 5 12;"
                );
                amenFlow.getChildren().add(chip);
            }
        } else {
            Label none = new Label("No amenities added to this room.");
            none.setStyle("-fx-font-family: 'Montserrat Light'; -fx-font-size: 11px; -fx-text-fill: #768064;");
            amenFlow.getChildren().add(none);
        }
        amenitiesSection.getChildren().addAll(amenHeader, amenFlow);
        panel.getChildren().add(amenitiesSection);


        // Uses: res.getCheckin(), res.getCheckout(), res.getReservationID()
        // res.getStatus() == Reservations.ReservationStatus.CONFIRMED
        // guest.getUserName()
        VBox occupantSection = new VBox(10);
        occupantSection.setPadding(new Insets(18, 40, 18, 40));

        Label occupantHeader = new Label("CURRENT OCCUPANT");
        occupantHeader.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 10px; " +
                "-fx-text-fill: #768064; -fx-letter-spacing: 3px;");

        if (room.getStatus() == Rooms.RoomStatus.OCCUPIED) {
            // Find CONFIRMED reservation whose assigned room matches this room
            Reservations activeRes = Database.getReservationsList().stream()
                    .filter(res ->
                            res.getRoom() != null &&
                                    res.getRoom().getRoomNumber().equals(room.getRoomNumber()) &&
                                    res.getStatus() == Reservations.ReservationStatus.CONFIRMED
                    )
                    .findFirst()
                    .orElse(null);

            if (activeRes != null) {
                Guests guest = activeRes.getGuest();

                HBox guestRow = new HBox(14);
                guestRow.setAlignment(Pos.CENTER_LEFT);
                guestRow.setStyle("-fx-background-color: rgba(0,0,0,0.3); " +
                        "-fx-background-radius: 10; -fx-padding: 14 18;");

                Label icon = new Label("👤");
                icon.setStyle("-fx-font-size: 24px;");

                VBox guestInfo = new VBox(4);


                Label guestName = new Label(activeRes.getGuest().getUserName().toUpperCase());
                guestName.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 15px; -fx-text-fill: #DADED8;");

                Label guestDates = new Label(
                        "CHECK-IN: " + activeRes.getCheckin() +
                                "     ·     CHECK-OUT: " + activeRes.getCheckout()
                );
                guestDates.setStyle("-fx-font-family: 'Montserrat Light'; " +
                        "-fx-font-size: 10px; -fx-text-fill: #768064;");

                Label resvId = new Label("RESERVATION #" + activeRes.getReservationID());
                resvId.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 10px; -fx-text-fill: #d4af37;");

                // Show stay price
                Label stayPrice = new Label("TOTAL STAY: $" +
                        String.format("%.2f", activeRes.getStayPrice()));
                stayPrice.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 11px; -fx-text-fill: #DADED8;");

                guestInfo.getChildren().addAll(guestName, guestDates, resvId, stayPrice);
                guestRow.getChildren().addAll(icon, guestInfo);
                occupantSection.getChildren().addAll(occupantHeader, guestRow);

            } else {
                Label noData = new Label("Occupied — no confirmed reservation found.");
                noData.setStyle("-fx-font-family: 'Montserrat Light'; " +
                        "-fx-font-size: 11px; -fx-text-fill: #e07070;");
                occupantSection.getChildren().addAll(occupantHeader, noData);
            }

        } else {
            HBox availableRow = new HBox(12);
            availableRow.setAlignment(Pos.CENTER_LEFT);
            availableRow.setStyle("-fx-background-color: rgba(80,160,80,0.07); " +
                    "-fx-background-radius: 10; -fx-padding: 14 18;");
            Label greenDot = new Label("●");
            greenDot.setStyle("-fx-text-fill: #7ec97e; -fx-font-size: 10px;");
            Label availMsg = new Label("THIS ROOM IS CURRENTLY AVAILABLE");
            availMsg.setStyle("-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-text-fill: #7ec97e;");
            availableRow.getChildren().addAll(greenDot, availMsg);
            occupantSection.getChildren().addAll(occupantHeader, availableRow);
        }
        panel.getChildren().add(occupantSection);

        //  Close button
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: rgba(0,0,0,0.25); " +
                "-fx-background-radius: 0 0 22 22; -fx-padding: 20 40 28 40;");

        Button closeBtn = new Button("CLOSE");
        closeBtn.setPrefWidth(200);
        closeBtn.setPrefHeight(42);
        String base  = "-fx-background-color: transparent; -fx-border-color: rgba(118,128,100,0.5); " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #768064; " +
                "-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-cursor: hand; -fx-letter-spacing: 2px;";
        String hover = "-fx-background-color: rgba(118,128,100,0.12); -fx-border-color: rgba(118,128,100,0.8); " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-text-fill: #DADED8; " +
                "-fx-font-family: 'Cinzel'; -fx-font-size: 12px; -fx-cursor: hand; -fx-letter-spacing: 2px;";
        closeBtn.setStyle(base);
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(hover));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(base));
        closeBtn.setOnAction(e -> hideOverlay());

        dialogOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == dialogOverlay) hideOverlay();
        });

        footer.getChildren().add(closeBtn);
        panel.getChildren().add(footer);

        // Wrap in ScrollPane for short screens
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setMaxHeight(720);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-width: 0;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        showOverlay(scroll);
    }
}