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
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


public static boolean cancelAppointment(int Id, String Name) {      //id is of the who is canceling and name is whom it is being cancelled with
    String query = """
    DELETE FROM Appointments 
    WHERE (doctor_id = ? AND patient_id = 
           (SELECT patientID FROM Patient WHERE name = ?))
       OR (patient_id = ? AND doctor_id = 
           (SELECT doctorID FROM Doctor WHERE name = ?))
    """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the doctorId and patientName parameters
            statement.setInt(1, Id);
            statement.setString(2, Name);
            statement.setInt(3, Id);
            statement.setString(4, Name);

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
                history.setPatientName(rs.getString("patientName"));
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

    public static Admin getAdminPersonalDeatils(int adminId) {
        Admin admin = null;
        String query = "SELECT * FROM Admin WHERE adminID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the doctorId and patientName parameters
            statement.setInt(1, adminId);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                Date hireDate = resultSet.getDate("hireDate");
    
                // Create and populate the Admin object
                admin=new Admin(adminId, name, email, phoneNumber, hireDate);
            }
            return admin;

        } catch (SQLException e) {
            e.printStackTrace();
            return admin;
        }
    }


    public static List<MedicalHistory> searchMedicalHistory(String query) {
        List<MedicalHistory> results = new ArrayList<>();
        String sql = """
            SELECT * 
            FROM MedicalHistory 
            WHERE patientId LIKE ? 
               OR patientName REGEXP ?
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            // Query for patientId
            stmt.setString(1, "%" + query + "%");
    
            // Query for patientName (REGEXP allows space-separated word matching)
            String regexPattern = ".*\\b" + query + ".*";
            stmt.setString(2, regexPattern);
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MedicalHistory record = new MedicalHistory();
                record.setPatientId(rs.getInt("patientId"));
                record.setPatientName(rs.getString("patientName"));
                record.setAllergies(rs.getString("allergies"));
                record.setMedications(rs.getString("medications"));
                record.setPastIllnesses(rs.getString("pastIllnesses"));
                record.setSurgeries(rs.getString("surgeries"));
                record.setFamilyHistory(rs.getString("familyHistory"));
                record.setNotes(rs.getString("notes"));
                record.setLastUpdated(rs.getTimestamp("lastUpdated"));
                results.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static LinkedList<Doctor> getAllDoctors(){
        LinkedList<Doctor> doctorList = new LinkedList<>();
        String sql = """
            SELECT * 
            FROM Doctor
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorDetails(rs.getInt("doctorID"), rs.getString("doctorName"), rs.getString("email"), rs.getString("phoneNumber"), rs.getString("specialization"));
                d.setHireDate(rs.getDate("hireDate"));
                doctorList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctorList;
    }

    public static LinkedList<Patient> getAllPatients(){
        LinkedList<Patient> patientList = new LinkedList<>();
        String sql = """
            SELECT * 
            FROM Patient 
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setID(rs.getInt("patientID"));
                p.setName(rs.getString("patientName"));
                p.setEmail("email");
                patientList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientList;
    }
    
    public static void saveDoctorSchedule(int doctorID, String dayOfWeek, String startTime, String endTime) {
        String deleteQuery = "DELETE FROM DoctorSchedule WHERE doctorID = ? AND dayOfWeek = ?";
        String insertQuery = "INSERT INTO DoctorSchedule (doctorID, dayOfWeek, startTime, endTime) VALUES (?, ?, ?, ?)";
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
    
            // Delete existing schedule for the same day
            deleteStatement.setInt(1, doctorID);
            deleteStatement.setString(2, dayOfWeek);
            deleteStatement.executeUpdate();
    
            // Insert the new schedule
            insertStatement.setInt(1, doctorID);
            insertStatement.setString(2, dayOfWeek);
            insertStatement.setString(3, startTime);
            insertStatement.setString(4, endTime);
            insertStatement.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

// Load schedule from database
public static ObservableList<Schedule> viewDoctorSchedule(Doctor doctor) {
    String query = "SELECT dayOfWeek, startTime, endTime FROM DoctorSchedule WHERE doctorID = ?";
    ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(query)) {
        
        // Set the doctor ID
        statement.setInt(1, doctor.getID());
        
        ResultSet resultSet = statement.executeQuery();
        
        // Process the result set and update the schedule list
        while (resultSet.next()) {
            String dayOfWeek = resultSet.getString("dayOfWeek");
            String startTime = resultSet.getString("startTime");
            String endTime = resultSet.getString("endTime");

            scheduleList.add(new Schedule(dayOfWeek, startTime, endTime));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return scheduleList;
}

    public static List<String> distinctSpecialization(){
        String query = "SELECT DISTINCT specialization FROM Doctor ORDER BY specialization";
        List<String> specialization=null;

        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
        PreparedStatement statement=connection.prepareStatement(query);)
        {
            ResultSet result =statement.executeQuery();
            specialization= new ArrayList<>();
            while(result.next()){
                specialization.add(result.getString("specialization"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return specialization;

    }
}
