package SceneBuilderFiles.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import packages.Database.DatabaseConnection;

public class RegisterController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneNumberField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation checks
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        // Try inserting into the database
        System.out.println("Inserting into the database...");
        boolean success = DatabaseConnection.addPatient(firstName + " " + lastName, email, phone, password);
        System.out.println("Insert success: " + success);

        // If registration is successful, navigate to login page
        if (success) {
            DatabaseConnection.updatePassword(email, email, confirmPassword);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully.");
            navigateToLoginPage();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed. ");
        }
    }

    private void navigateToLoginPage() {
        try {
            // Ensure the path to Login.fxml is correct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SceneBuilderFiles/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) firstNameField.getScene().getWindow();
            Scene loginScene = new Scene(root, 800, 600); // Adjust dimensions as needed
            currentStage.setScene(loginScene);
            currentStage.setTitle("Orenix Hospital Management System - Login");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}





/*package SceneBuilderFiles.Controller;

import java.io.IOException;
import java.sql.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import packages.Database.DatabaseConnection;

public class RegisterController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    public void initialize() {
        // Any initialization logic can go here.
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        System.out.println("Register button clicked!"); // Debug statement
    
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }
    
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        Date date = new Date(System.currentTimeMillis());
    
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }
    
        System.out.println("Inserting into the database...");
        boolean success = DatabaseConnection.addPatient(firstName + " " + lastName, email, phone, password, date);
        System.out.println("Insert success: " + success);
    
        if (success) {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");
            navigateToLoginPage();
        } else {
            showAlert(AlertType.ERROR, "Error", "Registration failed.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SceneBuilderFiles/Login.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }
}
 */