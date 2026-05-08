import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.InputStream;

public class ManageAmenitiesController {

    @FXML private FlowPane amenitiesContainer;
    @FXML private StackPane dialogOverlay;

    // ================= INIT =================

    @FXML
    public void initialize() {
        assignDefaultImages();
        loadAmenities();
        hideOverlay();
    }

    // ================= ASSIGN DEFAULT IMAGES =================

    private void assignDefaultImages() {
        for (Amenity a : Database.getamenitiesList()) {
            if (a.getImagePath() == null || a.getImagePath().equals("/fallback.jpg")) {
                String guessedPath = "/" + a.getAmenityName().toLowerCase().trim() + ".jpg";
                InputStream is = getClass().getResourceAsStream(guessedPath);
                if (is != null) {
                    a.setImagePath(guessedPath);
                }
            }
        }
    }

    // ================= LOAD =================

    private void loadAmenities() {
        amenitiesContainer.getChildren().clear();
        for (Amenity a : Database.getamenitiesList()) {
            amenitiesContainer.getChildren().add(createCard(a));
        }
    }

    // ================= CARD =================

    private VBox createCard(Amenity a) {
        ImageView img = new ImageView();
        img.setFitWidth(320);
        img.setFitHeight(200);
        img.setPreserveRatio(false);
        loadImage(img, a.getImagePath());

        Rectangle clip = new Rectangle(320, 200);
        clip.setArcWidth(26);
        clip.setArcHeight(26);
        img.setClip(clip);

        Label name = new Label(a.getAmenityName());
        name.getStyleClass().add("room-card-title");

        Label cost = new Label("$" + a.getAmenityCost());
        cost.getStyleClass().add("room-card-sub");

        Label status = new Label(a.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
        status.getStyleClass().add(a.isAvailable() ? "status-available" : "status-occupied");

        Button edit = new Button("EDIT");
        edit.getStyleClass().add("minimal-button");
        edit.setMaxWidth(Double.MAX_VALUE);
        edit.setOnAction(e -> openEditAmenityDialog(a));

        Button del = new Button("DELETE");
        del.getStyleClass().add("minimal-button-danger");
        del.setMaxWidth(Double.MAX_VALUE);
        del.setOnAction(e -> {
            Database.getamenitiesList().remove(a);
            Database.addActivity("Deleted Amenity: " + a.getAmenityName());
            AdminController.refreshUI();
            loadAmenities();
        });

        HBox buttons = new HBox(10, edit, del);
        HBox.setHgrow(edit, Priority.ALWAYS);
        HBox.setHgrow(del,  Priority.ALWAYS);
        buttons.setPadding(new Insets(6, 0, 0, 0));

        VBox info = new VBox(6, name, cost, status, buttons);
        info.setPadding(new Insets(16, 18, 18, 18));

        VBox card = new VBox(0, img, info);
        card.getStyleClass().add("room-card");
        card.setPrefWidth(320);
        card.setMaxWidth(320);
        card.setMinWidth(320);
        return card;
    }

    // ================= IMAGE LOADER =================

    private void loadImage(ImageView iv, String path) {
        if (path != null && !path.isEmpty()) {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                iv.setImage(new Image(is));
                return;
            }
            try {
                File f = new File(path);
                if (f.exists()) {
                    iv.setImage(new Image(f.toURI().toString()));
                    return;
                }
            } catch (Exception ignored) {}
        }
        InputStream fb = getClass().getResourceAsStream("/fallback.jpg");
        if (fb != null) iv.setImage(new Image(fb));
    }

    // ================= OVERLAY =================

    private void showOverlay(Parent content) {
        // Prevent the dialog from stretching full height inside the StackPane
        content.setStyle(content.getStyle() != null ? content.getStyle() : "");
        StackPane.setAlignment(content, Pos.CENTER);
        if (content instanceof Region r) {
            r.setMaxHeight(Region.USE_PREF_SIZE);
        }

        dialogOverlay.setAlignment(Pos.CENTER);
        dialogOverlay.getChildren().setAll(content);
        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);

        GaussianBlur blur = new GaussianBlur(0);
        amenitiesContainer.setEffect(blur);

        new Timeline(
                new KeyFrame(Duration.millis(180),
                        new KeyValue(blur.radiusProperty(), 18))
        ).play();
    }

    public void hideOverlay() {
        amenitiesContainer.setEffect(null);
        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
        dialogOverlay.getChildren().clear();
    }

    // ================= ADD AMENITY =================

    @FXML
    private void openAddAmenityDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAmenityDialog.fxml"));
            Parent dialog = loader.load();

            AddAmenityDialogController controller = loader.getController();
            controller.setParent(this);

            showOverlay(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Called by AddAmenityDialogController when the user confirms. */
    public void addAmenityFromDialog(String name, double cost, String imagePath) {
        try {
            Amenity newAmenity = new Amenity(name, cost);
            if (imagePath != null) {
                newAmenity.setImagePath(imagePath);
            }
            Database.getamenitiesList().add(newAmenity);
            Database.addActivity("Added Amenity: " + newAmenity.getAmenityName());
            AdminController.refreshUI();
            hideOverlay();
            loadAmenities();
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid amenity: " + ex.getMessage());
        }
    }

    // ================= EDIT AMENITY =================

    private void openEditAmenityDialog(Amenity a) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AmenityEditDialog.fxml"));
            Parent dialog = loader.load();

            AmenityEditDialogController controller = loader.getController();
            controller.setParent(this);
            controller.setAmenity(a);

            showOverlay(dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Called by AmenityEditDialogController when the user saves. */
    public void updateAmenityFromDialog(Amenity a, double newCost, boolean available, String imagePath) {
        a.setAmenityCost(newCost);
        a.setAvailable(available);
        if (imagePath != null) {
            a.setImagePath(imagePath);
        }
        Database.addActivity("Edited Amenity: " + a.getAmenityName());
        AdminController.refreshUI();
        hideOverlay();
        loadAmenities();
    }

    // ================= REFRESH =================

    public void refresh() {
        loadAmenities();
    }
}
