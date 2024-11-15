import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import packages.Person.*;
import SceneBuilderFiles.Controller.*;

public class OrenixApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600); // Adjusted width and height for two-column layout
        scene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/Login.css").toExternalForm());

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
                    case 2: // Doctor Role
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Doctor!");
                        
                        // Create a Doctor object with appropriate data
                        Doctor doctor = new Doctor("Dr. " + username,"General Medicine");

                        openDoctorDashboard(primaryStage, doctor); // Open Doctor Dashboard
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

    // Method to open Doctor Dashboard with Doctor object
    private void openDoctorDashboard(Stage loginStage, Doctor doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/DoctorDashboard.fxml"));
            Parent root = loader.load();

            // Get the DoctorDashboard controller and pass the Doctor object
            DoctorDashboardController controller = loader.getController();
            controller.setDoctor(doctor);

            Scene doctorScene = new Scene(root, 1000, 800); // Adjust dimensions as needed
            doctorScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/DoctorDashboard.css").toExternalForm());

            Stage doctorStage = new Stage();
            doctorStage.setTitle("Orenix Hospital Management System - Doctor Dashboard");
            doctorStage.setScene(doctorScene);

            // Close the login stage
            loginStage.close();

            // Show the Doctor Dashboard
            doctorStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Doctor Dashboard.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
