package SceneBuilderFiles.Controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> comboBox;
    @FXML
    private Button registerButton;

    public void initialize() {
        // Adding items programmatically to the ComboBox
        comboBox.getItems().addAll("Patient", "Doctor", "Admin");

    }

    // This method will be called when the Register button is clicked
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // Example validation: Check if password and confirm password match
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        // Get values from the form fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        Date date = new Date(System.currentTimeMillis());
        String role = comboBox.getValue();  // This will be either "Admin" or "User"

        // Check if the ComboBox has a valid selection
        if (role == null || role.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please select a role.");
            return;
        }
        boolean success=false;
        // Insert the user data into the database
        if(role=="Patient"){
            // Insert patient data into the database
            success = DatabaseConnection.addPatient(firstName+lastName, email, phone, password, date);
        }
        else if(role=="Doctor"){
            success = DatabaseConnection.addDoctor(firstName+lastName, email, phone, password, date);
        }
        else{
            //boolean success = addUserToDatabase(firstName, lastName, email, phone, password, role);
        }
        
        if (success) {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully.");
            navigateToLoginPage();
        } else {
            showAlert(AlertType.ERROR, "Error", "Registration failed.");
        }
    }

    // Show an alert for errors or other information
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Navigate to the login page
    private void navigateToLoginPage() {
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("@Login.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Set the scene and show the login page
            stage.setScene(scene);
            stage.setTitle("Login Page");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load the login page.");
        }
    }
}
