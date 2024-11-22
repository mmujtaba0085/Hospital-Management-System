package SceneBuilderFiles.Controller;

import java.sql.Date;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Others.Bill;
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
    
    public void viewPersonalDetails() {
        // Update the main content title
        mainContentTitle.setText("Personal Details");
    
        // Create a new AnchorPane for this content
        AnchorPane ap = new AnchorPane();
    
        // Retrieve Admin's personal details using the admin's ID
        admin = DatabaseConnection.getAdminPersonalDeatils(admin.getID());
    
        if (admin != null) {
            // Create a label for "Name" and another for the actual name value
            Label nameValueLabel = new Label("Name:");
            Label nameLabel = new Label(admin.getName());
    
            // Create labels for the email and phone number
            Label emailValueLabel = new Label("Email:");
            Label emailLabel = new Label(admin.getEmail());
    
            Label phoneValueLabel = new Label("Phone:");
            Label phoneLabel = new Label(admin.getPhoneNumber());
    
            // Increase font size for all labels
            nameValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            nameLabel.setStyle("-fx-font-size: 18px;");
            
            emailValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            emailLabel.setStyle("-fx-font-size: 18px;");
            
            phoneValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            phoneLabel.setStyle("-fx-font-size: 18px;");
    
            // Set positions for the labels
            nameLabel.setLayoutX(181);
            nameLabel.setLayoutY(47);
            
            nameValueLabel.setLayoutX(33);  // Placing the value label next to "Name"
            nameValueLabel.setLayoutY(47);
    
            emailLabel.setLayoutX(181);
            emailLabel.setLayoutY(89);
            
            emailValueLabel.setLayoutX(33);  // Placing the value label next to "Email"
            emailValueLabel.setLayoutY(89);
    
            phoneLabel.setLayoutX(181);
            phoneLabel.setLayoutY(131);
            
            phoneValueLabel.setLayoutX(33);  // Placing the value label next to "Phone"
            phoneValueLabel.setLayoutY(131);

            // Create an ImageView and set an image (change the path as per your image)
            ImageView profileImage = new ImageView();
            Image image = new Image("file:images/person.jpg"); // Adjust the path to your image file
            profileImage.setImage(image);
            
            // Set size for the image (adjust the size as necessary)
            profileImage.setFitWidth(200);
            profileImage.setFitHeight(150);
            
            // Position the image to the right of the labels
            profileImage.setLayoutX(450);  // Positioning the image on the right side
            profileImage.setLayoutY(47);   // Align the top of the image with the name label
    
            // Add all labels to the AnchorPane
            ap.getChildren().addAll(nameLabel, nameValueLabel, emailLabel, emailValueLabel, phoneLabel, phoneValueLabel, profileImage);
    
            // Clear any existing content and add the new AnchorPane to mainContentArea
            mainContentArea.getChildren().clear();
            mainContentArea.getChildren().add(ap);
        } else {
            // Display an error message if admin details are not found
            mainContentTitle.setText("Error: Admin details not found.");
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
    
        // Create a FilteredList to enable searching
        FilteredList<Patient> filteredList = new FilteredList<>(patientObservableList, p -> true);
    
        // Create a TextField for search input
        TextField searchBox = new TextField();
        searchBox.setPromptText("Search by Name...");
        searchBox.setLayoutX(10);  // Position the search box
        searchBox.setLayoutY(10);
        searchBox.setPrefWidth(300);
    
        // Add a listener to the search box for real-time filtering
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(patient -> {
                // If the search box is empty, show all patients
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
    
                // Compare patient name with the search input
                String lowerCaseFilter = newValue.toLowerCase();
                return patient.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    
        // Create a TableView for displaying patients
        TableView<Patient> patientTable = new TableView<>();
        patientTable.setItems(filteredList);
    
        // Define TableColumn for Patient ID
        TableColumn<Patient, Integer> idColumn = new TableColumn<>("Patient ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    
        // Define TableColumn for Name
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    
        // Define TableColumn for Email
        TableColumn<Patient, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    
        // Define TableColumn for Phone Number
        TableColumn<Patient, String> numberColumn = new TableColumn<>("Phone Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    
        // Define TableColumn for Check-up Date
        TableColumn<Patient, Date> checkupColumn = new TableColumn<>("Check-up Date");
        checkupColumn.setCellValueFactory(new PropertyValueFactory<>("checkupDate"));
    
        // Add all columns to the TableView
        patientTable.getColumns().addAll(idColumn, nameColumn, emailColumn, numberColumn, checkupColumn);
    
        // Adjust TableView layout
        patientTable.setLayoutX(10);
        patientTable.setLayoutY(50);
        patientTable.setPrefWidth(mainContentArea.getPrefWidth() - 20);
        patientTable.setPrefHeight(mainContentArea.getPrefHeight() - 60);
    
        // Add a click listener to the TableView
        patientTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click to view details
                Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
                if (selectedPatient != null) {
                    // Call a method to display patient details
                    displayPatientDetails(selectedPatient);
                }
            }
        });
    
        // Add the search box and the TableView to the mainContentArea
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().addAll(searchBox, patientTable);
    }
    
    // Method to display selected patient's details
    private void displayPatientDetails(Patient patient) {
        // Create a new AnchorPane for displaying details
        AnchorPane detailsPane = new AnchorPane();

        // Create labels for patient details
        Label idLabel = new Label("Patient ID: " + patient.getID());
        idLabel.setLayoutX(20);
        idLabel.setLayoutY(20);
        idLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Name: " + patient.getName());
        nameLabel.setLayoutX(20);
        nameLabel.setLayoutY(60);
        nameLabel.setStyle("-fx-font-size: 16px;");

        Label emailLabel = new Label("Email: " + patient.getEmail());
        emailLabel.setLayoutX(20);
        emailLabel.setLayoutY(100);
        emailLabel.setStyle("-fx-font-size: 16px;");

        Label phoneLabel = new Label("Phone Number: " + patient.getPhoneNumber());
        phoneLabel.setLayoutX(20);
        phoneLabel.setLayoutY(140);
        phoneLabel.setStyle("-fx-font-size: 16px;");

        Label checkupDateLabel = new Label("Check-up Date: " + patient.getCheckupDate());
        checkupDateLabel.setLayoutX(20);
        checkupDateLabel.setLayoutY(180);
        checkupDateLabel.setStyle("-fx-font-size: 16px;");

        // Create a Back Button
        Button backButton = new Button("Back");
        backButton.setLayoutX(20);
        backButton.setLayoutY(220);
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Set the back button action to reload the patient list
        backButton.setOnAction(event -> viewPatientList());

        // Add all components to the detailsPane
        detailsPane.getChildren().addAll(idLabel, nameLabel, emailLabel, phoneLabel, checkupDateLabel, backButton);

        // Clear the mainContentArea and display the detailsPane
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(detailsPane);
    }

    


    @SuppressWarnings("unchecked")
    @FXML
    public void viewDoctorList() {
        mainContentTitle.setText("Doctor List");
    
        // Retrieve all doctors from the database
        LinkedList<Doctor> doctorList = DatabaseConnection.getAllDoctors();
    
        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList(doctorList);
    
        // Create a FilteredList for search functionality
        FilteredList<Doctor> filteredList = new FilteredList<>(doctorObservableList, p -> true);
    
        // Create a TextField for search input
        TextField searchBox = new TextField();
        searchBox.setPromptText("Search by Name...");
        searchBox.setLayoutX(10); // Position the search box
        searchBox.setLayoutY(10);
        searchBox.setPrefWidth(300);
    
        // Add a listener to filter the list based on search input
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(doctor -> {
                // If the search box is empty, show all doctors
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
    
                // Compare doctor name with the search input
                String lowerCaseFilter = newValue.toLowerCase();
                return doctor.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    
        // Create a TableView for displaying doctors
        TableView<Doctor> doctorTable = new TableView<>();
        doctorTable.setItems(filteredList);
    
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
    
        // Adjust TableView layout
        doctorTable.setPrefWidth(mainContentArea.getPrefWidth());
        doctorTable.setPrefHeight(mainContentArea.getPrefHeight() - 50); // Leave space for the search box
        doctorTable.setLayoutY(50); // Position the TableView below the search box
    
        // Add a double-click listener to display doctor details
        doctorTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !doctorTable.getSelectionModel().isEmpty()) {
                Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
                displayDoctorDetails(selectedDoctor);
            }
        });
    
        // Add the search box and table to the main content area
        mainContentArea.getChildren().clear(); // Clear existing content
        mainContentArea.getChildren().addAll(searchBox, doctorTable);
    }
    
    private void displayDoctorDetails(Doctor doctor) {
        // Create a new AnchorPane for displaying doctor details
        AnchorPane detailsPane = new AnchorPane();
    
        // Create labels for doctor's details
        Label idLabel = new Label("Doctor ID: " + doctor.getID());
        idLabel.setLayoutX(20);
        idLabel.setLayoutY(20);
        idLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        Label nameLabel = new Label("Name: " + doctor.getName());
        nameLabel.setLayoutX(20);
        nameLabel.setLayoutY(60);
        nameLabel.setStyle("-fx-font-size: 16px;");
    
        Label specialtyLabel = new Label("Specialization: " + doctor.getSpecialization());
        specialtyLabel.setLayoutX(20);
        specialtyLabel.setLayoutY(100);
        specialtyLabel.setStyle("-fx-font-size: 16px;");
    
        Label emailLabel = new Label("Email: " + doctor.getEmail());
        emailLabel.setLayoutX(20);
        emailLabel.setLayoutY(140);
        emailLabel.setStyle("-fx-font-size: 16px;");
    
        // Create a Back Button
        Button backButton = new Button("Back");
        backButton.setLayoutX(20);
        backButton.setLayoutY(180);
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    
        // Set the back button action to reload the doctor list
        backButton.setOnAction(event -> viewDoctorList());
    
        // Add all components to the detailsPane
        detailsPane.getChildren().addAll(idLabel, nameLabel, specialtyLabel, emailLabel, backButton);
    
        // Clear the mainContentArea and display the detailsPane
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().add(detailsPane);
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
        LinkedList<Bill> billList = DatabaseConnection.getAllBills();
    
        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Bill> billObservableList = FXCollections.observableArrayList(billList);

        // Create a TableView for displaying doctors
        TableView<Bill> billTable = new TableView<>();
        billTable.setItems(billObservableList);

        // Define TableColumn for Doctor ID
        TableColumn<Bill, Integer> idColumn = new TableColumn<>("Bill ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        // Define TableColumn for Name
        TableColumn<Bill, String> pColumn = new TableColumn<>("Payer's Name");
        pColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        // Define TableColumn for amount
        TableColumn<Bill, Double> aColumn = new TableColumn<>("Amount");
        aColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Define TableColumn for amount
        TableColumn<Bill, Boolean> dColumn = new TableColumn<>("Status");
        dColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
        dColumn.setCellFactory(column -> new TableCell<Bill, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Paid" : "Unpaid");
                }
            }
        });
        

        // Add all columns to the TableView
        billTable.getColumns().addAll(idColumn, pColumn, aColumn, dColumn);

        // Adjust TableView layout and add it to the mainContentArea
        billTable.setPrefWidth(mainContentArea.getPrefWidth());
        billTable.setPrefHeight(mainContentArea.getPrefHeight());
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().add(billTable);
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
