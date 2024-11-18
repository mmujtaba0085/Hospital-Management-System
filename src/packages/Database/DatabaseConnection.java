package packages.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import packages.Person.*;
import packages.Others.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/HMS";
    private static final String USER = "root"; 
    private static final String PASSWORD = "icu321@"; 

    // Method to authenticate and return role code
    public static int authenticateUser(String username, String password) {
        String query = "SELECT role FROM login WHERE username = ? AND password = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            statement.setString(1, username);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                switch (role.toLowerCase()) {
                    case "admin":
                        return 1;
                    case "doctor":
                        return 2;
                    case "receptionist":
                        return 3;
                    case "patient":
                        return 4;
                    default:
                        return 0; // Unrecognized role
                }
            } else {
                return 0; // No match found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }   
    }

    // Checking if the user is a doctor and is registered in the system
    public static Doctor DoctorDetail(String email,String password){
        Doctor doctor=null;
        if(authenticateUser(email, password)!=2){
            return null;
        }
        
        String query="select doctorId,specialization,name,phoneNumber,hireDate from doctor where email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                int doctorId = resultSet.getInt("doctorId");
                String specialization = resultSet.getString("specialization");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                Date hireDate = resultSet.getDate("hireDate");
    
                // Create and populate the Doctor object
                doctor=new Doctor(doctorId, name, specialization, email, phoneNumber, hireDate);
            }
            return doctor;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }   


    }

    // checking if the user is a patient and is registered in the system
    public static Patient PatientDetail(String email,String password){
        Patient patient=null;
        if(authenticateUser(email, password)!=4){
            return null;
        }
        
        String query="select patientId,name,phoneNumber,checkupDate from patient where email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                int patientId = resultSet.getInt("patientId");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                Date checkupDate = resultSet.getDate("checkupDate");
    
                // Create and populate the Patient object
                patient=new Patient(patientId, name, email, phoneNumber, checkupDate);
            }
            return patient;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }   


    }

    // checking if admin is the user and registered in the system
    public static Admin AdminDetail(String email,String password){
        Admin admin=null;
        if(authenticateUser(email, password)!=1){
            return null;
        }
        
        String query="select adminId,name,phoneNumber,hireDate from admin where email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                int adminId = resultSet.getInt("adminId");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                Date hireDate = resultSet.getDate("hireDate");
    
                // Create and populate the Admin object
                admin=new Admin(adminId, name, email, phoneNumber, hireDate);
            }
            return admin;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }   


    }

    public static List<Appointment> ViewAppointments(String email) {
        String query = """
            SELECT a.appointmentID, p.name AS patient_name, d.name AS doctor_name, a.time_of_appointment 
            FROM Appointments a 
            LEFT JOIN Patient p ON a.patient_id = p.patientID 
            LEFT JOIN Doctor d ON a.doctor_id = d.doctorID 
            WHERE p.email = ? OR d.email = ?
            """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set email for both parameters
            statement.setString(1, email);
            statement.setString(2, email);

            ResultSet resultSet = statement.executeQuery();

            // Loop through the result set and add appointments to the list
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("appointmentID");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                Timestamp timeOfAppointment = resultSet.getTimestamp("time_of_appointment");

                // Create an Appointment object and add it to the list
                appointments.add(new Appointment(appointmentID, patientName, doctorName, timeOfAppointment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    

    public static boolean cancelAppointment(int doctorId, String patientName) {
        String query = """
            DELETE FROM Appointments 
            WHERE doctor_id = ? AND patient_id = 
            (SELECT patientID FROM Patient WHERE name = ?)
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the doctorId and patientName parameters
            statement.setInt(1, doctorId);
            statement.setString(2, patientName);

            // Execute the query and return true if one or more rows were affected
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<MedicalHistory> getMedicalReports(List<Integer> patientIds) {
        List<MedicalHistory> medicalReports = new ArrayList<>();
        if (patientIds.isEmpty()) return medicalReports;
    
        // Construct placeholders for IN clause
        String placeholders = String.join(",", Collections.nCopies(patientIds.size(), "?"));
        String query = "SELECT * FROM MedicalHistory WHERE patientId IN (" + placeholders + ")";
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
    
            // Set parameters for the query
            for (int i = 0; i < patientIds.size(); i++) {
                stmt.setInt(i + 1, patientIds.get(i));
            }
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MedicalHistory history = new MedicalHistory();
                history.setPatientId(rs.getInt("patientId"));
                history.setAllergies(rs.getString("allergies"));
                history.setMedications(rs.getString("medications"));
                history.setPastIllnesses(rs.getString("pastIllnesses"));
                history.setSurgeries(rs.getString("surgeries"));
                history.setFamilyHistory(rs.getString("familyHistory"));
                history.setNotes(rs.getString("notes"));
                history.setLastUpdated(rs.getTimestamp("lastUpdated"));
    
                medicalReports.add(history);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medical reports: " + e.getMessage());
        }
    
        return medicalReports;
    }
    

    public static List<Integer> getPatientIdsByName(List<String> patientNames) {
        List<Integer> patientIds = new ArrayList<>();
        if (patientNames.isEmpty()) return patientIds;

        String placeholders = String.join(",", Collections.nCopies(patientNames.size(), "?"));
        String query = "SELECT patientID FROM Patient WHERE name IN (" + placeholders + ")";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set parameters for the query
            for (int i = 0; i < patientNames.size(); i++) {
                stmt.setString(i + 1, patientNames.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                patientIds.add(rs.getInt("patientID"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient IDs: " + e.getMessage());
        }
        

        return patientIds;
    }



}
