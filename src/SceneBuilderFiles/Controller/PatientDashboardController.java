package SceneBuilderFiles.Controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Others.Bill;
import packages.Others.MedicalHistory;
import packages.Others.Schedule;
import packages.Person.Patient;

public class PatientDashboardController {

    private Patient patient;

    
    @FXML
    private Label mainContentTitle; // Label for displaying main content area title
    
    @FXML
    private AnchorPane mainContentArea;

    @FXML
    private Pane subOptionPane; // Pane for showing sub-option content dynamically

    @FXML
    private Label subOptionTitle; // Label for sub-options title


    public PatientDashboardController(){
        patient=null;
    }

    // Initialize method called automatically
    @FXML
    public void initialize() {
        System.out.println("Patient's Dashboard Initialized!");
        mainContentTitle.setText("Welcome to the Patient's Dashboard");
        subOptionPane.setVisible(false); // Hide sub-options by default
        
    }

    //Displays the home/overview section.
    @FXML
    private void showOverview(ActionEvent event) {
        mainContentTitle.setText("Home / Overview");
        
        System.out.println("Displaying Home / Overview.");
        
    }

     // Method to set Doctor object
    public void setPatient(Patient patient) {
        this.patient = patient;
        mainContentTitle.setText("Welcome, " + this.patient.getName());
        viewProfile();
    }

