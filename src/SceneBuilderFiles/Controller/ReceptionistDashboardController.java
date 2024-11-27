package SceneBuilderFiles.Controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Others.Bill;
import packages.Person.Doctor;
import packages.Person.Patient;
import packages.Person.Receptionist;

public class ReceptionistDashboardController{
    private Receptionist receptionist;

    // Constructor
    public ReceptionistDashboardController() {
        receptionist = null; // Initialize with null until set later
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
        System.out.println("Receptionist's Dashboard Initialized!");
        mainContentTitle.setText("Welcome to the Receptionist's Dashboard");
        subOptionPane.setVisible(false); // Hide sub-options by default
    }

    // Method to set receptionist object
    public void setReceptionist(Receptionist receptionist) {
        this.receptionist = receptionist;
        mainContentTitle.setText("Welcome, " + this.receptionist.getName());
        viewPersonalDetails();
    }

    // Sidebar Handlers
    @FXML
    public void showOverview() {
        mainContentTitle.setText("Overview");
    }

    public void viewPersonalDetails() {
    mainContentTitle.setText("My Profile");
    
    // Create main content pane
    VBox profileBox = new VBox(15);
    profileBox.setPadding(new Insets(20));
    
    // Profile Information Section
    VBox infoBox = new VBox(10);
    infoBox.setStyle("-fx-padding: 20px; -fx-background-color: #f5f5f5; -fx-background-radius: 5px;");
    
    // Create labels for doctor information
    Label nameLabel = new Label("Name: " + receptionist.getName());
    Label emailLabel = new Label("Email: " + receptionist.getEmail());
    Label phoneLabel = new Label("Phone: " + receptionist.getPhoneNumber());
    Label addressLabel = new Label("Address: " + receptionist.getAddress());
    
    // Style the labels
    String labelStyle = "-fx-font-size: 14px;";
    nameLabel.setStyle(labelStyle);
    emailLabel.setStyle(labelStyle);
    phoneLabel.setStyle(labelStyle);
    addressLabel.setStyle(labelStyle);
    
    infoBox.getChildren().addAll(nameLabel, emailLabel, phoneLabel, addressLabel);
    
    // Create buttons
    Button editProfileButton = new Button("Edit Profile");
    Button changePasswordButton = new Button("Change Password");
    
    // Style the buttons
    String buttonStyle = "-fx-background-color: #e1722f; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 150px;";
    editProfileButton.setStyle(buttonStyle);
    changePasswordButton.setStyle(buttonStyle);
    
    // Button actions
    editProfileButton.setOnAction(e -> showEditProfileDialog());
    changePasswordButton.setOnAction(e -> showChangePasswordDialog());
    
    // Add all components to the main profile box
    profileBox.getChildren().addAll(infoBox, editProfileButton, changePasswordButton);
    
   
    mainContentArea.getChildren().clear();
    mainContentArea.getChildren().addAll(profileBox);
    
    // Set anchors
    AnchorPane.setTopAnchor(profileBox, 50.0);
    AnchorPane.setLeftAnchor(profileBox, 20.0);
    AnchorPane.setRightAnchor(profileBox, 20.0);
}

