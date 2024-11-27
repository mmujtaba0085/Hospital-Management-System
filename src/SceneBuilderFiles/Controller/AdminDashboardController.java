package SceneBuilderFiles.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import packages.Database.DatabaseConnection;
import packages.Others.Appointment;
import packages.Others.Bill;
import packages.Others.Complaint;
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

    
    
    
    
    public void viewPersonalDetails() {
        // Update the main content title
        mainContentTitle.setText("Personal Details");
    
        // Create a new AnchorPane for this content
        AnchorPane ap = new AnchorPane();
    
        // Retrieve Admin's personal details using the admin's ID
        admin = DatabaseConnection.getAdminPersonalDeatils(admin.getID());
        System.out.println("------------------Admin ID "+admin.getID());
    
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
    

    @SuppressWarnings({ "unchecked", "unused" })
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
    
        // Create a Button for adding a new patient
        Button addNewPatientButton = new Button("Add New Patient");
        addNewPatientButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
        addNewPatientButton.setLayoutX(530); // Position the button next to the search box
        addNewPatientButton.setLayoutY(10);
        addNewPatientButton.setOnAction(event -> {
            // Call a method to open the form for adding a new patient
            openAddNewPatientForm();
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
        mainContentArea.getChildren().addAll(searchBox, addNewPatientButton, patientTable);
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

    @SuppressWarnings("unused")
    private void openAddNewPatientForm() {
        // Create a new stage for the add patient form
        Stage addPatientStage = new Stage();
        addPatientStage.setTitle("Add New Patient");

        // Create a VBox to hold the form elements
        VBox formVBox = new VBox(10); // Spacing between form elements
        formVBox.setPadding(new Insets(20));

        // Create labels and text fields for patient details
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Patient's name");
        
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Patient's email");

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter Patient's phone number");

        Label checkupLabel = new Label("Check-up Date:");
        DatePicker checkupDatePicker = new DatePicker();
        checkupDatePicker.setPromptText("Enter checkup date");

        // Create a button to submit the form
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Handle form submission
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            LocalDate checkupDate = checkupDatePicker.getValue();

            // Validate input data
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || checkupDate == null) {
                showError("All fields must be filled out!");
            } else {
                // Create a new Patient object with the provided data
                Patient newPatient = new Patient();
                newPatient.setName(name);
                newPatient.setEmail(email);
                newPatient.setPhoneNumber(phone);
                newPatient.setCheckupDate(Date.valueOf(checkupDate)); // Convert LocalDate to Date

                // Add the new patient to the database (implement the logic for adding a patient to your database)
                boolean success = DatabaseConnection.addNewPatient(newPatient);
                if (success) {
                    showInfo("Patient added successfully!");
                    addPatientStage.close();
                } else {
                    showError("Error adding patient. Please try again.");
                }
            }
        });

        // Create a button to cancel and close the form
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        cancelButton.setOnAction(e -> addPatientStage.close());

        // Create a HBox for buttons (Submit & Cancel)
        HBox buttonHBox = new HBox(10, submitButton, cancelButton);
        buttonHBox.setAlignment(Pos.CENTER);

        // Add all elements to the VBox
        formVBox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, checkupLabel, checkupDatePicker, buttonHBox);

        // Set the scene and show the stage
        Scene scene = new Scene(formVBox, 400, 350);
        addPatientStage.setScene(scene);
        addPatientStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @SuppressWarnings({ "unchecked", "unused" })
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
    
        // Create an "Add New Doctor" button
        Button addDoctorButton = new Button("Add New Doctor");
        addDoctorButton.setLayoutX(530);  // Position the button next to the search box
        addDoctorButton.setLayoutY(10);   // Align vertically with the search box
        addDoctorButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
    
        // Set action for the "Add New Doctor" button
        addDoctorButton.setOnAction(event -> {
            openAddNewDoctorForm();
        });
    
        // Add the search box, table, and button to the main content area
        mainContentArea.getChildren().clear(); // Clear existing content
        mainContentArea.getChildren().addAll(searchBox, doctorTable, addDoctorButton);
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

    @SuppressWarnings("unused")
    private void openAddNewDoctorForm() {
        // Create a new stage for the add doctor form
        Stage addDoctorStage = new Stage();
        addDoctorStage.setTitle("Add New Doctor");

        // Create a VBox to hold the form elements
        VBox formVBox = new VBox(10); // Spacing between form elements
        formVBox.setPadding(new Insets(20));

        // Create labels and text fields for doctor details
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Doctor's name");
        
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Doctor's email");

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter Doctor's phone number");

        Label checkupLabel = new Label("Check-up Date:");
        DatePicker hireDatePicker = new DatePicker();
        hireDatePicker.setPromptText("Enter checkup date");

        // Create a button to submit the form
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Handle form submission
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            LocalDate hireDate = hireDatePicker.getValue();

            // Validate input data
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || hireDate == null) {
                showError("All fields must be filled out!");
            } else {
                // Create a new Doctor object with the provided data
                Doctor newDoctor = new Doctor();
                newDoctor.setName(name);
                newDoctor.setEmail(email);
                newDoctor.setPhoneNumber(phone);
                newDoctor.setHireDate(Date.valueOf(hireDate)); // Convert LocalDate to Date

                // Add the new doctot to the database (implement the logic for adding a patient to your database)
                boolean success = DatabaseConnection.addNewDoctor(newDoctor);
                if (success) {
                    showInfo("Doctor added successfully!");
                    addDoctorStage.close();
                } else {
                    showError("Error adding doctor. Please try again.");
                }
            }
        });

        // Create a button to cancel and close the form
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        cancelButton.setOnAction(e -> addDoctorStage.close());

        // Create a HBox for buttons (Submit & Cancel)
        HBox buttonHBox = new HBox(10, submitButton, cancelButton);
        buttonHBox.setAlignment(Pos.CENTER);

        // Add all elements to the VBox
        formVBox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, checkupLabel, hireDatePicker, buttonHBox);

        // Set the scene and show the stage
        Scene scene = new Scene(formVBox, 400, 350);
        addDoctorStage.setScene(scene);
        addDoctorStage.show();
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

    @FXML
    public void openSettings() {
        mainContentTitle.setText("Settings");
    }

    @SuppressWarnings({ "unused", "unchecked" })
    @FXML
