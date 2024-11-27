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
        TextField emailField = (TextField) scene.lookup(".text-field");
        PasswordField passwordField = (PasswordField) scene.lookup(".password-field");
        Button loginButton = (Button) scene.lookup(".login-button");

        // Access the controller
        LoginController controller = loader.getController();
        // Access the initialized signUpButton
        controller.signUpButton.setOnAction(event -> navigateToRegisterPage(primaryStage));

        // Set up login button action
        loginButton.setOnAction(event -> {
            // String username = emailField.getText();
            // String password = passwordField.getText();

            //String username="john.doe@gmail.com";
             String username="alice.smith@hospital.com";
            //String username = "admin.b@hospital.com";
            // String username="rachel.green@hospital.com"; // receptionist
            String password = "default_password";

            int role = DatabaseConnection.authenticateUser(username, password);
            if (role != 0) {
                switch (role) {
                    case 1:
                        Admin admin = DatabaseConnection.AdminDetail(username, password);
                        openAdminDashboard(primaryStage, admin);
                        break;
                    case 2:
                        Doctor doctor = DatabaseConnection.DoctorDetail(username, password);
                        openDoctorDashboard(primaryStage, doctor);
                        break;
                    case 3:

                        // Create a Receptionist object with appropriate data
                        Receptionist receptionist = DatabaseConnection.ReceptionistDetail(username, password);
                        System.out.println("Receptionist: " + receptionist.getEmail());

                        openReceptionistDashboard(primaryStage, receptionist); // Open Receptionist Dashboard
                        break;
                    case 4:
                        // Create a Patient object with appropriate data
                        Patient patient = DatabaseConnection.PatientDetail(username, password);

                        openPatientDashboard(primaryStage, patient); // Open Patient Dashboard

                        break;
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });

        primaryStage.setTitle("Orenix Hospital Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to navigate to the registration page
    private void navigateToRegisterPage(Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/Register.fxml"));
            Parent root = loader.load();

            @SuppressWarnings("unused")
            RegisterController registerController = loader.getController();

            Scene registerScene = new Scene(root, 700, 500); // Adjust dimensions as needed
            currentStage.setScene(registerScene);
            currentStage.setTitle("Orenix Hospital Management System - Register");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the registration page.");
        }
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

            Scene doctorScene = new Scene(root, 1000, 700); // Adjust dimensions as needed
            // doctorScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/DoctorDashboard.css").toExternalForm());

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

            Scene patientScene = new Scene(root, 900, 700); // Adjust dimensions as needed
            // patientScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/PatientDashboard.css").toExternalForm());

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

            Scene adminScene = new Scene(root, 900, 700); // Adjust dimensions as needed
            // adminScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/AdminDashboard.css").toExternalForm());

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

    // Method to open Receptionist Dashboard with Receptionist object
    private void openReceptionistDashboard(Stage loginStage, Receptionist receptionist) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneBuilderFiles/ReceptionistDashboard.fxml"));
            Parent root = loader.load();

            // Get the ReceptionistDashboard controller and pass the Receptionist object
            ReceptionistDashboardController controller = loader.getController();
            controller.setReceptionist(receptionist);

            Scene receptionistScene = new Scene(root, 900, 700); // Adjust dimensions as needed width, height
            // adminScene.getStylesheets().add(getClass().getResource("SceneBuilderFiles/CSS/ReceptionistDashboard.css").toExternalForm());

            Stage receptionistStage = new Stage();
            receptionistStage.setTitle("Orenix Hospital Management System - Receptionist Dashboard");
            receptionistStage.setScene(receptionistScene);

            // Close the login stage
            loginStage.close();

            // Show the Receptionist Dashboard
            receptionistStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Receptionist Dashboard.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