    @SuppressWarnings("unused")
    public void viewProfile() {
    mainContentTitle.setText("My Profile");
    
    // Create main content pane
    VBox profileBox = new VBox(15);
    profileBox.setPadding(new Insets(20));
    
    // Profile Information Section
    VBox infoBox = new VBox(10);
    infoBox.setStyle("-fx-padding: 20px; -fx-background-color: #f5f5f5; -fx-background-radius: 5px;");
    
    // Create labels for patient information
    Label nameLabel = new Label("Name: " + patient.getName());
    Label emailLabel = new Label("Email: " + patient.getEmail());
    Label phoneLabel = new Label("Phone: " + patient.getPhoneNumber());
    Label addressLabel = new Label("Address: " + patient.getAddress());
    
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
    String buttonStyle = "-fx-background-color: #e1722f; -fx-text-fill: white;";
    editProfileButton.setStyle(buttonStyle);
    changePasswordButton.setStyle(buttonStyle);
    
    // Button actions
    editProfileButton.setOnAction(e -> showEditProfileDialog());
    changePasswordButton.setOnAction(e -> showChangePasswordDialog());
    
    // Add all components to the main profile box
    profileBox.getChildren().addAll(infoBox, editProfileButton, changePasswordButton);
    
    // Add to main content area
    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().clear();
    mainContentPane.getChildren().addAll(mainContentTitle, profileBox);
    
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
    
    TextField nameField = new TextField(patient.getName());
    TextField phoneField = new TextField(patient.getPhoneNumber());
    TextField addressField = new TextField(patient.getAddress());
    
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
            // Update doctor object
            patient.setName(nameField.getText());
            patient.setPhoneNumber(phoneField.getText());
            patient.setAddress(addressField.getText());
            
            // Update in database
            boolean success = DatabaseConnection.updatePatientProfile(
                patient.getID(),
                nameField.getText(),
                phoneField.getText(),
                addressField.getText()
            );
            
            if (success) {
                showAlert(AlertType.INFORMATION, "Success", "Profile updated successfully!");
                viewProfile(); // Refresh the profile view
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
                patient.getEmail(),
                currentPassword,
                newPassword
            );
            
            if (success) {
                showAlert(AlertType.INFORMATION, "Success", "Password changed successfully!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to change password. Please check your current password and try again.");
            }
        }
    });
}

   

    public void viewPersonalDetails() {
        // Update the main content title
        mainContentTitle.setText("Personal Details");
    
        // Create a new AnchorPane for this content
        AnchorPane ap = new AnchorPane();
    
        // Retrieve Patient's personal details using the patient's ID
        patient = DatabaseConnection.getPatientPersonalDetails(patient.getID());
        System.out.println("------------------Patient ID "+patient.getID());
    
        if (patient != null) {
            // Create a label for "Name" and another for the actual name value
            Label nameValueLabel = new Label("Name:");
            Label nameLabel = new Label(patient.getName());
    
            // Create labels for the email and phone number
            Label emailValueLabel = new Label("Email:");
            Label emailLabel = new Label(patient.getEmail());
    
            Label phoneValueLabel = new Label("Phone:");
            Label phoneLabel = new Label(patient.getPhoneNumber());
    
            // Increase font size for all labels
            nameValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            nameLabel.setStyle("-fx-font-size: 18px;");
            
            emailValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            emailLabel.setStyle("-fx-font-size: 18px;");
            
            phoneValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            phoneLabel.setStyle("-fx-font-size: 18px;");
    
            // Set positions for the labels
            nameValueLabel.setLayoutX(33);
            nameValueLabel.setLayoutY(47);
            nameLabel.setLayoutX(181);
            nameLabel.setLayoutY(47);
    
            emailValueLabel.setLayoutX(33);
            emailValueLabel.setLayoutY(89);
            emailLabel.setLayoutX(181);
            emailLabel.setLayoutY(89);
    
            phoneValueLabel.setLayoutX(33);
            phoneValueLabel.setLayoutY(131);
            phoneLabel.setLayoutX(181);
            phoneLabel.setLayoutY(131);
    
            // Create an ImageView and set an image (change the path as per your image)
            ImageView profileImage = new ImageView();
            try {
                Image image = new Image("file:images/person.jpg"); // Adjust the path to your image file
                profileImage.setImage(image);
            } catch (Exception e) {
                System.out.println("Error: Unable to load image. " + e.getMessage());
            }
    
            // Set size for the image (adjust the size as necessary)
            profileImage.setFitWidth(200);
            profileImage.setFitHeight(150);
    
            // Position the image to the right of the labels
            profileImage.setLayoutX(450);  // Positioning the image on the right side
            profileImage.setLayoutY(47);   // Align the top of the image with the name label
    
            // Add all labels to the AnchorPane
            ap.getChildren().addAll(nameValueLabel, nameLabel, emailValueLabel, emailLabel, phoneValueLabel, phoneLabel, profileImage);
    
            // Clear any existing content and add the new AnchorPane to mainContentArea
            mainContentArea.getChildren().clear();
            mainContentArea.getChildren().add(ap);
        } else {
            // Display an error message if patient details are not found
            mainContentTitle.setText("Error: Patient details not found.");
        }
    }
    

    
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
        List<Appointment> appointments = DatabaseConnection.viewAppointments(patient.getEmail());

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
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("AppointedDay"));

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


    @SuppressWarnings({ "unchecked", "unused" })
    @FXML
    private void bookAppointment() {
        mainContentTitle.setText("Book New Appointment");
        if (patient == null) {
            mainContentTitle.setText("Error: Patient not found!");
            System.out.println("Patient is not set.");
            return;
        }
    
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear();
    
        VBox scheduleBox = new VBox(10);
        scheduleBox.setPadding(new Insets(20));
    
        // Create TableView
        TableView<Schedule> scheduleTable = new TableView<>();
        scheduleTable.setEditable(false);
    
        // Columns
        TableColumn<Schedule, String> docnameColumn = new TableColumn<>("Doctor Name");
        docnameColumn.setCellValueFactory(new PropertyValueFactory<>("DoctorName"));
        docnameColumn.setPrefWidth(200);  // Set preferred width for Doctor Name column

        TableColumn<Schedule, String> daysAvailableColumn = new TableColumn<>("Days Available");
        daysAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("availableDays"));
        daysAvailableColumn.setPrefWidth(250);  // Set preferred width for Doctor Name column

        scheduleTable.getColumns().addAll(docnameColumn, daysAvailableColumn);
    
        // ComboBoxes for specialization
        Label specializationLabel = new Label("Select Doctor Specialization:");
        ComboBox<String> docSpecialization = new ComboBox<>();
    
        List<String> allSpecializations = DatabaseConnection.distinctSpecialization();
        docSpecialization.getItems().addAll(allSpecializations);
        docSpecialization.setPromptText("Specialization");
    
        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
        searchButton.setOnAction(event -> {
            String specialization = docSpecialization.getValue();
    
            if (specialization == null || specialization.isEmpty()) {
                showErrorDialog("Please select the doctor's specialization.");
                return;
            }
    
            // Fetch schedule data from database
            ObservableList<Schedule> scheduleList = DatabaseConnection.SpecializedAppoint(specialization);
            scheduleTable.setItems(scheduleList);
            scheduleTable.refresh();
        });
    
        // Add components to the layout
        scheduleBox.getChildren().addAll(specializationLabel, docSpecialization, searchButton, scheduleTable);
    
        // Add layout to main content area
        mainContentPane.getChildren().addAll(mainContentTitle, scheduleBox);
        AnchorPane.setTopAnchor(scheduleBox, 50.0);
        AnchorPane.setLeftAnchor(scheduleBox, 20.0);
        AnchorPane.setRightAnchor(scheduleBox, 20.0);
        AnchorPane.setBottomAnchor(scheduleBox, 20.0);
    
        System.out.println("Booking a new appointment.");
    
        scheduleTable.setOnMouseClicked(event -> {
            Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
            if (selectedSchedule != null) {
                int doctorID = selectedSchedule.getDoctorID();
                // Check if the patient already has an appointment with the selected doctor
                boolean hasAppointment = DatabaseConnection.hasExistingAppointment(patient.getID(), doctorID);
        
                if (hasAppointment) {
                    showErrorDialog("You already have an appointment with this doctor.");
                } else {
                    showDoctorDays(doctorID); // Proceed to select day and book
                }
            }
        });
        
    }

    
    @SuppressWarnings("unused")
    private void showDoctorDays(int doctorID) {
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear();
    
        VBox daySelectionBox = new VBox(10);
        daySelectionBox.setPadding(new Insets(20));
    
        Label availableDaysLabel = new Label("Select a Day:");
        ComboBox<String> availableDaysComboBox = new ComboBox<>();
    
        // Fetch available days for the selected doctor
        List<String> availableDays = DatabaseConnection.getDoctorAvailableDays(doctorID);
        if (availableDays.isEmpty()) {
            showErrorDialog("No available days for this doctor.");
            return;
        }
        availableDaysComboBox.getItems().addAll(availableDays);
    
        Button confirmButton = new Button("Confirm Appointment");
confirmButton.setOnAction(event -> {
    String selectedDay = availableDaysComboBox.getValue();

    if (selectedDay == null || selectedDay.isEmpty()) {
        showErrorDialog("Please select a day for the appointment.");
        return;
    }

    // Check if patient already has an appointment with this doctor (redundant double-check)
    boolean hasAppointment = DatabaseConnection.hasExistingAppointment(patient.getID(), doctorID);

    if (hasAppointment) {
        showErrorDialog("You already have an appointment with this doctor.");
        return;
    }

    // Proceed to book appointment
    boolean success = DatabaseConnection.checkAndBookAppointment(doctorID, selectedDay, patient.getID(), new Timestamp(System.currentTimeMillis()),DatabaseConnection.getDoctorSpecializationById(doctorID));

    if (success) {
        showConfirmationDialog("Appointment booked successfully for " + selectedDay + "!");
    } else {
        showErrorDialog("Booking failed! No slots available for the selected day.");
    }
});


    
        daySelectionBox.getChildren().addAll(availableDaysLabel, availableDaysComboBox, confirmButton);
        mainContentPane.getChildren().addAll(mainContentTitle, daySelectionBox);
        AnchorPane.setTopAnchor(daySelectionBox, 50.0);
        AnchorPane.setLeftAnchor(daySelectionBox, 20.0);
        AnchorPane.setRightAnchor(daySelectionBox, 20.0);
        AnchorPane.setBottomAnchor(daySelectionBox, 20.0);
    
        System.out.println("Doctor's available days displayed.");
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

    @SuppressWarnings({ "unchecked", "unused" })
    @FXML
    private void rescheduleAppointment(ActionEvent event) {
    mainContentTitle.setText("Reschedule Appointment");
    if (patient == null) {
        mainContentTitle.setText("Error: Patient not found!");
        System.out.println("Patient is not set.");
        return;
    }

    List<Appointment> appointments = DatabaseConnection.viewAppointments(patient.getEmail());

    // Check if no appointments are found
    if (appointments.isEmpty()) {
        showNoAppointmentsPopup(); // Show a popup informing no appointments to reschedule
        return; // Exit the method since there are no appointments to reschedule
    }

    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().clear(); // Clear existing content

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
    rescheduleButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
    rescheduleButton.setOnAction(e -> {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showErrorDialog("Please select an appointment to reschedule.");
            return;
        }

        // Proceed to select a new day for the same doctor
        selectNewDayForReschedule(selectedAppointment);
    });

    // Add the TableView and button to the main content area
    rescheduleBox.getChildren().addAll(appointmentTable, rescheduleButton);
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
private void selectNewDayForReschedule(Appointment oldAppointment) {
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
    @SuppressWarnings({ "unchecked", "unused" })
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
        List<Appointment> appointments = DatabaseConnection.viewAppointments(patient.getEmail());


        // Check if no appointments are found
        if (appointments.isEmpty()) {
            showNoAppointmentsPopup(); // Show a popup informing no appointments to reschedule
            return; // Exit the method since there are no appointments to reschedule
        }
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
    
        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Appointment Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("AppointedDay"));
    
        // Add columns to the table
        appointmentTable.getColumns().addAll(selectColumn, idColumn, patientColumn, doctorColumn, timeColumn);
    
        // Populate the TableView with the appointments
        appointmentTable.getItems().addAll(appointments);
    
        // Add "Cancel" button and event handler
        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
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
            boolean confirmation = showConfirmationDialog2("Are you sure you want to cancel the selected appointments?");
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
    
    private boolean showConfirmationDialog2(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

    @FXML
    private void viewHealthRecords(ActionEvent event) {
        VBox detailsPane = new VBox();
    detailsPane.setSpacing(10);
    List<Integer> patientIds=new ArrayList<>();
    patientIds.add(patient.getID());
    List<MedicalHistory> selectedPatient=DatabaseConnection.getMedicalReports(patientIds);
    // Display patient name in bold
    Label patientNameLabel = new Label(selectedPatient.get(0).getPatientName());
    patientNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    // Display allergies
    Label allergiesLabel = new Label("Allergies:");
    allergiesLabel.setStyle("-fx-font-weight: bold;");
    Label allergiesDetails = new Label(selectedPatient.get(0).getAllergies());

    // Display medications
    Label medicationsLabel = new Label("Medications:");
    medicationsLabel.setStyle("-fx-font-weight: bold;");
    Label medicationsDetails = new Label(selectedPatient.get(0).getMedications());

    // Similarly for past illnesses, surgeries, family history, and notes
    Label pastIllnessesLabel = new Label("Past Illnesses:");
    pastIllnessesLabel.setStyle("-fx-font-weight: bold;");
    Label pastIllnessesDetails = new Label(selectedPatient.get(0).getPastIllnesses());

    Label surgeriesLabel = new Label("Surgeries:");
    surgeriesLabel.setStyle("-fx-font-weight: bold;");
    Label surgeriesDetails = new Label(selectedPatient.get(0).getSurgeries());

    Label familyHistoryLabel = new Label("Family History:");
    familyHistoryLabel.setStyle("-fx-font-weight: bold;");
    Label familyHistoryDetails = new Label(selectedPatient.get(0).getFamilyHistory());

    Label notesLabel = new Label("Notes:");
    notesLabel.setStyle("-fx-font-weight: bold;");
    Label notesDetails = new Label(selectedPatient.get(0).getNotes());

    // Add all labels, the back button, and the update button to the details pane
    detailsPane.getChildren().addAll(
            patientNameLabel,
            allergiesLabel, allergiesDetails,
            medicationsLabel, medicationsDetails,
            pastIllnessesLabel, pastIllnessesDetails,
            surgeriesLabel, surgeriesDetails,
            familyHistoryLabel, familyHistoryDetails,
            notesLabel, notesDetails
    );

    // Add details pane to the main content
    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().forEach(child -> child.setVisible(false));
    mainContentPane.getChildren().add(detailsPane);

    // Position the details pane within the pane
    AnchorPane.setTopAnchor(detailsPane, 50.0);
    AnchorPane.setLeftAnchor(detailsPane, 20.0);
    AnchorPane.setRightAnchor(detailsPane, 20.0);
    }

    @FXML
    private void viewTestResults(ActionEvent event) {
        mainContentTitle.setText("View Test Results");
        // Add logic to fetch and display test results
        System.out.println("Viewing test results.");
    }

    @FXML
    private void downloadMedicalHistory(ActionEvent event) {
        mainContentTitle.setText("Download Medical History");
        // Add logic to generate and download medical history
        System.out.println("Downloading medical history.");
    }

    @FXML
    private void viewPrescriptions(ActionEvent event) {
        mainContentTitle.setText("Prescriptions");
        // Add logic to fetch and display prescriptions
        System.out.println("Viewing prescriptions.");
    }

    
    @SuppressWarnings({ "unchecked", "unused" })
    @FXML
    private void viewBillingDetails(ActionEvent event) {
        mainContentTitle.setText("Billing Details"); // Update the title to "Billing Details"
    
        ObservableList<Bill> billList = DatabaseConnection.getSpecificPatientBill(patient);
    
        TableView<Bill> billTable = new TableView<>();
        billTable.setItems(billList);
    
        TableColumn<Bill, Integer> billIdColumn = new TableColumn<>("Bill ID");
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    
        TableColumn<Bill, String> patientNameColumn = new TableColumn<>("Patient Name");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    
        TableColumn<Bill, Double> amountColumn = new TableColumn<>("Total Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    
        TableColumn<Bill, Double> remainingAmountColumn = new TableColumn<>("Remaining Amount");
        remainingAmountColumn.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));
    
        TableColumn<Bill, String> paymentStatusColumn = new TableColumn<>("Payment Status");
        paymentStatusColumn.setCellValueFactory(cellData -> {
            boolean isPaid = cellData.getValue().getPaid();
            return new SimpleStringProperty(isPaid ? "Paid" : "Unpaid");
        });
    
        TableColumn<Bill, Void> makePaymentColumn = new TableColumn<>("Actions");
        makePaymentColumn.setCellFactory(param -> new TableCell<>() {
            private final Button makePaymentButton = new Button("Make Payment");
    
            {
                makePaymentButton.setStyle("""
                    -fx-background-color: #4CAF50;
                    -fx-text-fill: white;
                    -fx-font-size: 12px;
                    -fx-padding: 5px;
                    -fx-border-radius: 3px;
                    -fx-background-radius: 3px;
                """);
                makePaymentButton.setOnAction(event -> {
                    Bill selectedBill = getTableView().getItems().get(getIndex());
                    makePayment(selectedBill);
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getPaid()) {
                    setGraphic(null); // Hide the button for paid bills
                } else {
                    setGraphic(makePaymentButton);
                }
            }
        });
    
        billTable.getColumns().addAll(billIdColumn, patientNameColumn, amountColumn, remainingAmountColumn, paymentStatusColumn, makePaymentColumn);
    
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        backButton.setOnAction(e -> viewPersonalDetails());
    
        
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().addAll(backButton, billTable);
    }
    


    @SuppressWarnings("unused")
    @FXML
    private void makePayment(Bill bill) {
        mainContentTitle.setText("Make a Payment");
        mainContentArea.getChildren().clear();

        Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Enter card number (16 digits)");

        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("MM/YY");

        Label cvcLabel = new Label("CVC:");
        TextField cvcField = new TextField();
        cvcField.setPromptText("Enter 3-digit CVC");

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField(String.valueOf(bill.getRemainingAmount()));
        amountField.setDisable(true);

        Button submitButton = new Button("Submit Payment");
        submitButton.setStyle("""
            -fx-background-color: #4CAF50;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
            -fx-border-radius: 5px;
            -fx-background-radius: 5px;
        """);

        submitButton.setOnAction(e -> {
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvc = cvcField.getText();

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                return;
            }

            if (!cardNumber.matches("\\d{16}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Card", "Please enter a valid 16-digit card number.");
                return;
            }

            if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Expiry Date", "Please enter a valid expiry date (MM/YY).");
                return;
            }

            if (!cvc.matches("\\d{3}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid CVC", "Please enter a valid 3-digit CVC.");
                return;
            }

            String cardType = cardNumber.startsWith("4") ? "Visa" : cardNumber.startsWith("5") ? "MasterCard" : "Unknown Card Type";

            VBox processingLayout = new VBox(10, new ProgressIndicator());
            processingLayout.setAlignment(Pos.CENTER);
            mainContentArea.getChildren().clear();
            mainContentArea.getChildren().add(processingLayout);

            new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                DatabaseConnection.updateBillStatus(bill.getID(), 0, true);
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Payment processed for " + cardType + ".");
                    viewBillingDetails(new ActionEvent());
                });
            })).play();
        });

        VBox formLayout = new VBox(10, cardNumberLabel, cardNumberField, expiryDateLabel, expiryDateField, cvcLabel, cvcField, amountLabel, amountField, submitButton);
        formLayout.setStyle("-fx-padding: 20px;");
        mainContentArea.getChildren().add(formLayout);
    }





    @FXML
    private void downloadInvoice(ActionEvent event) {
        mainContentTitle.setText("Download Invoice");
        // Add logic to generate and download invoice
        System.out.println("Downloading invoice.");
    }

    @FXML
    private void viewNotifications(ActionEvent event) {
        mainContentTitle.setText("Notifications");
        // Add logic to fetch and display notifications
        System.out.println("Viewing notifications.");
    }



   @SuppressWarnings("unused")
@FXML
private void openHelp(ActionEvent event) {
    mainContentTitle.setText("Help and Support");


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
            boolean isComplaintInserted = DatabaseConnection.insertComplaint("Patient", complaintText);
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

    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().forEach(child -> child.setVisible(false));
    mainContentPane.getChildren().add(helpLayout);
}

 // Helper method to show alert messages
 private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
}

}
