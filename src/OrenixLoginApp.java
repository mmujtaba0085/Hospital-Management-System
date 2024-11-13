import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrenixLoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600); // Adjusted width and height for two-column layout
        scene.getStylesheets().add(getClass().getResource("CSS/Login.css").toExternalForm());

        // Get references to UI elements
        TextField emailField = (TextField) scene.lookup(".text-field");  // Get Email TextField
        PasswordField passwordField = (PasswordField) scene.lookup(".password-field");  // Get Password Field
        Button loginButton = (Button) scene.lookup(".login-button");

        // Set up login button action
        loginButton.setOnAction(event -> {
            String username = emailField.getText();
            String password = passwordField.getText();

            // Check credentials in the database
            int role = DatabaseConnection.authenticateUser(username, password);
            if (role != 0) {
                // Login successful based on role
                switch (role) {
                    case 1:
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
                        // Open Admin Dashboard or perform admin-specific actions
                        break;
                    case 2:
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Doctor!");
                        // Open Doctor Dashboard or perform doctor-specific actions
                        break;
                    case 3:
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Receptionist!");
                        // Open Receptionist Dashboard or perform receptionist-specific actions
                        break;
                    case 4:
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Patient!");
                        // Open Patient Dashboard or perform patient-specific actions
                        break;
                }
            } else {
                // Login failed
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });

        primaryStage.setTitle("Orenix Hospital Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
