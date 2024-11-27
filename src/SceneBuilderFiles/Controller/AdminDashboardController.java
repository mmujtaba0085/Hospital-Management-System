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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import packages.Person.Receptionist;

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
    Label nameLabel = new Label("Name: " + admin.getName());
    Label emailLabel = new Label("Email: " + admin.getEmail());
    Label phoneLabel = new Label("Phone: " + admin.getPhoneNumber());
    Label addressLabel = new Label("Address: " + admin.getAddress());
    
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
    
    // Add to main content area
    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().forEach(child -> child.setVisible(false));
    mainContentPane.getChildren().addAll(profileBox);
    
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
    
    TextField nameField = new TextField(admin.getName());
    TextField phoneField = new TextField(admin.getPhoneNumber());
    TextField addressField = new TextField(admin.getAddress());
    
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
            admin.setName(nameField.getText());
            admin.setPhoneNumber(phoneField.getText());
            admin.setAddress(addressField.getText());
            
            // Update in database
            boolean success = DatabaseConnection.updateAdminProfile(
                admin.getID(),
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
                admin.getEmail(),
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
    
        
        // Define TableColumn for Remove Action
        TableColumn<Patient, Void> removeColumn = new TableColumn<>("Remove");
        removeColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");
    
            {
                removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                removeButton.setOnAction(event -> {
                    Patient selectedPatient = getTableView().getItems().get(getIndex());
                    boolean success = DatabaseConnection.removePatientByID(selectedPatient.getID());
                    if (success) {
                        showInfo("Patient removed successfully!");
                        patientObservableList.remove(selectedPatient);
                    } else {
                        showError("Error removing patient. Please try again.");
                    }
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
    
        // Add all columns to the TableView
        patientTable.getColumns().addAll(idColumn, nameColumn, emailColumn, numberColumn, removeColumn);
    
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
    
        // Add the search box, table, and button to the main content pane
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().addAll(searchBox, addNewPatientButton, patientTable);
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
    
        // Create a button to submit the form
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    
        // Handle form submission
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
    
            // Validate input data
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showError("All fields must be filled out!");
            } else {
                // Create a new Patient object with the provided data
                Patient newPatient = new Patient();
                newPatient.setName(name);
                newPatient.setEmail(email);
                newPatient.setPhoneNumber(phone);
    
                // Add the new patient to the database (implement the logic for adding a patient to your database)
                boolean success = DatabaseConnection.addNewPatient(newPatient);
                if (success) {
                    showInfo("Patient added successfully!");
                    addPatientStage.close();
                    viewPatientList();
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
        formVBox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, buttonHBox);
    
        // Set the scene and show the stage
        Scene scene = new Scene(formVBox, 400, 300); // Adjusted height to fit the new form
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
    
        // Define TableColumn for Remove Action
        TableColumn<Doctor, Void> removeColumn = new TableColumn<>("Remove");
        removeColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");
    
            {
                removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                removeButton.setOnAction(event -> {
                    Doctor selectedDoctor = getTableView().getItems().get(getIndex());
                    boolean success = DatabaseConnection.removeDoctorByID(selectedDoctor.getID());
                    if (success) {
                        showInfo("Doctor removed successfully!");
                        doctorObservableList.remove(selectedDoctor);
                    } else {
                        showError("Error removing doctor. Please try again.");
                    }
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
    
        // Add all columns to the TableView
        doctorTable.getColumns().addAll(idColumn, nameColumn, specialtyColumn, contactColumn, removeColumn);
    
        // Adjust TableView layout
        doctorTable.setPrefWidth(mainContentArea.getPrefWidth());
        doctorTable.setPrefHeight(mainContentArea.getPrefHeight() - 50); // Leave space for the search box
        doctorTable.setLayoutY(50); // Position the TableView below the search box
    
        // Create an "Add New Doctor" button
        Button addDoctorButton = new Button("Add New Doctor");
        addDoctorButton.setLayoutX(530);  // Position the button next to the search box
        addDoctorButton.setLayoutY(10);   // Align vertically with the search box
        addDoctorButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");
    
        // Set action for the "Add New Doctor" button
        addDoctorButton.setOnAction(event -> {
            openAddNewDoctorForm();
        });
    
        // Add the search box, table, and button to the main content pane
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().addAll(searchBox, doctorTable, addDoctorButton);
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

    Label checkupLabel = new Label("Hire Date:");
    DatePicker hireDatePicker = new DatePicker();
    hireDatePicker.setPromptText("Enter hire date");

    // Label and ComboBox for specialization selection
    Label specializationLabel = new Label("Specialization:");
    TextField specializationField = new TextField();
    specializationField.setPromptText("Select Doctor's Specialization");

    // Create a button to submit the form
    Button submitButton = new Button("Submit");
    submitButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

    // Handle form submission
    submitButton.setOnAction(e -> {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        LocalDate hireDate = hireDatePicker.getValue();
        String specialization = specializationField.getText();

        // Validate input data
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || hireDate == null || specialization == null) {
            showError("All fields must be filled out, including specialization!");
        } else {
            // Create a new Doctor object with the provided data
            Doctor newDoctor = new Doctor();
            newDoctor.setName(name);
            newDoctor.setEmail(email);
            newDoctor.setPhoneNumber(phone);
            newDoctor.setHireDate(Date.valueOf(hireDate)); // Convert LocalDate to Date
            newDoctor.setSpecialization(specialization);  // Set specialization

            // Add the new doctor to the database
            boolean success = DatabaseConnection.addNewDoctor(newDoctor);
            if (success) {
                showInfo("Doctor added successfully!");
                addDoctorStage.close();
                viewDoctorList();
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
    formVBox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, 
                                  checkupLabel, hireDatePicker, specializationLabel, specializationField, buttonHBox);

    // Set the scene and show the stage
    Scene scene = new Scene(formVBox, 400, 400); // Adjust the scene size to fit the new form elements
    addDoctorStage.setScene(scene);
    addDoctorStage.show();
}



@SuppressWarnings({ "unchecked", "unused" })
@FXML
public void viewReceptionistList() {
    mainContentTitle.setText("Receptionist List");

    // Retrieve all receptionists from the database
    LinkedList<Receptionist> receptionistList = DatabaseConnection.getAllReceptionist();

    // Convert the LinkedList to an ObservableList for the TableView
    ObservableList<Receptionist> receptionistObservableList = FXCollections.observableArrayList(receptionistList);

    // Create a FilteredList for search functionality
    FilteredList<Receptionist> filteredList = new FilteredList<>(receptionistObservableList, p -> true);

    // Create a TextField for search input
    TextField searchBox = new TextField();
    searchBox.setPromptText("Search by Name...");
    searchBox.setLayoutX(10); // Position the search box
    searchBox.setLayoutY(10);
    searchBox.setPrefWidth(300);

    // Add a listener to filter the list based on search input
    searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredList.setPredicate(receptionist -> {
            // If the search box is empty, show all receptionists
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Compare receptionist name with the search input
            String lowerCaseFilter = newValue.toLowerCase();
            return receptionist.getName().toLowerCase().contains(lowerCaseFilter);
        });
    });

    // Create a TableView for displaying receptionists
    TableView<Receptionist> receptionistTable = new TableView<>();
    receptionistTable.setItems(filteredList);

    // Define TableColumn for Receptionist ID
    TableColumn<Receptionist, Integer> idColumn = new TableColumn<>("Receptionist ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

    // Define TableColumn for Name
    TableColumn<Receptionist, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    // Define TableColumn for Email Address
    TableColumn<Receptionist, String> contactColumn = new TableColumn<>("Email Address");
    contactColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    // Define TableColumn for Remove Action
    TableColumn<Receptionist, Void> removeColumn = new TableColumn<>("Remove");
    removeColumn.setCellFactory(param -> new TableCell<>() {
        private final Button removeButton = new Button("Remove");

        {
            removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            removeButton.setOnAction(event -> {
                Receptionist selectedReceptionist = getTableView().getItems().get(getIndex());
                boolean success = DatabaseConnection.removeReceptionist(selectedReceptionist.getID());
                if (success) {
                    showInfo("Receptionist removed successfully!");
                    receptionistObservableList.remove(selectedReceptionist);
                } else {
                    showError("Error removing receptionist. Please try again.");
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(removeButton);
            }
        }
    });

    // Add all columns to the TableView
    receptionistTable.getColumns().addAll(idColumn, nameColumn, contactColumn, removeColumn);

    // Adjust TableView layout
    receptionistTable.setPrefWidth(mainContentArea.getPrefWidth());
    receptionistTable.setPrefHeight(mainContentArea.getPrefHeight() - 50); // Leave space for the search box
    receptionistTable.setLayoutY(50); // Position the TableView below the search box

    // Create an "Add New Receptionist" button
    Button addReceptionistButton = new Button("Add New Receptionist");
    addReceptionistButton.setLayoutX(530);  // Position the button next to the search box
    addReceptionistButton.setLayoutY(10);   // Align vertically with the search box
    addReceptionistButton.setStyle("-fx-background-color: #e1722f; -fx-text-fill: white;");

    // Set action for the "Add New Receptionist" button
    addReceptionistButton.setOnAction(event -> {
        openAddNewReceptionistForm();
    });

    // Add the search box, table, and button to the main content pane
    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().forEach(child -> child.setVisible(false));
    mainContentPane.getChildren().addAll(searchBox, receptionistTable, addReceptionistButton);
}


@SuppressWarnings("unused")
private void displayReceptionistDetails(Receptionist doctor) {
    // Create a new AnchorPane for displaying doctor details
    AnchorPane detailsPane = new AnchorPane();

    // Create labels for doctor's details
    Label idLabel = new Label("Receptionist ID: " + doctor.getID());
    idLabel.setLayoutX(20);
    idLabel.setLayoutY(20);
    idLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    Label nameLabel = new Label("Name: " + doctor.getName());
    nameLabel.setLayoutX(20);
    nameLabel.setLayoutY(60);
    nameLabel.setStyle("-fx-font-size: 16px;");


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
    backButton.setOnAction(event -> viewReceptionistList());

    // Add all components to the detailsPane
    detailsPane.getChildren().addAll(idLabel, nameLabel, emailLabel, backButton);

    // Clear the mainContentArea and display the detailsPane
    mainContentArea.getChildren().clear();
    mainContentArea.getChildren().add(detailsPane);
}

@SuppressWarnings("unused")
private void openAddNewReceptionistForm() {
// Create a new stage for the add doctor form
Stage addDoctorStage = new Stage();
addDoctorStage.setTitle("Add New Receptionist");

// Create a VBox to hold the form elements
VBox formVBox = new VBox(10); // Spacing between form elements
formVBox.setPadding(new Insets(20));

// Create labels and text fields for doctor details
Label nameLabel = new Label("Name:");
TextField nameField = new TextField();
nameField.setPromptText("Enter Receptionist's name");

Label emailLabel = new Label("Email:");
TextField emailField = new TextField();
emailField.setPromptText("Enter Receptionist's email");

Label phoneLabel = new Label("Phone Number:");
TextField phoneField = new TextField();
phoneField.setPromptText("Enter Receptionist's phone number");

Label checkupLabel = new Label("Hire Date:");
DatePicker hireDatePicker = new DatePicker();
hireDatePicker.setPromptText("Enter hire date");


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
        showError("All fields must be filled out, including specialization!");
    } else {
        // Create a new Doctor object with the provided data
        Receptionist newRecep = new Receptionist();
        newRecep.setName(name);
        newRecep.setEmail(email);
        newRecep.setPhoneNumber(phone);
        newRecep.setHireDate(Date.valueOf(hireDate)); // Convert LocalDate to Date

        // Add the new doctor to the database
        boolean success = DatabaseConnection.addNewReceptionist(newRecep);
        if (success) {
            showInfo("Receptionist added successfully!");
            addDoctorStage.close();
            viewReceptionistList();
        } else {
            showError("Error adding Receptionist. Please try again.");
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
formVBox.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, 
                              checkupLabel, hireDatePicker, buttonHBox);

// Set the scene and show the stage
Scene scene = new Scene(formVBox, 400, 400); // Adjust the scene size to fit the new form elements
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
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().add(appointmentTable);
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
        Pane mainContentPane = (Pane) mainContentTitle.getParent();
        mainContentPane.getChildren().forEach(child -> child.setVisible(false));
        mainContentPane.getChildren().add(billTable);
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

    TableColumn<Complaint, String> patientIdColumn = new TableColumn<>("User");
    patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

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
                        "User: " + selectedComplaint.getUserType() + "\n" +
                        "Complaint: " + selectedComplaint.getComplaintText() + "\n" +
                        "Status: " + selectedComplaint.getStatus());

                // Hide table and show details
                complaintTable.setVisible(false);
                complaintDetailsBox.setVisible(true);
            }
        }
    });

    // Clear the previous content and add both the TableView and details box
    Pane mainContentPane = (Pane) mainContentTitle.getParent();
    mainContentPane.getChildren().forEach(child -> child.setVisible(false));
    mainContentPane.getChildren().addAll(complaintTable, complaintDetailsBox);
}

public void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


}