    private void showEditProfileDialog() {
    // Create a new dialog
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Edit Profile");
    dialog.setHeaderText("Update your profile information");
    
    // Create the dialog pane and add buttons
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
    // Create form fields
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));
    
    TextField nameField = new TextField(receptionist.getName());
    TextField phoneField = new TextField(receptionist.getPhoneNumber());
    TextField addressField = new TextField(receptionist.getAddress());
    
    // Add labels and fields to grid
    grid.add(new Label("Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Phone:"), 0, 1);
    grid.add(phoneField, 1, 1);
    grid.add(new Label("Address:"), 0, 2);
    grid.add(addressField, 1, 2);

    dialogPane.setContent(grid);
    
    // Handle the result
    dialog.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // Update admin object
            receptionist.setName(nameField.getText());
            receptionist.setPhoneNumber(phoneField.getText());
            receptionist.setAddress(addressField.getText());
            
            // Update in database
            boolean success = DatabaseConnection.updateReceptionistProfile(
                receptionist.getID(),
                nameField.getText(),
                phoneField.getText(),
                addressField.getText()
            );
            
            if (success) {
                showAlert(AlertType.INFORMATION, "Success", "Profile updated successfully!");
                viewPersonalDetails();// Refresh the profile view
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to update profile. Please try again.");
            }
        }
    });
}

    private void showChangePasswordDialog() {
    // Create a new dialog
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Change Password");
    dialog.setHeaderText("Enter your current and new password");
    
    // Create the dialog pane and add buttons
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    
    // Create form fields
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));
    
    PasswordField currentPasswordField = new PasswordField();
    PasswordField newPasswordField = new PasswordField();
    PasswordField confirmPasswordField = new PasswordField();
    
    // Add labels and fields to grid
    grid.add(new Label("Current Password:"), 0, 0);
    grid.add(currentPasswordField, 1, 0);
    grid.add(new Label("New Password:"), 0, 1);
    grid.add(newPasswordField, 1, 1);
    grid.add(new Label("Confirm Password:"), 0, 2);
    grid.add(confirmPasswordField, 1, 2);
    
    dialogPane.setContent(grid);
    
    // Handle the result
    dialog.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            // Validate passwords
            if (!newPassword.equals(confirmPassword)) {
                showAlert(AlertType.ERROR, "Error", "New passwords do not match!");
                return;
            }
            
            // Update password in database
            boolean success = DatabaseConnection.updatePassword(
                receptionist.getEmail(),
                currentPassword,
                newPassword
            );
            
            if (success) {
                showAlert2(AlertType.INFORMATION, "Success", "Password changed successfully!");
            } else {
                showAlert2(AlertType.ERROR, "Error", "Failed to change password. Please check your current password and try again.");
            }
        }
    });
}

    private void showAlert2(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @SuppressWarnings({ "unused", "unchecked" })
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
        searchBox.setLayoutX(10); // Position the search box
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
    
        // Add the search box, "Add New Patient" button, and TableView to the mainContentArea
        mainContentArea.getChildren().clear(); // Clear any existing content
        mainContentArea.getChildren().addAll(searchBox, patientTable);
    }
    
    
    // Method to display selected patient's details
    @SuppressWarnings("unused")
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

    @SuppressWarnings({ "unused", "unchecked" })
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
    
        // Add the search box, table, and button to the main content area
        mainContentArea.getChildren().clear(); // Clear existing content
        mainContentArea.getChildren().addAll(searchBox, doctorTable);
    }
    
    
    @SuppressWarnings("unused")
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
    public void viewAppointments() {
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

   
    
    private void showConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles rescheduling an appointment.
     */

    @SuppressWarnings({ "unused" })
    @FXML
    private void rescheduleAppointment(ActionEvent event) {
        mainContentTitle.setText("Reschedule Appointment");
    
        //Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentArea.getChildren().clear(); // Clear existing content
    
        VBox rescheduleBox = new VBox(10);
        rescheduleBox.setPadding(new Insets(20));
    
        // Input field for patient's email
        Label emailLabel = new Label("Enter Patient's Email:");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email address");
    
        Button fetchAppointmentsButton = new Button("Fetch Appointments");
        fetchAppointmentsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        fetchAppointmentsButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            Patient patient=DatabaseConnection.getPatientByEmail(email);
            if (email.isEmpty()) {
                showErrorDialog("Please enter a valid email.");
                return;
            }
    
            // Retrieve appointments for the specified email
            List<Appointment> appointments = DatabaseConnection.viewAppointments(email);
    
            // Check if no appointments are found
            if (appointments.isEmpty()) {
                showNoAppointmentsPopup(); // Show a popup informing no appointments to reschedule
                return; // Exit the method since there are no appointments to reschedule
            }
    
            // Display appointments in the TableView
            displayAppointmentsForReschedule(mainContentArea, appointments, patient);
        });
    
        // Add the email field and button to the reschedule box
        rescheduleBox.getChildren().addAll(emailLabel, emailField, fetchAppointmentsButton);
        mainContentArea.getChildren().addAll(mainContentTitle, rescheduleBox);
    
        AnchorPane.setTopAnchor(rescheduleBox, 50.0);
        AnchorPane.setLeftAnchor(rescheduleBox, 20.0);
        AnchorPane.setRightAnchor(rescheduleBox, 20.0);
        AnchorPane.setBottomAnchor(rescheduleBox, 20.0);
    }
    
    // Helper method to display appointments and handle rescheduling
    @SuppressWarnings({ "unchecked", "unused" })
    private void displayAppointmentsForReschedule(Pane mainContentPane, List<Appointment> appointments, Patient patient) {
        VBox rescheduleBox = new VBox(10);
        rescheduleBox.setPadding(new Insets(20));
    
        // Create a TableView for displaying appointments
        TableView<Appointment> appointmentTable = new TableView<>();
    
        // Define TableColumns for the Appointment properties
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
    
        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient Name");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    
        TableColumn<Appointment, String> doctorColumn = new TableColumn<>("Doctor Name");
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
    
        TableColumn<Appointment, String> dayColumn = new TableColumn<>("Appointed Day");
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("AppointedDay"));
    
        // Add columns to the table
        appointmentTable.getColumns().addAll(idColumn, patientColumn, doctorColumn, dayColumn);
    
        // Populate the TableView with the appointments
        appointmentTable.getItems().addAll(appointments);
    
        // Add a reschedule button
        Button rescheduleButton = new Button("Reschedule Selected");
        rescheduleButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        rescheduleButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                showErrorDialog("Please select an appointment to reschedule.");
                return;
            }
    
            // Proceed to select a new day for the same doctor
            selectNewDayForReschedule(selectedAppointment, patient);
        });
    
        // Add the TableView and button to the main content area
        rescheduleBox.getChildren().addAll(appointmentTable, rescheduleButton);
        mainContentPane.getChildren().clear();
        mainContentPane.getChildren().addAll(mainContentTitle, rescheduleBox);
    
        AnchorPane.setTopAnchor(rescheduleBox, 50.0);
        AnchorPane.setLeftAnchor(rescheduleBox, 20.0);
        AnchorPane.setRightAnchor(rescheduleBox, 20.0);
        AnchorPane.setBottomAnchor(rescheduleBox, 20.0);
    }
    

    private void showNoAppointmentsPopup() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION); // Information alert
    alert.setTitle("No Appointments");
    alert.setHeaderText(null);
    alert.setContentText("You have no appointments in scheduled.");
    alert.showAndWait(); // Display the alert and wait for user acknowledgment
}



