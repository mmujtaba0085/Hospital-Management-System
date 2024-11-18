package SceneBuilderFiles.Controller;

import packages.Others.Appointment;
import packages.Others.MedicalHistory;
import packages.Person.Doctor;
import packages.Database.DatabaseConnection;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
import javafx.scene.layout.StackPane;
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

    
    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @FXML
    public void viewMedicalHistory() {
        if (doctor == null) {
            mainContentTitle.setText("Error: Doctor not found!");
            System.out.println("Doctor is not set.");
            return;
        }

        mainContentTitle.setText("Medical Reports of Patients");
        System.out.println("Fetching medical reports...");

        // Retrieve appointments for the doctor
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(doctor.getEmail());

        if (appointments.isEmpty()) {
            mainContentTitle.setText("No appointments found for this doctor.");
            System.out.println("No appointments found.");
            return;
        }

        // Extract patient names from appointments
        List<String> patientNames = appointments.stream()
                .map(Appointment::getPatientName)
                .distinct()
                .collect(Collectors.toList());

        // Query the Patient table to get corresponding patient IDs
        List<Integer> patientIds = DatabaseConnection.getPatientIdsByName(patientNames);

        if (patientIds.isEmpty()) {
            mainContentTitle.setText("No patients found for the appointments.");
            System.out.println("No patients found.");
            return;
        }

        // Fetch medical reports for these patient IDs
        List<MedicalHistory> medicalReports = DatabaseConnection.getMedicalReports(patientIds);

        if (medicalReports.isEmpty()) {
            mainContentTitle.setText("No medical reports found for the patients.");
            System.out.println("No medical reports found.");
            return;
        }

        // Create a TableView for displaying patient details
        TableView<MedicalHistory> reportTable = new TableView<>();

        // Define TableColumns for the MedicalHistory properties

        TableColumn<MedicalHistory, Integer> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<MedicalHistory, String> patientNameColumn = new TableColumn<>("Patient Name");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<MedicalHistory, String> allergiesColumn = new TableColumn<>("Allergies");
        allergiesColumn.setCellValueFactory(new PropertyValueFactory<>("allergies"));

        TableColumn<MedicalHistory, String> medicationsColumn = new TableColumn<>("Medications");
        medicationsColumn.setCellValueFactory(new PropertyValueFactory<>("medications"));

        // Add columns to the table
        reportTable.getColumns().addAll(
                patientIdColumn,
                patientNameColumn,
                allergiesColumn,
                medicationsColumn
        );

        // Populate the TableView with the medical reports
        reportTable.getItems().addAll(medicalReports);

        // Add the TableView to the main content area
        Pane mainContentPane = (Pane) mainContentTitle.getParent(); // Assuming mainContentTitle is in the main content area
        mainContentPane.getChildren().clear(); // Clear existing content
        mainContentPane.getChildren().addAll(mainContentTitle, reportTable);

        // Position the table within the pane
        AnchorPane.setTopAnchor(reportTable, 50.0);
        AnchorPane.setLeftAnchor(reportTable, 20.0);
        AnchorPane.setRightAnchor(reportTable, 20.0);
        AnchorPane.setBottomAnchor(reportTable, 20.0);

        // Set an event listener for when a patient is selected in the TableView
        reportTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayPatientDetails(newValue);
            }
        });
    }

    private void displayPatientDetails(MedicalHistory selectedPatient) {
        // Create a new pane for displaying the selected patient's full history
        VBox detailsPane = new VBox();
        detailsPane.setSpacing(10);
    
        // Display patient name in bold
        Label patientNameLabel = new Label(selectedPatient.getPatientName());
        patientNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        // Display allergies in bold and simple text
        Label allergiesLabel = new Label("Allergies:");
        allergiesLabel.setStyle("-fx-font-weight: bold;");
        Label allergiesDetails = new Label(selectedPatient.getAllergies());
    
        // Display medications in bold and simple text
        Label medicationsLabel = new Label("Medications:");
        medicationsLabel.setStyle("-fx-font-weight: bold;");
        Label medicationsDetails = new Label(selectedPatient.getMedications());
    
        // Similarly for past illnesses, surgeries, family history, and notes
        Label pastIllnessesLabel = new Label("Past Illnesses:");
        pastIllnessesLabel.setStyle("-fx-font-weight: bold;");
        Label pastIllnessesDetails = new Label(selectedPatient.getPastIllnesses());
    
        Label surgeriesLabel = new Label("Surgeries:");
        surgeriesLabel.setStyle("-fx-font-weight: bold;");
        Label surgeriesDetails = new Label(selectedPatient.getSurgeries());
    
        Label familyHistoryLabel = new Label("Family History:");
        familyHistoryLabel.setStyle("-fx-font-weight: bold;");
        Label familyHistoryDetails = new Label(selectedPatient.getFamilyHistory());
    
        Label notesLabel = new Label("Notes:");
        notesLabel.setStyle("-fx-font-weight: bold;");
        Label notesDetails = new Label(selectedPatient.getNotes());
    
        // Add a back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(event -> {
            viewMedicalHistory(); // Reload the previous TableView
        });
    
        // Add all labels and the back button to the details pane
        detailsPane.getChildren().addAll(
                patientNameLabel,
                allergiesLabel, allergiesDetails,
                medicationsLabel, medicationsDetails,
                pastIllnessesLabel, pastIllnessesDetails,
                surgeriesLabel, surgeriesDetails,
                familyHistoryLabel, familyHistoryDetails,
                notesLabel, notesDetails,
                backButton // Add back button at the end
        );
    
        // Add details pane to the main content
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().add(detailsPane);
    
        // Position the details pane within the pane
        AnchorPane.setTopAnchor(detailsPane, 50.0); // Adjust based on desired positioning
        AnchorPane.setLeftAnchor(detailsPane, 20.0);
        AnchorPane.setRightAnchor(detailsPane, 20.0);
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
