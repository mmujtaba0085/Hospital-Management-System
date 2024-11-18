package SceneBuilderFiles.Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PatientHealthRecordController {

    @FXML
    private TextField patientIdField;

    @FXML
    private TextArea healthRecordsArea;

    @FXML
    private TextArea medicalHistoryArea;

    @FXML
    private Button saveButton;

    @FXML
    private Label notificationLabel;

    @FXML
    private ImageView backButton;

    // Handle Save Button Click
    @FXML
    private void handleSaveHealthRecord() {
        String patientId = patientIdField.getText();
        String healthRecords = healthRecordsArea.getText();
        String medicalHistory = medicalHistoryArea.getText();

        if (patientId.isEmpty() || healthRecords.isEmpty() || medicalHistory.isEmpty()) {
            notificationLabel.setText("Please fill in all the fields.");
        } else {
            // Logic to save the patient health record and medical history
            // You would typically save these to a database or backend service here

            notificationLabel.setText("Health record and medical history saved successfully.");
        }
    }

    @FXML
    private void goBackToPatientPortal() throws IOException {
        loadNewScene("PatientPortal.fxml", "Patient Portal");
    }

    private void loadNewScene(String fxmlFile, String pageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientPortal.fxml"));
        AnchorPane root = loader.load();

        // Get the current stage (window)
        Stage stage = (Stage) backButton.getScene().getWindow(); // Reference correct back button
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.setTitle(pageTitle);
        stage.show();
    }

}
