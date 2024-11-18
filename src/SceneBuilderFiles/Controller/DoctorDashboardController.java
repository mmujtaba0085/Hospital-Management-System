package SceneBuilderFiles.Controller;

import packages.Others.Appointment;
import packages.Person.Doctor;
import packages.Database.DatabaseConnection;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;


public class DoctorDashboardController {
    private Doctor doctor;

    // Constructor
    public DoctorDashboardController() {
        doctor = null; // Initialize with null until set later
    }

    // FXML components
    @FXML
    private VBox sidebar;

    @FXML
    private Pane subOptionPane;

    @FXML
    private Label subOptionTitle;

    @FXML
    private Label mainContentTitle;

    // Initialize method called automatically
    @FXML
    public void initialize() {
        System.out.println("Doctor's Dashboard Initialized!");
        mainContentTitle.setText("Welcome to the Doctor's Dashboard");
        subOptionPane.setVisible(false); // Hide sub-options by default
    }

    // Method to set Doctor object
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        mainContentTitle.setText("Welcome, " + this.doctor.getName());
        System.out.println("Doctor's specialization: " + doctor.getSpecialization());
    }

    // Sidebar Handlers
    @FXML
    public void showOverview() {
        mainContentTitle.setText("Overview");
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void viewSchedule() {
    if (doctor == null) {
        mainContentTitle.setText("Error: Doctor not found!");
        System.out.println("Doctor is not set.");
        return;
    }

    mainContentTitle.setText("Appointments Schedule");
    System.out.println("Fetching appointments...");

    // Retrieve appointments using the doctor's email
    List<Appointment> appointments = DatabaseConnection.ViewAppointments(doctor.getEmail());

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

    // Additional Sidebar Handlers
    @FXML
    public void rescheduleAppointments() {
        Pane mainContentPane = (Pane) mainContentTitle.getParent(); // Assuming mainContentTitle is in the main content area
        mainContentPane.getChildren().clear(); // Clear existing content
        mainContentTitle.setText("Reschedule Appointments");
        mainContentPane.getChildren().addAll(mainContentTitle); // Add title and appointments list
    }

    
    @FXML
    public void cancelAppointments() {
        if (doctor == null) {
            mainContentTitle.setText("Error: Doctor not found!");
            System.out.println("Doctor is not set.");
            return;
        }
    
        mainContentTitle.setText("Cancel Appointments");
        System.out.println("Fetching appointments...");
    
        // Retrieve appointments using the doctor's email
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(doctor.getEmail());
    
        // Create a TableView for displaying appointments with checkboxes
        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Enable multiple selection
    
        // Define TableColumn for checkboxes
        TableColumn<Appointment, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty()); // Bind to BooleanProperty
    
        selectColumn.setCellFactory(tc -> {
            CheckBoxTableCell<Appointment, Boolean> cell = new CheckBoxTableCell<>();
            
            // Add key and mouse event listeners
            cell.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    // Toggle the checkbox value on Enter key press
                    BooleanProperty cellSelected = cell.getTableRow().getItem().selectedProperty();
                    if (cellSelected != null) {
                        cellSelected.set(!cellSelected.get());
                    }
                }
            });
            
            cell.setOnMousePressed(event -> {
                if (event.isSecondaryButtonDown()) { // Right-click toggles the checkbox
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
        cancelButton.setOnAction(event -> {
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
                String patientName = appointment.getPatientName();
                boolean success = DatabaseConnection.cancelAppointment(doctor.getID(), patientName);
                if (!success) {
                    System.out.println("Failed to cancel appointment ID: " + appointment.getAppointmentID());
                }
            }
    
            // Remove cancelled appointments from the list and TableView
            appointments.removeAll(selectedAppointments);  // Remove from the internal list
            appointmentTable.getItems().clear();  // Clear the table view
            appointmentTable.getItems().addAll(appointments);  // Re-populate the table with remaining appointments
    
            // Optionally, refresh the schedule view
            viewSchedule();
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
    

    @FXML
    public void viewPatientDetails() {
        mainContentTitle.setText("Patient Details");
    }

    @FXML
    public void updateHealthRecords() {
        mainContentTitle.setText("Update Health Records");
    }

    @FXML
    public void viewMedicalHistory() {
        mainContentTitle.setText("Medical History");
    }

    @FXML
    public void createConsultationNotes() {
        mainContentTitle.setText("Create Consultation Notes");
    }

    @FXML
    public void viewPreviousNotes() {
        mainContentTitle.setText("Previous Consultation Notes");
    }

    @FXML
    public void openPrescriptions() {
        mainContentTitle.setText("Prescriptions");
    }

    @FXML
    public void openCommunication() {
        mainContentTitle.setText("Communication");
    }

    @FXML
    public void openBilling() {
        mainContentTitle.setText("Billing and Payments");
    }

    @FXML
    public void generateReports() {
        mainContentTitle.setText("Generate Reports");
    }

    @FXML
    public void downloadReports() {
        mainContentTitle.setText("Download Reports");
    }

    @FXML
    public void openSettings() {
        mainContentTitle.setText("Settings");
    }

    @FXML
    public void openHelp() {
        mainContentTitle.setText("Help and Support");
    }
}