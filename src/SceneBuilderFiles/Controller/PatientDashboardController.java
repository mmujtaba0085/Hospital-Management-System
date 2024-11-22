package SceneBuilderFiles.Controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Others.Bill;
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
    @SuppressWarnings("unused")
    @FXML
    private void bookAppointment(ActionEvent event) {
        mainContentTitle.setText("Book New Appointment");

        if (patient == null) {
            mainContentTitle.setText("Error: Patient not found!");
            System.out.println("Patient is not set.");
            return;
        }

        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().clear();
        mainContentTitle.setText("Set Weekly Schedule");

        VBox scheduleBox = new VBox(10);
        scheduleBox.setPadding(new Insets(20));

        // Create TableView
        TableView<Schedule> scheduleTable = new TableView<>();
        scheduleTable.setEditable(false);

        // Columns
        TableColumn<Schedule, String> docnameColumn = new TableColumn<>("Doctor Name");
        docnameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorname"));

        TableColumn<Schedule, String> dayColumn = new TableColumn<>("Day");
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));

        TableColumn<Schedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Schedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        // ComboBoxes for selecting time range, specialization, date, and doctor
        Label timeLabel = new Label("Select Appointment Details:");
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        ComboBox<Integer> startTimeComboBox = new ComboBox<>();
        startTimeComboBox.setLayoutX(100);
        startTimeComboBox.setLayoutY(50);
        startTimeComboBox.setPrefWidth(200);  // Set the preferred width to 200px
        startTimeComboBox.setPrefHeight(40);  // Set the preferred height to 40px
        startTimeComboBox.setStyle("-fx-background-color: lightgray; " + // background color
                            "-fx-font-size: 14px; " +           // font size
                            "-fx-text-fill: black;");
        ComboBox<Integer> endTimeComboBox = new ComboBox<>();
        endTimeComboBox.setLayoutX(100);
        endTimeComboBox.setLayoutY(30);
        endTimeComboBox.setPrefWidth(200);  // Set the preferred width to 200px
        endTimeComboBox.setPrefHeight(40);  // Set the preferred height to 40px
        endTimeComboBox.setStyle("-fx-background-color: lightgray; " + // background color
                            "-fx-font-size: 14px; " +           // font size
                            "-fx-text-fill: black;");
        ComboBox<String> docSpecialization = new ComboBox<>();
        docSpecialization.setLayoutX(100);
        docSpecialization.setLayoutY(30);
        docSpecialization.setPrefWidth(200);  // Set the preferred width to 200px
        docSpecialization.setPrefHeight(40);  // Set the preferred height to 40px
        docSpecialization.setStyle("-fx-background-color: lightgray; " + // background color
                            "-fx-font-size: 14px; " +           // font size
                            "-fx-text-fill: black;");
        ComboBox<String> doctorComboBox = new ComboBox<>();
        doctorComboBox.setLayoutX(100);
        doctorComboBox.setLayoutY(30);
        doctorComboBox.setPrefWidth(200);  // Set the preferred width to 200px
        doctorComboBox.setPrefHeight(40);  // Set the preferred height to 40px
        doctorComboBox.setStyle("-fx-background-color: lightgray; " + // background color
                            "-fx-font-size: 14px; " +           // font size
                            "-fx-text-fill: black;");
        DatePicker appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setLayoutX(100);
        appointmentDatePicker.setLayoutY(30);
        appointmentDatePicker.setPrefWidth(200);  // Set the preferred width to 200px
        appointmentDatePicker.setPrefHeight(40);  // Set the preferred height to 40px
        appointmentDatePicker.setStyle("-fx-background-color: lightgray; " + // background color
                            "-fx-font-size: 14px; " +           // font size
                            "-fx-text-fill: black;");

        for (int i = 0; i < 24; i++) {
            startTimeComboBox.getItems().add(i);
            endTimeComboBox.getItems().add(i);
        }

        List<String> allSpecializations = DatabaseConnection.distinctSpecialization();
        for (String specialization : allSpecializations) {
            docSpecialization.getItems().add(specialization);
        }

        startTimeComboBox.setPromptText("Start Time");
        endTimeComboBox.setPromptText("End Time");
        docSpecialization.setPromptText("Specialization");
        appointmentDatePicker.setPromptText("Select Appointment Date");
        doctorComboBox.setPromptText("Select Doctor");

        // Update the doctorComboBox when a specialization is selected
        docSpecialization.setOnAction(e -> {
            String selectedSpecialization = docSpecialization.getValue();
            List<String> doctors = DatabaseConnection.getDoctorsBySpecialization(selectedSpecialization);
            doctorComboBox.getItems().clear();
            doctorComboBox.getItems().addAll(doctors);
        });

        // Create the "Book Appointment" button
        Button bookButton = new Button("Book Appointment");
        bookButton.setStyle("-fx-background-color:  #e1722f; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        bookButton.setOnAction(e -> {
            Integer startTime = startTimeComboBox.getValue();
            Integer endTime = endTimeComboBox.getValue();
            String specialization = docSpecialization.getValue();
            LocalDate appointmentDate = appointmentDatePicker.getValue();
            String selectedDoctor = doctorComboBox.getValue();

            if (startTime == null || endTime == null || specialization == null || appointmentDate == null || selectedDoctor == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields before booking.");
                return;
            }

            if (startTime >= endTime) {
                showAlert(Alert.AlertType.ERROR, "Error", "Start time must be earlier than end time.");
                return;
            }

            // Add logic to book the appointment in the database
            boolean isBooked = DatabaseConnection.bookAppointmentWithDoctor(patient.getID(), specialization, startTime, endTime, appointmentDate, DatabaseConnection.getDoctorIDByName(selectedDoctor));
            if (isBooked) {
                showConfirmationDialog(selectedDoctor);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to book the appointment.");
            }
        });

        // Add components to the layout
        scheduleBox.getChildren().addAll(timeLabel, docSpecialization, doctorComboBox, appointmentDatePicker, startTimeComboBox, endTimeComboBox, bookButton);

        // Add layout to main content area
        mainContentPane.getChildren().addAll(mainContentTitle, scheduleBox);
        AnchorPane.setTopAnchor(scheduleBox, 50.0);
        AnchorPane.setLeftAnchor(scheduleBox, 20.0);
        AnchorPane.setRightAnchor(scheduleBox, 20.0);
        AnchorPane.setBottomAnchor(scheduleBox, 20.0);

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
    
    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    @SuppressWarnings({ "unchecked", "unused" })
    @FXML
    private void viewBillingDetails(ActionEvent event) {
        mainContentTitle.setText("Billing Details");

        // Fetch billing information for the specific patient
        LinkedList<Bill> billList = DatabaseConnection.getSpecificPatientBill(patient);

        // Convert the LinkedList to an ObservableList for the TableView
        ObservableList<Bill> billObservableList = FXCollections.observableArrayList(billList);

        // Create a TableView for displaying bills
        TableView<Bill> billTable = new TableView<>();
        billTable.setItems(billObservableList);

        // Define TableColumn for Bill ID
        TableColumn<Bill, Integer> billIdColumn = new TableColumn<>("Bill ID");
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        // Define TableColumn for Patient Name
        TableColumn<Bill, String> patientNameColumn = new TableColumn<>("Patient Name");
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        // Define TableColumn for Amount
        TableColumn<Bill, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Define TableColumn for Payment Status
        TableColumn<Bill, String> paymentStatusColumn = new TableColumn<>("Payment Status");
        paymentStatusColumn.setCellValueFactory(cellData -> {
            boolean isPaid = cellData.getValue().getPaid();
            return new SimpleStringProperty(isPaid ? "Paid" : "Unpaid");
        });

        // Add all columns to the TableView
        billTable.getColumns().addAll(billIdColumn, patientNameColumn, amountColumn, paymentStatusColumn);

        // Adjust TableView layout
        billTable.setPrefWidth(mainContentArea.getPrefWidth());
        billTable.setPrefHeight(mainContentArea.getPrefHeight() - 50);
        billTable.setLayoutY(50);

        // Create a Back Button
        Button backButton = new Button("Back");
        backButton.setLayoutX(10);
        backButton.setLayoutY(10);
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Set the action for the Back Button to navigate to the previous screen
        backButton.setOnAction(e -> {
            // Replace this with the method to navigate back to the patient dashboard or relevant screen
            viewPersonalDetails();
        });

        // Clear existing content in the mainContentArea and add the new components
        mainContentArea.getChildren().clear();
        mainContentArea.getChildren().addAll(backButton, billTable);
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
