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
import packages.Database.*;;

public class OrenixApp extends Application {

    @SuppressWarnings("unused")
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


            //String username="john.doe@gmail.com";
            //String password="default_password";
            
            

            // Check credentials in the database
            int role = DatabaseConnection.authenticateUser(username, password);
            if (role != 0) {
                // Login successful based on role
                switch (role) {
                    case 1:
                        // Create a Admin object with appropriate data
                        Admin admin = DatabaseConnection.AdminDetail(username, password);

                        openAdminDashboard(primaryStage, admin); // Open Admin Dashboard
                        break;
                    case 2: // Doctor Role
                        
                        
                        // Create a Doctor object with appropriate data
                        Doctor doctor = DatabaseConnection.DoctorDetail(username, password);

                        openDoctorDashboard(primaryStage, doctor); // Open Doctor Dashboard
                        break;
                    case 3:
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Receptionist!");
                        // Open Receptionist Dashboard or perform receptionist-specific actions
                        break;
                    case 4:
                        // Create a Doctor object with appropriate data
                        Patient patient = DatabaseConnection.PatientDetail(username, password);

                        openPatientDashboard(primaryStage, patient); // Open Doctor Dashboard
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
            //doctorScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/DoctorDashboard.css").toExternalForm());

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

    // Method to open Patient Dashboard with Patient object
    private void openPatientDashboard(Stage loginStage, Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/PatientDashboard.fxml"));
            Parent root = loader.load();

            // Get the PatientDashboard controller and pass the Patient object
            PatientDashboardController controller = loader.getController();
            controller.setPatient(patient);
            

            Scene patientScene = new Scene(root, 1000, 800); // Adjust dimensions as needed
            //patientScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/PatientDashboard.css").toExternalForm());

            Stage patientStage = new Stage();
            patientStage.setTitle("Orenix Hospital Management System - Patient Dashboard");
            patientStage.setScene(patientScene);

            // Close the login stage
            loginStage.close();

            // Show the Patient Dashboard
            patientStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Patient Dashboard.");
        }
    }

    // Method to open Admin Dashboard with Admin object
    private void openAdminDashboard(Stage loginStage, Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/AdminDashboard.fxml"));
            Parent root = loader.load();

            // Get the AdminDashboard controller and pass the Admin object
            AdminDashboardController controller = loader.getController();
            controller.setAdmin(admin);
            

            Scene adminScene = new Scene(root, 1000, 800); // Adjust dimensions as needed
            //adminScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/AdminDashboard.css").toExternalForm());

            Stage adminStage = new Stage();
            adminStage.setTitle("Orenix Hospital Management System - Admin Dashboard");
            adminStage.setScene(adminScene);

            // Close the login stage
            loginStage.close();

            // Show the Admin Dashboard
            adminStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Admin Dashboard.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
