import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class ManageRoomTypesController {

    @FXML private FlowPane roomTypesContainer;
    @FXML private StackPane dialogOverlay;

    private final Admin admin = Database.getAdmin();

    @FXML
    public void initialize() {
        loadRoomTypes();
        hideOverlay();
    }

    // ================= LOAD =================

    private void loadRoomTypes() {
        roomTypesContainer.getChildren().clear();
        for (RoomType rt : Database.getAvailableRoomTypesList()) {
            roomTypesContainer.getChildren().add(createCard(rt));
        }
    }

    // ================= CARD =================

    private VBox createCard(RoomType rt) {

        Label name = new Label(rt.getTypeName().toUpperCase());
        name.getStyleClass().add("room-card-title");

        Label beds = new Label("BEDS: " + rt.getNumberOfBeds());
        beds.getStyleClass().add("room-card-sub");

        Label capacity = new Label("CAPACITY: " + rt.getCapacity());
        capacity.getStyleClass().add("room-card-sub");

        Label price = new Label("$" + rt.getPricePerNight() + " / NIGHT");
        price.getStyleClass().add("status-available");

        Label desc = new Label(rt.getRoomDescription());
        desc.getStyleClass().add("room-card-sub");
        desc.setWrapText(true);
        desc.setMaxWidth(280);

        Button edit = new Button("EDIT");
        edit.getStyleClass().add("minimal-button");
        edit.setMaxWidth(Double.MAX_VALUE);
        edit.setOnAction(e -> openEditDialog(rt));

        Button del = new Button("DELETE");
        del.getStyleClass().add("minimal-button-danger");
        del.setMaxWidth(Double.MAX_VALUE);
        del.setOnAction(e -> {
            try {
                admin.deleteRoomType(rt.getTypeName());
                Database.addActivity("Deleted Room Type: " + rt.getTypeName());
                AdminController.refreshUI();
                loadRoomTypes();
            } catch (IllegalArgumentException ex) {
                showError("Cannot delete: " + ex.getMessage());
            }
        });

        HBox buttons = new HBox(10, edit, del);
        HBox.setHgrow(edit, Priority.ALWAYS);
        HBox.setHgrow(del, Priority.ALWAYS);
        buttons.setPadding(new Insets(6, 0, 0, 0));

        VBox info = new VBox(8, name, beds, capacity, price, desc, buttons);
        info.setPadding(new Insets(20, 18, 18, 18));

        VBox card = new VBox(info);
        card.getStyleClass().add("room-card");
        card.setPrefWidth(320);
        card.setMaxWidth(320);
        card.setMinWidth(320);
        return card;
    }

    // ================= OVERLAY =================

    private void showOverlay(Parent content) {
        StackPane.setAlignment(content, Pos.CENTER);
        if (content instanceof Region r) r.setMaxHeight(Region.USE_PREF_SIZE);

        dialogOverlay.setAlignment(Pos.CENTER);
        dialogOverlay.getChildren().setAll(content);
        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);

        GaussianBlur blur = new GaussianBlur(0);
        roomTypesContainer.setEffect(blur);
        new Timeline(new KeyFrame(Duration.millis(180),
                new KeyValue(blur.radiusProperty(), 18))).play();
    }

    public void hideOverlay() {
        roomTypesContainer.setEffect(null);
        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();
    }

    // ================= ADD =================

    @FXML
    private void openAddDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRoomTypeDialog.fxml"));
            Parent dialog = loader.load();
            AddRoomTypeDialogController controller = loader.getController();
            controller.setParent(this);
            showOverlay(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRoomTypeFromDialog(String name, int beds, int capacity,
                                      String desc, double price) {
        try {
            admin.createRoomType(name, beds, capacity, desc, price);
            Database.addActivity("Added Room Type: " + name);
            AdminController.refreshUI();
            hideOverlay();
            loadRoomTypes();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // ================= EDIT =================

    private void openEditDialog(RoomType rt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditRoomTypeDialog.fxml"));
            Parent dialog = loader.load();
            EditRoomTypeDialogController controller = loader.getController();
            controller.setParent(this);
            controller.setRoomType(rt);
            showOverlay(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRoomTypeFromDialog(RoomType rt, double newPrice) {
        try {
            admin.updateRoomTypePrice(newPrice, rt.getTypeName());
            Database.addActivity("Updated Room Type: " + rt.getTypeName());
            AdminController.refreshUI();
            hideOverlay();
            loadRoomTypes();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    // ================= ERROR =================

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}