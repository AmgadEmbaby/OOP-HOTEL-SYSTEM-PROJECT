import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageRoomsController implements Initializable {

    @FXML private FlowPane roomsContainer;
    @FXML private StackPane dialogOverlay;
    @FXML private TextField floorField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private Label dialogErrorLabel;

    private Admin admin = Database.getAdmin();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRoomTypes();
        loadRooms();
    }

    private void loadRoomTypes() {
        roomTypeCombo.getItems().clear();
        for (RoomType rt : Database.getAvailableRoomTypesList()) {
            roomTypeCombo.getItems().add(rt.getTypeName());
        }
    }

    private void loadRooms() {
        roomsContainer.getChildren().clear();
        for (Rooms room : Database.getRoomList()) {
            roomsContainer.getChildren().add(createRoomCard(room));
        }
    }

    @FXML
    private void openAddRoomDialog(ActionEvent event) {
        dialogErrorLabel.setText("");
        floorField.clear();
        roomTypeCombo.getSelectionModel().clearSelection();

        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);

        // simple fade in
        FadeTransition fade = new FadeTransition(Duration.millis(150), dialogOverlay);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    @FXML
    private void closeDialog(ActionEvent event) {

        roomsContainer.setEffect(null);

        FadeTransition fade = new FadeTransition(Duration.millis(150), dialogOverlay);
        fade.setFromValue(1);
        fade.setToValue(0);

        fade.setOnFinished(e -> {
            dialogOverlay.setVisible(false);
            dialogOverlay.setManaged(false);
            dialogOverlay.getChildren().clear();
        });

        fade.play();
    }
    private void closeOverlay() {

        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();

        if (roomsContainer != null) {
            roomsContainer.setEffect(null);
        }
    }

    @FXML
    private void createRoom(ActionEvent event) {
        try {
            String floorText = floorField.getText().trim();
            String roomType = roomTypeCombo.getValue();

            if (floorText.isEmpty() || roomType == null) {
                dialogErrorLabel.setText("Please fill all fields.");
                return;
            }

            int floor = Integer.parseInt(floorText);
            admin.createRoom(floor, roomType);

            hideOverlay();
            loadRooms();

        } catch (Exception e) {
            dialogErrorLabel.setText(e.getMessage());
        }
    }

    private void deleteRoom(Rooms room) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Room");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                admin.deleteRoom(room.getRoomNumber());
                loadRooms();
            }
        });
    }


    @FXML
    private void openStatusDialog(Rooms room) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("RoomStatusDialog.fxml"));

            Parent dialog = loader.load();

            RoomStatusDialogController controller = loader.getController();

            controller.setData(room, () -> {
                loadRooms();          // refresh UI
                closeOverlay();       // IMPORTANT FIX (we add this)
            });

            dialogOverlay.getChildren().setAll(dialog);
            dialogOverlay.setVisible(true);
            dialogOverlay.setManaged(true);

            GaussianBlur blur = new GaussianBlur(0);
            roomsContainer.setEffect(blur);

            FadeTransition fade = new FadeTransition(Duration.millis(180), dialog);
            fade.setFromValue(0);
            fade.setToValue(1);

            ScaleTransition scale = new ScaleTransition(Duration.millis(180), dialog);
            scale.setFromX(0.92);
            scale.setFromY(0.92);
            scale.setToX(1);
            scale.setToY(1);

            Timeline blurAnim = new Timeline(
                    new KeyFrame(Duration.millis(180),
                            new KeyValue(blur.radiusProperty(), 18))
            );

            fade.play();
            scale.play();
            blurAnim.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createRoomCard(Rooms room) {

        VBox card = new VBox();
        card.getStyleClass().add("room-card");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(320);
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(false);

        String typeName = room.getRoomtype().getTypeName().toLowerCase();

        String imagePath = switch (typeName) {
            case "suite" -> "/suite.jpg";
            case "double" -> "/double.jpg";
            default -> "/single.jpg";
        };

        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox infoSection = new VBox(10);
        infoSection.setPadding(new Insets(22));

        Label roomTitle = new Label(room.getRoomtype().getTypeName().toUpperCase());
        roomTitle.getStyleClass().add("room-card-title");

        Label roomNumber = new Label("ROOM " + room.getRoomNumber());
        roomNumber.getStyleClass().add("room-card-sub");

        Label floor = new Label("Floor " + room.getRoomFloor());
        floor.getStyleClass().add("room-card-sub");

        Label status = new Label(room.getStatus().toString());
        status.getStyleClass().add(
                room.getStatus() == Rooms.RoomStatus.AVAILABLE
                        ? "status-available"
                        : "status-occupied"
        );

        HBox buttons = new HBox(10);

        Button editBtn = new Button("EDIT");
        editBtn.getStyleClass().add("minimal-button");

        Button deleteBtn = new Button("DELETE");
        deleteBtn.getStyleClass().add("minimal-button-danger");

        editBtn.setOnAction(e -> openStatusDialog(room));
        deleteBtn.setOnAction(e -> deleteRoom(room));

        buttons.getChildren().addAll(editBtn, deleteBtn);

        infoSection.getChildren().addAll(
                roomTitle,
                roomNumber,
                floor,
                status,
                buttons
        );

        card.getChildren().addAll(imageView, infoSection);

        return card;
    }
    private void hideOverlay() {
        roomsContainer.setEffect(null);

        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();
    }
}