@SuppressWarnings("unused")
private void selectNewDayForReschedule(Appointment oldAppointment, Patient patient) {
    mainContentTitle.setText("Select New Day for Reschedule");

    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().clear();

    VBox daySelectionBox = new VBox(10);
    daySelectionBox.setPadding(new Insets(20));

    Label availableDaysLabel = new Label("Select a New Day:");
    ComboBox<String> availableDaysComboBox = new ComboBox<>();

    int doctorID = DatabaseConnection.getDoctorIDByName(oldAppointment.getDoctorName());

    // Fetch available days for the same doctor
    List<String> availableDays = DatabaseConnection.getDoctorAvailableDays(doctorID);
    if (availableDays.isEmpty()) {
        showErrorDialog("No available days for this doctor.");
        return;
    }

    // Remove the current appointed day from the available days
    availableDays.remove(oldAppointment.getAppointedDay());
    if (availableDays.isEmpty()) {
        showErrorDialog("No other available days for this doctor.");
        return;
    }

    availableDaysComboBox.getItems().addAll(availableDays);

    Button confirmButton = new Button("Confirm Reschedule");
    confirmButton.setOnAction(event -> {
        String selectedDay = availableDaysComboBox.getValue();

        if (selectedDay == null || selectedDay.isEmpty()) {
            showErrorDialog("Please select a new day for the appointment.");
            return;
        }

        // Proceed to book the new appointment
        boolean success = DatabaseConnection.isSlotAvailable(DatabaseConnection.getDoctorIDByName(oldAppointment.getDoctorName()), selectedDay);

        if (success) {
            
            boolean cancelSuccess = DatabaseConnection.cancelAppointment(patient.getID(), oldAppointment.getDoctorName());
            if (cancelSuccess) {
                DatabaseConnection.checkAndBookAppointment(doctorID, selectedDay, patient.getID(), new Timestamp(System.currentTimeMillis()),DatabaseConnection.getDoctorSpecializationById(doctorID));
                showConfirmationDialog("Appointment rescheduled successfully to " + selectedDay + "!");
            } else {
                showErrorDialog("Failed to cancel the old appointment.");
            }
        } else {
            showErrorDialog("Rescheduling failed! No slots available for the selected day.");
        }
    });

    daySelectionBox.getChildren().addAll(availableDaysLabel, availableDaysComboBox, confirmButton);
    mainContentPane.getChildren().addAll(mainContentTitle, daySelectionBox);
    AnchorPane.setTopAnchor(daySelectionBox, 50.0);
    AnchorPane.setLeftAnchor(daySelectionBox, 20.0);
    AnchorPane.setRightAnchor(daySelectionBox, 20.0);
    AnchorPane.setBottomAnchor(daySelectionBox, 20.0);

    System.out.println("Selecting a new day for reschedule.");
}




    /**
     * Handles canceling an appointment.
     */
    @SuppressWarnings({ "unused" })
    @FXML
    private void cancelAppointment(ActionEvent event) {
        mainContentTitle.setText("Cancel Appointments");
    
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear(); // Clear existing content
    
        VBox cancelBox = new VBox(10);
        cancelBox.setPadding(new Insets(20));
    
        // Input field for patient's email
        Label emailLabel = new Label("Enter Patient's Email:");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email address");
    
        Button fetchAppointmentsButton = new Button("Fetch Appointments");
        fetchAppointmentsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        fetchAppointmentsButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                showErrorDialog("Please enter a valid email.");
                return;
            }
    
            // Retrieve appointments using the provided email
            List<Appointment> appointments = DatabaseConnection.viewAppointments(email);
    
            // Check if no appointments are found
            if (appointments.isEmpty()) {
                showNoAppointmentsPopup(); // Show a popup informing no appointments to cancel
                return;
            }
    
            // Display appointments in the TableView for cancelation
            displayAppointmentsForCancel(mainContentPane, appointments);
        });
    
        // Add the email field and button to the cancel box
        cancelBox.getChildren().addAll(emailLabel, emailField, fetchAppointmentsButton);
        mainContentPane.getChildren().addAll(mainContentTitle, cancelBox);
    
        AnchorPane.setTopAnchor(cancelBox, 50.0);
        AnchorPane.setLeftAnchor(cancelBox, 20.0);
        AnchorPane.setRightAnchor(cancelBox, 20.0);
        AnchorPane.setBottomAnchor(cancelBox, 20.0);
    }
    
    // Helper method to display appointments and handle cancellation
    @SuppressWarnings({ "unchecked", "unused" })
    private void displayAppointmentsForCancel(Pane mainContentPane, List<Appointment> appointments) {
        VBox cancelBox = new VBox(10);
        cancelBox.setPadding(new Insets(20));
    
        // Create a TableView for displaying appointments
        TableView<Appointment> appointmentTable = new TableView<>();
    
        // Define TableColumns for the Appointment properties
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
    
        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient Name");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    
        TableColumn<Appointment, String> doctorColumn = new TableColumn<>("Doctor Name");
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
    
        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Appointment Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointedDay"));
    
        // Add columns to the table
        appointmentTable.getColumns().addAll(idColumn, patientColumn, doctorColumn, timeColumn);
    
        // Populate the TableView with the appointments
        appointmentTable.getItems().addAll(appointments);
    
        // Add "Cancel" button and event handler
        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 14px;");
        cancelButton.setOnAction(e -> {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                showErrorDialog("Please select an appointment to cancel.");
                return;
            }
    
            // Show confirmation dialog
            boolean confirmation = showConfirmationDialog1("Are you sure you want to cancel this appointment?");
            if (!confirmation) {
                System.out.println("Cancellation aborted.");
                return;
            }
    
            // Cancel the selected appointment in the database
            boolean success = DatabaseConnection.deleteAppointment(selectedAppointment.getAppointmentID());
            if (success) {
                // Remove the canceled appointment from the TableView
                appointmentTable.getItems().remove(selectedAppointment);
                System.out.println("Appointment canceled successfully.");
            } else {
                showErrorDialog("Failed to cancel the appointment. Please try again.");
            }
        });
    
        // Add the TableView and button to the main content area
        cancelBox.getChildren().addAll(appointmentTable, cancelButton);
        mainContentPane.getChildren().clear();
        mainContentPane.getChildren().addAll(mainContentTitle, cancelBox);
    
        AnchorPane.setTopAnchor(cancelBox, 50.0);
        AnchorPane.setLeftAnchor(cancelBox, 20.0);
        AnchorPane.setRightAnchor(cancelBox, 20.0);
        AnchorPane.setBottomAnchor(cancelBox, 20.0);
    }

    // Method to show a confirmation dialog
    private boolean showConfirmationDialog1(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /*private boolean showConfirmationDialog2(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    } */

    @SuppressWarnings({ "unused", "unchecked" })
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

    @SuppressWarnings("unused")
    public void openHelp(ActionEvent event) {
        mainContentTitle.setText("Help and Support");

        // Clear previous content
        mainContentArea.getChildren().clear();

        // Title Label
        Label helpTitle = new Label("How can we assist you?");
        helpTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        helpTitle.setAlignment(Pos.CENTER);

        // Complaint Form Section
        Label complaintLabel = new Label("Submit a Complaint:");
        complaintLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextArea complaintTextArea = new TextArea();
        complaintTextArea.setPromptText("Describe your issue...");
        complaintTextArea.setStyle("-fx-pref-height: 100px; -fx-pref-width: 400px; -fx-padding: 5px;");

        Button submitComplaintButton = new Button("Submit");
        submitComplaintButton.setStyle("""
            -fx-background-color: #4CAF50;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 5px 10px;
            -fx-border-radius: 5px;
            -fx-background-radius: 5px;
        """);

        submitComplaintButton.setOnAction(e -> {
            String complaintText = complaintTextArea.getText();
            if (complaintText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please describe your issue before submitting.");
            } else {
                boolean isComplaintInserted = DatabaseConnection.insertComplaint("Receptionist", complaintText);
                if (isComplaintInserted) {
                    showAlert(Alert.AlertType.INFORMATION, "Complaint Submitted", "Your complaint has been recorded. We will address it promptly.");
                    complaintTextArea.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit your complaint. Please try again.");
                }
            }
        });
        

        // Contact Information Section
        Label contactLabel = new Label("Contact Hospital Administrators:");
        contactLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox contactDetails = new VBox(5,
            new Label("Phone: +1 234 567 890"),
            new Label("Email: admin@orenixhospital.com"),
            new Label("Address: 123 Health Street, Cityville, Country")
        );
        contactDetails.setStyle("-fx-font-size: 12px;");

        // FAQ Section
        Label faqLabel = new Label("Frequently Asked Questions:");
        faqLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox faqContent = new VBox(10,
            new Label("Q: How do I book an appointment?\nA: Navigate to the 'Book Appointment' section and choose your doctor."),
            new Label("Q: Can I cancel my appointment?\nA: Yes, go to your 'Appointments' section and select 'Cancel'."),
            new Label("Q: How can I pay for services?\nA: You can pay online using the 'Billing Details' section.")
        );
        faqContent.setStyle("-fx-font-size: 12px;");

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> viewPersonalDetails());

        // Layout for Complaint Section
        VBox complaintSection = new VBox(10, complaintLabel, complaintTextArea, submitComplaintButton);
        complaintSection.setAlignment(Pos.CENTER_LEFT);

        // Layout for Help and Support Page
        VBox helpLayout = new VBox(20, helpTitle, complaintSection, contactLabel, contactDetails, faqLabel, faqContent, backButton);
        helpLayout.setAlignment(Pos.TOP_CENTER);
        helpLayout.setPadding(new Insets(20));
        helpLayout.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 1px;");

        mainContentArea.getChildren().add(helpLayout);
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}