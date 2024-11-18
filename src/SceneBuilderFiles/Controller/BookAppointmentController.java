package SceneBuilderFiles.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BookAppointmentController {

    @FXML
    private Button bookAppointmentButton;

    @FXML
    private Button rescheduleAppointmentButton;

    @FXML
    private Button cancelAppointmentButton;

    @FXML
    private ChoiceBox<String> doctorChoiceBox;

    @FXML
    private ChoiceBox<String> timeSlotChoiceBox;

    @FXML
    private ListView<String> appointmentListView;

    @FXML
    private Label notificationLabel;

    @FXML
    private ImageView backButton;

    public BookAppointmentController() {

    }

    // Handle the "Book Appointment" Button Click
    @FXML
    private void handleBookAppointment() {
        String selectedDoctor = doctorChoiceBox.getValue();
        String selectedTimeSlot = timeSlotChoiceBox.getValue();

        if (selectedDoctor == null || selectedTimeSlot == null) {
            notificationLabel.setText("Please select both a doctor and a time slot.");
        } else {
            // Logic to book the appointment (add to the list or backend system)
            appointmentListView.getItems().add("Appointment with Dr. " + selectedDoctor + " at " + selectedTimeSlot);
            notificationLabel.setText("Appointment booked successfully.");
        }
    }

    // Handle the "Reschedule Appointment" Button Click
    @FXML
    private void handleRescheduleAppointment() {
        String selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            notificationLabel.setText("Please select an appointment to reschedule.");
        } else {
            // Logic to reschedule (update time, doctor, etc.)
            notificationLabel.setText("Appointment rescheduled.");
        }
    }

    // Handle the "Cancel Appointment" Button Click
    @FXML
    private void handleCancelAppointment() {
        String selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            notificationLabel.setText("Please select an appointment to cancel.");
        } else {
            // Logic to cancel the appointment (remove from list or backend)
            appointmentListView.getItems().remove(selectedAppointment);
            notificationLabel.setText("Appointment canceled.");
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
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.setTitle(pageTitle);
        stage.show();
    }

}
