import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainGui extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
        Scene scene = new Scene(root, 1000, 700);

        // --- Robust CSS Loading ---
        URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("CRITICAL WARNING: Could not find 'styles.css'. The application will run without styling.");
            System.err.println("Please ensure 'styles.css' is in the 'src' folder and that your IDE is configured to copy it to the output directory.");
        }
        // -------------------------

        stage.setTitle("The Five Seasons - Hotel Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
