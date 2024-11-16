package SceneBuilderFiles.Controller;

import packages.Person.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DoctorDashboardController {
    private Doctor doctor;

    public DoctorDashboardController(){
        doctor=null;
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


    // Method to receive Doctor object
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;

        // Update UI elements with Doctor details
        mainContentTitle.setText("Welcome, " + this.doctor.getName());
        System.out.println("Doctor's specialization: " + doctor.getSpecialization());
    }
    @FXML
    public void initialize() {
        // Initialize the dashboard
        System.out.println("Doctor's Dashboard Initialized!");
        mainContentTitle.setText("Welcome to the Doctor's Dashboard");
        subOptionPane.setVisible(false); // Hide sub-option pane initially
    }

    // Sidebar button handlers
    @FXML
    public void showOverview() {
        System.out.println("Showing Overview...");
        mainContentTitle.setText("Overview");
    }

    @FXML
    public void viewSchedule() {
        System.out.println("Viewing Schedule...");
        mainContentTitle.setText("Appointments Schedule");
    }

    @FXML
    public void rescheduleAppointments() {
        System.out.println("Rescheduling Appointments...");
        mainContentTitle.setText("Reschedule Appointments");
    }

    @FXML
    public void cancelAppointments() {
        System.out.println("Cancelling Appointments...");
        mainContentTitle.setText("Cancel Appointments");
    }

    @FXML
    public void viewPatientDetails() {
        System.out.println("Viewing Patient Details...");
        mainContentTitle.setText("Patient Details");
    }

    @FXML
    public void updateHealthRecords() {
        System.out.println("Updating Patient Health Records...");
        mainContentTitle.setText("Update Health Records");
    }

    @FXML
    public void viewMedicalHistory() {
        System.out.println("Viewing Medical History...");
        mainContentTitle.setText("Medical History");
    }

    @FXML
    public void createConsultationNotes() {
        System.out.println("Creating Consultation Notes...");
        mainContentTitle.setText("Create Consultation Notes");
    }

    @FXML
    public void viewPreviousNotes() {
        System.out.println("Viewing Previous Consultation Notes...");
        mainContentTitle.setText("Previous Consultation Notes");
    }

    @FXML
    public void openPrescriptions() {
        System.out.println("Opening Prescriptions...");
        mainContentTitle.setText("Prescriptions");
    }

    @FXML
    public void openCommunication() {
        System.out.println("Opening Communication...");
        mainContentTitle.setText("Communication");
    }

    @FXML
    public void openBilling() {
        System.out.println("Opening Billing and Payments...");
        mainContentTitle.setText("Billing and Payments");
    }

    @FXML
    public void generateReports() {
        System.out.println("Generating Reports...");
        mainContentTitle.setText("Generate Reports");
    }

    @FXML
    public void downloadReports() {
        System.out.println("Downloading Reports...");
        mainContentTitle.setText("Download Reports");
    }

    @FXML
    public void openSettings() {
        System.out.println("Opening Settings...");
        mainContentTitle.setText("Settings");
    }

    @FXML
    public void openHelp() {
        System.out.println("Opening Help and Support...");
        mainContentTitle.setText("Help and Support");
    }

    

    // Additional helper methods if needed
    public void showSubOptions(String title) {
        subOptionTitle.setText(title);
        subOptionPane.setVisible(true);
    }
}