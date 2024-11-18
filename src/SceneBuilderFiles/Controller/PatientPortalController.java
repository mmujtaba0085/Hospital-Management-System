package SceneBuilderFiles.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientPortalController {

    @FXML
    private Button viewAppointmentsButton;

    @FXML
    private Button viewMedicalHistoryButton;

    @FXML
    private Button viewBillingButton;

    public PatientPortalController() {
    }

    // Handle View Appointments Button Click
    @FXML
    private void handleViewAppointments() throws IOException {
        // Load the Appointments FXML page
        loadNewScene("BookAppointment.fxml", "Appointments Page");
    }

    // Handle View Medical History Button Click
    @FXML
    private void handleViewMedicalHistory() throws IOException {
        // Load the Medical History FXML page
        loadNewScene("PatientHealthRecords.fxml", "Medical History Page");
    }

    // Handle View Billing Button Click
    @FXML
    private void handleViewBilling() throws IOException {
        // Load the Billing FXML page
        loadNewScene("PayBill.fxml", "Billing Page");
    }

    // Utility method to load a new scene
    private void loadNewScene(String fxmlFile, String pageTitle) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        AnchorPane root = loader.load();

        // Get the current stage (window)
        Stage stage = (Stage) viewAppointmentsButton.getScene().getWindow();

        // Set the new scene
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.setTitle(pageTitle);
        stage.show();
    }
}
