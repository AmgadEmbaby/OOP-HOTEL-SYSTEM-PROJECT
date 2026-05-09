import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGui extends Application {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void makeDraggable(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }





    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Zaslia.otf"), 50);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Cinzel.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Montserrat-Light.ttf"), 12);

        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Scene scene = new Scene(root, 1000, 600);

        // This removes the top white bar and "X" buttons
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.show();
        makeDraggable(root, stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}