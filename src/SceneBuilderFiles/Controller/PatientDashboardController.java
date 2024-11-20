package SceneBuilderFiles.Controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Person.Doctor;
import packages.Person.Patient;

public class PatientDashboardController {

    private Patient patient;

    @FXML
    private Label mainContentTitle; // Label for displaying main content area title

    @FXML
    private Pane subOptionPane; // Pane for showing sub-option content dynamically

    @FXML
    private Label subOptionTitle; // Label for sub-options title


    public PatientDashboardController(){
        patient=null;
    }


    /**
     * Displays the home/overview section.
     */
    @FXML
    private void showOverview(ActionEvent event) {
        mainContentTitle.setText("Home / Overview");
        // Add logic to populate main content area with overview details
        System.out.println("Displaying Home / Overview.");
    }


    // Method to set Doctor object
    public void setPatient(Patient patient) {
        this.patient = patient;
        mainContentTitle.setText("Welcome, " + this.patient.getName());
    }

    /**
     * Handles viewing upcoming appointments.
     */
    @SuppressWarnings("unchecked")
    @FXML
    private void viewAppointments() {
        if (patient == null) {
            mainContentTitle.setText("Error: Patient not found!");
            System.out.println("Patient is not set.");
            return;
        }

        mainContentTitle.setText("Appointments Schedule");
        System.out.println("Fetching appointments...");

        // Retrieve appointments using the doctor's email
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(patient.getEmail());

        // Create a TableView for displaying appointments
        TableView<Appointment> appointmentTable = new TableView<>();

        // Define TableColumns for the Appointment properties
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient Name");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<Appointment, String> doctorColumn = new TableColumn<>("Doctor Name");
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        TableColumn<Appointment, Timestamp> timeColumn = new TableColumn<>("Appointment Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));

        // Add columns to the table
        appointmentTable.getColumns().addAll(idColumn, patientColumn, doctorColumn, timeColumn);

        // Populate the TableView with the appointments
        if (!appointments.isEmpty()) {
            appointmentTable.getItems().addAll(appointments);
        } else {
            mainContentTitle.setText("No appointments scheduled.");
        }

        // Add the TableView to the main content area
        Pane mainContentPane = (Pane) mainContentTitle.getParent(); // Assuming mainContentTitle is in the main content area
        mainContentPane.getChildren().clear(); // Clear existing content
        mainContentPane.getChildren().addAll(mainContentTitle, appointmentTable);

        // Position the table within the pane
        AnchorPane.setTopAnchor(appointmentTable, 50.0);
        AnchorPane.setLeftAnchor(appointmentTable, 20.0);
        AnchorPane.setRightAnchor(appointmentTable, 20.0);
        AnchorPane.setBottomAnchor(appointmentTable, 20.0);
    }

    /**
     * Handles booking a new appointment.
     */
    @FXML
    private void bookAppointment(ActionEvent event) {
        mainContentTitle.setText("Book New Appointment");
        // Add logic to open booking form or process booking
        System.out.println("Booking a new appointment.");
    }

    /**
     * Handles rescheduling an appointment.
     */
    @FXML
    private void rescheduleAppointment(ActionEvent event) {
        mainContentTitle.setText("Reschedule Appointment");
        // Add logic for rescheduling
        System.out.println("Rescheduling an appointment.");
    }

    /**
     * Handles canceling an appointment.
     */
    @SuppressWarnings("unchecked")
    @FXML
    private void cancelAppointment(ActionEvent event) {
        if (patient == null) {
            mainContentTitle.setText("Error: Patient not found!");
            System.out.println("Patient is not set.");
            return;
        }
    
        mainContentTitle.setText("Cancel Appointments");
        System.out.println("Fetching appointments...");
    
        // Retrieve appointments using the doctor's email
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(patient.getEmail());
    
        // Create a TableView for displaying appointments with checkboxes
        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Enable multiple selection
    
        // Define TableColumn for checkboxes
        TableColumn<Appointment, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty()); // Bind to BooleanProperty
    
        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Appointment, Boolean> cell = new CheckBoxTableCell<>();
            
            // Add key and mouse event listeners
            cell.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    // Toggle the checkbox value on Enter key press
                    BooleanProperty cellSelected = cell.getTableRow().getItem().selectedProperty();
                    if (cellSelected != null) {
                        cellSelected.set(!cellSelected.get());
                    }
                }
            });
            
            cell.setOnMousePressed(e -> {
                if (e.isSecondaryButtonDown()) { // Right-click toggles the checkbox
                    BooleanProperty cellSelected = cell.getTableRow().getItem().selectedProperty();
                    if (cellSelected != null) {
                        cellSelected.set(!cellSelected.get());
                    }
                }
            });
            
            return cell;
        });
    
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
    
        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient Name");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    
        TableColumn<Appointment, String> doctorColumn = new TableColumn<>("Doctor Name");
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
    
        TableColumn<Appointment, Timestamp> timeColumn = new TableColumn<>("Appointment Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));
    
        // Add columns to the table
        appointmentTable.getColumns().addAll(selectColumn, idColumn, patientColumn, doctorColumn, timeColumn);
    
        // Populate the TableView with the appointments
        appointmentTable.getItems().addAll(appointments);
    
        // Add "Cancel" button and event handler
        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setOnAction(e -> {
            // Get selected appointments (only those with selected checkbox)
            List<Appointment> selectedAppointments = appointments.stream()
                    .filter(Appointment::isSelected)  // Get appointments with selected checkboxes
                    .collect(Collectors.toList());
    
            if (selectedAppointments.isEmpty()) {
                System.out.println("No appointments selected.");
                return;
            }
    
            // Show confirmation dialog
            boolean confirmation = showConfirmationDialog("Are you sure you want to cancel the selected appointments?");
            if (!confirmation) {
                System.out.println("Cancellation aborted.");
                return;
            }
    
            // Cancel appointments in the database
            for (Appointment appointment : selectedAppointments) {
                String doctorName = appointment.getDoctorName();
                boolean success = DatabaseConnection.cancelAppointment(patient.getID(), doctorName);
                if (!success) {
                    System.out.println("Failed to cancel appointment ID: " + appointment.getAppointmentID());
                }
            }
    
            // Remove cancelled appointments from the list and TableView
            appointments.removeAll(selectedAppointments);  // Remove from the internal list
            appointmentTable.getItems().clear();  // Clear the table view
            appointmentTable.getItems().addAll(appointments);  // Re-populate the table with remaining appointments
    
            // Optionally, refresh the schedule view
            viewAppointments();
            
        });
    
        // Add the TableView and button to the main content area
        Pane mainContentPane = (Pane) mainContentTitle.getParent(); // Assuming mainContentTitle is in the main content area
        mainContentPane.getChildren().clear(); // Clear existing content
        mainContentPane.getChildren().addAll(mainContentTitle, appointmentTable, cancelButton);
    
        // Position the table and button within the pane
        AnchorPane.setTopAnchor(appointmentTable, 50.0);
        AnchorPane.setLeftAnchor(appointmentTable, 20.0);
        AnchorPane.setRightAnchor(appointmentTable, 20.0);
        AnchorPane.setBottomAnchor(appointmentTable, 60.0);
        AnchorPane.setTopAnchor(cancelButton, 20.0);
        AnchorPane.setRightAnchor(cancelButton, 20.0);
    }
    
    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }
    

    /**
     * Handles viewing health records.
     */
    @FXML
    private void viewHealthRecords(ActionEvent event) {
        mainContentTitle.setText("View Health Records");
        // Add logic to fetch and display health records
        System.out.println("Viewing health records.");
    }

    /**
     * Handles viewing test results.
     */
    @FXML
    private void viewTestResults(ActionEvent event) {
        mainContentTitle.setText("View Test Results");
        // Add logic to fetch and display test results
        System.out.println("Viewing test results.");
    }

    /**
     * Handles downloading medical history.
     */
    @FXML
    private void downloadMedicalHistory(ActionEvent event) {
        mainContentTitle.setText("Download Medical History");
        // Add logic to generate and download medical history
        System.out.println("Downloading medical history.");
    }

    /**
     * Handles viewing prescriptions.
     */
    @FXML
    private void viewPrescriptions(ActionEvent event) {
        mainContentTitle.setText("Prescriptions");
        // Add logic to fetch and display prescriptions
        System.out.println("Viewing prescriptions.");
    }

    /**
     * Handles viewing billing details.
     */
    @FXML
    private void viewBillingDetails(ActionEvent event) {
        mainContentTitle.setText("Billing Details");
        // Add logic to fetch and display billing information
        System.out.println("Viewing billing details.");
    }

    /**
     * Handles making a payment.
     */
    @FXML
    private void makePayment(ActionEvent event) {
        mainContentTitle.setText("Make a Payment");
        // Add logic for processing payments
        System.out.println("Making a payment.");
    }

    /**
     * Handles downloading invoices.
     */
    @FXML
    private void downloadInvoice(ActionEvent event) {
        mainContentTitle.setText("Download Invoice");
        // Add logic to generate and download invoice
        System.out.println("Downloading invoice.");
    }

    /**
     * Handles viewing notifications.
     */
    @FXML
    private void viewNotifications(ActionEvent event) {
        mainContentTitle.setText("Notifications");
        // Add logic to fetch and display notifications
        System.out.println("Viewing notifications.");
    }

    /**
     * Handles editing profile settings.
     */
    @FXML
    private void editProfile(ActionEvent event) {
        mainContentTitle.setText("Profile Settings");
        // Add logic to fetch and display profile settings form
        System.out.println("Editing profile settings.");
    }

    /**
     * Handles opening the help and support section.
     */
    @FXML
    private void openHelp(ActionEvent event) {
        mainContentTitle.setText("Help and Support");
        // Add logic to fetch and display help and support information
        System.out.println("Opening help and support.");
    }
}
