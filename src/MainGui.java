import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle; // Add this import

public class MainGui extends Application {
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
    }
}