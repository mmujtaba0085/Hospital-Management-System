import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OrenixLoginApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("OrenixLogin.fxml"));
        Scene scene = new Scene(root, 800, 600); // Adjusted width and height for two-column layout
        scene.getStylesheets().add(getClass().getResource("OrenixLogin.css").toExternalForm());
        
        primaryStage.setTitle("Orenix Hospital Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
