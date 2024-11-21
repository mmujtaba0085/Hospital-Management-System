package SceneBuilderFiles.Controller;

import java.sql.Date;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Person.Admin;
import packages.Person.Doctor;
import packages.Person.Patient;

public class AdminDashboardController {
    private Admin admin;

    // Constructor
    public AdminDashboardController() {
        admin = null; // Initialize with null until set later
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

    @FXML
    private AnchorPane mainContentArea;

    // Initialize method called automatically
    @FXML
    public void initialize() {
        System.out.println("Admin's Dashboard Initialized!");
        mainContentTitle.setText("Welcome to the Admin's Dashboard");
        subOptionPane.setVisible(false); // Hide sub-options by default
    }

    // Method to set Admin object
    public void setAdmin(Admin admin) {
        this.admin = admin;
        mainContentTitle.setText("Welcome, " + this.admin.getName());
    }

    // Sidebar Handlers
    @FXML
    public void showOverview() {
        mainContentTitle.setText("Overview");
    }

    @FXML
    public void viewSchedule() {
        if (admin == null) {
            mainContentTitle.setText("Error: Admin not found!");
            System.out.println("Admin is not set.");
            return;
        }

        mainContentTitle.setText("Appointments Schedule");
        System.out.println("Fetching appointments...");

        /*
        // Retrieve appointments using the admin's email
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(admin.getEmail());

        // Create a TableView for displaying appointments
        TableView<Appointment> appointmentTable = new TableView<>();

        // Define TableColumns for the Appointment properties
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient Name");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<Appointment, String> adminColumn = new TableColumn<>("Admin Name");
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("adminName"));

        TableColumn<Appointment, Timestamp> timeColumn = new TableColumn<>("Appointment Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));

        // Add columns to the table
        appointmentTable.getColumns().addAll(idColumn, patientColumn, adminColumn, timeColumn);

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
         */
    }

    // Additional Sidebar Handlers
    @FXML
    public void rescheduleAppointments() {
        Pane mainContentPane = (Pane) mainContentTitle.getParent(); // Assuming mainContentTitle is in the main content area
        mainContentPane.getChildren().clear(); // Clear existing content
        mainContentTitle.setText("Reschedule Appointments");
        mainContentPane.getChildren().addAll(mainContentTitle); // Add title and appointments list
    }

    
    @SuppressWarnings({ })
    @FXML
    public void cancelAppointments() {
        if (admin == null) {
            mainContentTitle.setText("Error: Admin not found!");
            System.out.println("Admin is not set.");
            return;
        }

        /*
        mainContentTitle.setText("Cancel Appointments");
        System.out.println("Fetching appointments...");
    
        // Retrieve appointments using the admin's email
        List<Appointment> appointments = DatabaseConnection.ViewAppointments(admin.getEmail());
    
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
                boolean success = DatabaseConnection.cancelAppointment(admin.getID(), patientName);
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
         */
    }
    
    @FXML
    public void viewPersonalDetails() {
        mainContentTitle.setText("Personal Details");
        // Retrieve Admin's Personal details using the admin's email
        admin = DatabaseConnection.getAdminPersonalDeatils(admin.getID());

        if (admin != null) {
            System.out.println("Admin Details Retrieved Successfully:");
            System.out.println("Name: " + admin.getName());
            System.out.println("Email: " + admin.getEmail());
            System.out.println("Phone: " + admin.getPhoneNumber());
        } else {
            System.out.println("Error: Admin details not found.");
        }
    }

    @SuppressWarnings({ "unchecked" })
    @FXML
    public void viewPatientList() {
        mainContentTitle.setText("Patient List");
        
        // Retrieve all patients from the database
        LinkedList<Patient> patientList = DatabaseConnection.getAllPatients();
        
        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Patient> patientObservableList = FXCollections.observableArrayList(patientList);

        // Create a TableView for displaying patients
        TableView<Patient> patientTable = new TableView<>();
        patientTable.setItems(patientObservableList);

        // Define TableColumn for Patient ID
        TableColumn<Patient, Integer> idColumn = new TableColumn<>("Patient ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        // Define TableColumn for Name
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Define TableColumn for Age
        TableColumn<Patient, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Define TableColumn for Gender
        TableColumn<Patient, String> numberColumn = new TableColumn<>("Phone Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Define TableColumn for Gender
        TableColumn<Patient, Date> checkupColumn = new TableColumn<>("Check-up Date");
        checkupColumn.setCellValueFactory(new PropertyValueFactory<>("checkupDate"));

        // Add all columns to the TableView
        patientTable.getColumns().addAll(idColumn, nameColumn, emailColumn, numberColumn);

        // Adjust TableView layout and add it to the mainContentArea
        patientTable.setPrefWidth(mainContentArea.getPrefWidth());
        patientTable.setPrefHeight(mainContentArea.getPrefHeight());
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().add(patientTable);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void viewDoctorList() {
        mainContentTitle.setText("Doctor List");
        LinkedList<Doctor> doctorList = DatabaseConnection.getAllDoctors();

        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList(doctorList);

        // Create a TableView for displaying doctors
        TableView<Doctor> doctorTable = new TableView<>();
        doctorTable.setItems(doctorObservableList);

        // Define TableColumn for Doctor ID
        TableColumn<Doctor, Integer> idColumn = new TableColumn<>("Doctor ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        // Define TableColumn for Name
        TableColumn<Doctor, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Define TableColumn for Specialty
        TableColumn<Doctor, String> specialtyColumn = new TableColumn<>("Specialty");
        specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Define TableColumn for Contact Information
        TableColumn<Doctor, String> contactColumn = new TableColumn<>("Email Address");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add all columns to the TableView
        doctorTable.getColumns().addAll(idColumn, nameColumn, specialtyColumn, contactColumn);

        // Adjust TableView layout and add it to the mainContentArea
        doctorTable.setPrefWidth(mainContentArea.getPrefWidth());
        doctorTable.setPrefHeight(mainContentArea.getPrefHeight());
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().add(doctorTable);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void viewAppointment() {
        mainContentTitle.setText("View Appointments");
        LinkedList<Appointment> appointmentList = DatabaseConnection.getAllAppointments();
    
        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList(appointmentList);

        // Create a TableView for displaying doctors
        TableView<Appointment> appointmentTable = new TableView<>();
        appointmentTable.setItems(appointmentObservableList);

        // Define TableColumn for Doctor ID
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        // Define TableColumn for Name
        TableColumn<Appointment, String> pColumn = new TableColumn<>("Patient Name");
        pColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        // Define TableColumn for Specialty
        TableColumn<Appointment, String> dColumn = new TableColumn<>("Doctor Name");
        dColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        // Add all columns to the TableView
        appointmentTable.getColumns().addAll(idColumn, pColumn, dColumn);

        // Adjust TableView layout and add it to the mainContentArea
        appointmentTable.setPrefWidth(mainContentArea.getPrefWidth());
        appointmentTable.setPrefHeight(mainContentArea.getPrefHeight());
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().add(appointmentTable);
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
    public void openSettings() {
        mainContentTitle.setText("Settings");
    }

    @FXML
    public void openHelp() {
        mainContentTitle.setText("Help and Support");
    }
}
