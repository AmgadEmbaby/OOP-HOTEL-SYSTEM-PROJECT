import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class ManageRoomsController {

    @FXML private FlowPane roomsContainer;
    @FXML private StackPane dialogOverlay;

    private final Admin admin = Database.getAdmin();

    @FXML
    public void initialize() {
        loadRooms();
        hideOverlay();
    }

    private void loadRooms() {
        roomsContainer.getChildren().clear();
        for (Rooms r : Database.getRoomList()) {
            roomsContainer.getChildren().add(createCard(r));
        }
    }

    private void showOverlay(Parent content) {
        StackPane.setAlignment(content, Pos.CENTER);

        dialogOverlay.setAlignment(Pos.CENTER);
        dialogOverlay.getChildren().setAll(content);
        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);

        GaussianBlur blur = new GaussianBlur(0);
        roomsContainer.setEffect(blur);

        new Timeline(
                new KeyFrame(Duration.millis(180),
                        new KeyValue(blur.radiusProperty(), 18))
        ).play();
    }

    public void hideOverlay() {
        roomsContainer.setEffect(null);
        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();
    }

    @FXML
    private void openAddRoomDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRoomDialog.fxml"));
            Parent dialog = loader.load();

            AddRoomDialogController controller = loader.getController();
            controller.setParent(this);

            showOverlay(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRoomFromDialog(String floorText, String type) {
        try {
            int floor = Integer.parseInt(floorText);
            admin.createRoom(floor, type);
            Database.addActivity("Room created (Floor " + floor + ")");
            AdminController.refreshUI();
            hideOverlay();
            loadRooms();
        } catch (Exception e) {
            System.out.println("Invalid room creation");
        }
    }

    private VBox createCard(Rooms room) {

        ImageView imageView = new ImageView();
        imageView.setFitWidth(340);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);

        String typeName = room.getRoomtype().getTypeName().toLowerCase();
        String imagePath = switch (typeName) {
            case "suite"  -> "/suite.jpg";
            case "double" -> "/double.jpg";
            default       -> "/single.jpg";
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

        VBox info = new VBox(8);
        info.setPadding(new Insets(16, 18, 12, 18));

        Label title = new Label(room.getRoomtype().getTypeName().toUpperCase());
        title.getStyleClass().add("room-card-title");

        Label num   = new Label("ROOM "  + room.getRoomNumber());
        Label floor = new Label("FLOOR " + room.getRoomFloor());
        num.getStyleClass().add("room-card-sub");
        floor.getStyleClass().add("room-card-sub");

        Label status = new Label(room.getStatus().toString());
        status.getStyleClass().add(
                room.getStatus() == Rooms.RoomStatus.AVAILABLE
                        ? "status-available" : "status-occupied"
        );

        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(8, 0, 0, 0));

        Button edit = new Button("EDIT");
        Button del  = new Button("DELETE");
        edit.getStyleClass().add("minimal-button");
        del.getStyleClass().add("minimal-button-danger");
        edit.setMaxWidth(Double.MAX_VALUE);
        del.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(edit, Priority.ALWAYS);
        HBox.setHgrow(del,  Priority.ALWAYS);

        edit.setOnAction(e -> openStatusDialog(room));
        del.setOnAction(e -> {
            admin.deleteRoom(room.getRoomNumber());
            Database.addActivity("Deleted Room " + room.getRoomNumber());
            AdminController.refreshUI();
            loadRooms();
        });

        buttons.getChildren().addAll(edit, del);
        info.getChildren().addAll(title, num, floor, status, buttons);
        card.getChildren().addAll(imageView, info);
        return card;
    }

    private void openStatusDialog(Rooms room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RoomStatusDialog.fxml"));
            Parent dialog = loader.load();

            RoomStatusDialogController c = loader.getController();
            c.setParentController(this);
            c.setData(room, () -> {
                Database.addActivity("Room updated");
                AdminController.refreshUI();
                loadRooms();
                hideOverlay();
            });

            showOverlay(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshRooms() {
        loadRooms();
    }
}