private void viewComplaints() {
    mainContentTitle.setText("View Complaints");

    // Fetch unresolved complaints from the database
    ObservableList<Complaint> complaintList = DatabaseConnection.getUnresolvedComplaints();

    // Create a TableView for displaying complaints
    TableView<Complaint> complaintTable = new TableView<>();
    complaintTable.setItems(complaintList);

    // Define TableColumns
    TableColumn<Complaint, Integer> complaintIdColumn = new TableColumn<>("Complaint ID");
    complaintIdColumn.setCellValueFactory(new PropertyValueFactory<>("complaintID"));

    TableColumn<Complaint, Integer> patientIdColumn = new TableColumn<>("Patient ID");
    patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

    TableColumn<Complaint, String> complaintTextColumn = new TableColumn<>("Complaint");
    complaintTextColumn.setCellValueFactory(new PropertyValueFactory<>("complaintText"));

    TableColumn<Complaint, String> statusColumn = new TableColumn<>("Status");
    statusColumn.setCellValueFactory(cellData -> {
        return new SimpleStringProperty(cellData.getValue().getStatus());
    });

    // Define the "Resolve" action column
    TableColumn<Complaint, Void> resolveColumn = new TableColumn<>("Resolve");
    resolveColumn.setCellFactory(param -> new TableCell<Complaint, Void>() {
        private final Button resolveButton = new Button("Resolve");

        {
            resolveButton.setOnAction(event -> {
                Complaint selectedComplaint = getTableView().getItems().get(getIndex());
                boolean isResolved = DatabaseConnection.markComplaintAsResolved(selectedComplaint.getComplaintID());
                if (isResolved) {
                    selectedComplaint.setStatus("Resolved");
                    complaintTable.refresh();
                    viewComplaints();
                    showAlert(Alert.AlertType.INFORMATION, "Complaint Resolved", "The complaint has been marked as resolved.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to resolve the complaint.");
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(resolveButton);
            }
        }
    });

    // Add columns to the table
    complaintTable.getColumns().addAll(complaintIdColumn, patientIdColumn, complaintTextColumn, statusColumn, resolveColumn);

    // Adjust TableView layout
    complaintTable.setPrefWidth(mainContentArea.getPrefWidth());
    complaintTable.setPrefHeight(mainContentArea.getPrefHeight() - 50);
    complaintTable.setLayoutY(50);

    // Create a details view (initially hidden)
    VBox complaintDetailsBox = new VBox();
    complaintDetailsBox.setVisible(false); // Initially hidden
    complaintDetailsBox.setSpacing(10);
    complaintDetailsBox.setLayoutY(50);
    complaintDetailsBox.setPrefWidth(mainContentArea.getPrefWidth());

    Label detailsLabel = new Label("Complaint Details:");
    detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    TextArea detailsArea = new TextArea();
    detailsArea.setEditable(false);
    detailsArea.setWrapText(true);
    detailsArea.setPrefHeight(300);

    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #e1722f; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    backButton.setOnAction(event -> {
        complaintDetailsBox.setVisible(false);
        complaintTable.setVisible(true);
    });

    complaintDetailsBox.getChildren().addAll(detailsLabel, detailsArea, backButton);

    // Add listener for row selection
    complaintTable.setOnMouseClicked(event -> {
        if (event.getClickCount() == 1) { // Single-click
            Complaint selectedComplaint = complaintTable.getSelectionModel().getSelectedItem();
            if (selectedComplaint != null) {
                // Populate details
                detailsArea.setText("Complaint ID: " + selectedComplaint.getComplaintID() + "\n" +
                        "Patient ID: " + selectedComplaint.getPatientID() + "\n" +
                        "Complaint: " + selectedComplaint.getComplaintText() + "\n" +
                        "Status: " + selectedComplaint.getStatus());

                // Hide table and show details
                complaintTable.setVisible(false);
                complaintDetailsBox.setVisible(true);
            }
        }
    });

    // Clear the previous content and add both the TableView and details box
    mainContentArea.getChildren().clear();
    mainContentArea.getChildren().addAll(complaintTable, complaintDetailsBox);
}

public void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


}
