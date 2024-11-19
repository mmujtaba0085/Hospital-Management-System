package SceneBuilderFiles.Controller;

import packages.Others.Appointment;
import packages.Others.MedicalHistory;
import packages.Person.Doctor;
import packages.Others.Schedule;
import packages.Database.DatabaseConnection;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    @SuppressWarnings("unchecked")
    @FXML
    public void weekschedule() {
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear();
        mainContentTitle.setText("Set Weekly Schedule");
    
        VBox scheduleBox = new VBox(10);
        scheduleBox.setPadding(new Insets(20));
    
        // Create TableView
        TableView<Schedule> scheduleTable = new TableView<>();
        scheduleTable.setEditable(false);
    
        // Columns
        TableColumn<Schedule, String> dayColumn = new TableColumn<>("Day");
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
    
        TableColumn<Schedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    
        TableColumn<Schedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    
        scheduleTable.getColumns().addAll(dayColumn, startTimeColumn, endTimeColumn);
    
        // Observable list to hold schedule data
        ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
    
        // Populate table with default days
        for (String day : List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")) {
            scheduleList.add(new Schedule(day, "", ""));
        }
    
        // Load existing schedules from the database
        ObservableList<Schedule> loadedSchedules = DatabaseConnection.viewDoctorSchedule(doctor);
        for (Schedule loadedSchedule : loadedSchedules) {
            for (Schedule schedule : scheduleList) {
                if (schedule.getDay().equals(loadedSchedule.getDay())) {
                    schedule.setStartTime(loadedSchedule.getStartTime());
                    schedule.setEndTime(loadedSchedule.getEndTime());
                }
            }
        }
    
        // Add data to TableView
        scheduleTable.setItems(scheduleList);
    
        // ComboBoxes for selecting time range
        Label timeLabel = new Label("Select Appointment Time Range (24-hour format):");
        ComboBox<Integer> startTimeComboBox = new ComboBox<>();
        ComboBox<Integer> endTimeComboBox = new ComboBox<>();
        ComboBox<String> weekDays = new ComboBox<>();
        for (int i = 0; i < 24 * 2; i++) { // 30-minute increments
            startTimeComboBox.getItems().add(i);
            endTimeComboBox.getItems().add(i);
        }
        weekDays.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    
        startTimeComboBox.setPromptText("Start Time");
        endTimeComboBox.setPromptText("End Time");
        weekDays.setPromptText("Select Day");
    
        // Save Schedule Button
        Button confirmButton = new Button("Save Schedule");
        confirmButton.setOnAction(event -> {
            Integer startIndex = startTimeComboBox.getValue();
            Integer endIndex = endTimeComboBox.getValue();
            String selectedDay = weekDays.getValue();
    
            if (selectedDay == null || selectedDay.isEmpty()) {
                showErrorDialog("Please select at least one day.");
                return;
            }
            if (startIndex == null || endIndex == null) {
                showErrorDialog("Please select both start and end times.");
                return;
            }
            if (startIndex >= endIndex) {
                showErrorDialog("End time must be later than start time.");
                return;
            }
            if ((endIndex - startIndex) < 4) { // 4 increments of 30 mins = 2 hours
                showErrorDialog("Selected time range must be at least 2 hours.");
                return;
            }
    
            String startTime = formatTime(startIndex);
            String endTime = formatTime(endIndex);
    
            // Save to database and update table
            DatabaseConnection.saveDoctorSchedule(doctor.getID(), selectedDay, startTime, endTime);
    
            // Update schedule in the table
            for (Schedule schedule : scheduleList) {
                if (schedule.getDay().equals(selectedDay)) {
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                }
            }
    
            // Refresh TableView
            scheduleTable.refresh();
            showInformationDialog("Schedule saved successfully!");
        });
    
        // Add components to the layout
        scheduleBox.getChildren().addAll(timeLabel, weekDays, startTimeComboBox, endTimeComboBox, confirmButton, scheduleTable);
    
        // Add layout to main content area
        mainContentPane.getChildren().addAll(mainContentTitle, scheduleBox);
        AnchorPane.setTopAnchor(scheduleBox, 50.0);
        AnchorPane.setLeftAnchor(scheduleBox, 20.0);
        AnchorPane.setRightAnchor(scheduleBox, 20.0);
        AnchorPane.setBottomAnchor(scheduleBox, 20.0);
    }
    
    // Helper methods for formatting time and dialogs
    private String formatTime(int halfHours) {
        int hours = halfHours / 2;
        int minutes = (halfHours % 2) * 30;
        return String.format("%02d:%02d", hours, minutes);
    }
    
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInformationDialog(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    

    @SuppressWarnings("unchecked")
    @FXML
    public void viewPatientDetails() {
        mainContentTitle.setText("Patient Details");
    
        // Create a VBox to hold the search box and table
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
    
        // Create a TextField for searching patients
        TextField searchBox = new TextField();
        searchBox.setPromptText("Enter Patient ID or Name...");
        searchBox.setMaxWidth(300);
    
        // Create a TableView for displaying patient details
        TableView<MedicalHistory> patientTable = new TableView<>();
    
        // Define TableColumns for the MedicalHistory properties
        TableColumn<MedicalHistory, Integer> patientIdColumn = new TableColumn<>("Patient ID");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
    
        TableColumn<MedicalHistory, String> patientNameColumn = new TableColumn<>("Patient Name");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    
        TableColumn<MedicalHistory, String> allergiesColumn = new TableColumn<>("Allergies");
        allergiesColumn.setCellValueFactory(new PropertyValueFactory<>("allergies"));
    
        TableColumn<MedicalHistory, String> medicationsColumn = new TableColumn<>("Medications");
        medicationsColumn.setCellValueFactory(new PropertyValueFactory<>("medications"));
    
        TableColumn<MedicalHistory, Timestamp> lastUpdatedColumn = new TableColumn<>("Last Updated");
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
    
        // Add columns to the table
        patientTable.getColumns().addAll(
                patientIdColumn,
                patientNameColumn,
                allergiesColumn,
                medicationsColumn,
                lastUpdatedColumn
        );
    
        // Add the search box and table to the VBox
        vbox.getChildren().addAll(searchBox, patientTable);
    
        // Add the VBox to the main content area
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear();
        mainContentPane.getChildren().addAll(mainContentTitle, vbox);
    
        // Position the VBox within the pane
        AnchorPane.setTopAnchor(vbox, 50.0);
        AnchorPane.setLeftAnchor(vbox, 20.0);
        AnchorPane.setRightAnchor(vbox, 20.0);
        AnchorPane.setBottomAnchor(vbox, 20.0);
    
        // Add an event handler to perform the search when the user presses Enter
        searchBox.setOnAction(event -> {
            String query = searchBox.getText().trim();
            if (!query.isEmpty()) {
                // Query the database
                List<MedicalHistory> results = DatabaseConnection.searchMedicalHistory(query);
    
                if (results.isEmpty()) {
                    System.out.println("No matching records found.");
                    patientTable.getItems().clear(); // Clear previous results
                    mainContentTitle.setText("No matching records found.");
                } else {
                    // Update the table with the results
                    patientTable.getItems().clear();
                    patientTable.getItems().addAll(results);
                    mainContentTitle.setText("Search Results");
                }
            } else {
                // If search box is empty, clear results
                patientTable.getItems().clear();
                mainContentTitle.setText("Enter a valid search query.");
            }
        });
    
        // Set an event listener for when a patient is selected in the TableView
        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayPatientDetails(newValue,2);
            }
        });
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
                displayPatientDetails(newValue,1);
            }
        });
    }

    private void displayPatientDetails(MedicalHistory selectedPatient, int prevTab) {
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
            if(prevTab==1)
            {
                viewMedicalHistory(); // Reload the previous TableView
            }
            else if(prevTab==2)
            {
                viewPatientDetails();
            }
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
