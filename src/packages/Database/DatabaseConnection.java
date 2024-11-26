package packages.Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Date;
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

    public static List<Appointment> viewAppointments(String email) {
        
        String query = """
            SELECT a.appointmentID, a.appointedDay, 
                   p.name AS patient_name, d.name AS doctor_name
            FROM Appointments a
            LEFT JOIN Patient p ON a.patientID = p.patientID
            LEFT JOIN Doctor d ON a.doctorID = d.doctorID
            WHERE p.email = ? OR d.email = ?
        """;
    
        List<Appointment> appointments = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Bind email for both patient and doctor
            statement.setString(1, email);
            statement.setString(2, email);
    
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("appointmentID");
                String appointedDay = resultSet.getString("appointedDay");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
    
               
                appointments.add(new Appointment(appointmentID, patientName, doctorName, appointedDay));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return appointments;
    }
    
    public static boolean cancelAppointment(int Id, String Name) {
        String fetchAppointmentQuery = """
        SELECT doctorID, appointedDay 
        FROM Appointments 
        WHERE (doctorID = ? AND patientID = 
               (SELECT patientID FROM Patient WHERE name = ?))
           OR (patientID = ? AND doctorID = 
               (SELECT doctorID FROM Doctor WHERE name = ?))
        """;
    
        String deleteQuery = """
        DELETE FROM Appointments 
        WHERE (doctorID = ? AND patientID = 
               (SELECT patientID FROM Patient WHERE name = ?))
           OR (patientID = ? AND doctorID = 
               (SELECT doctorID FROM Doctor WHERE name = ?))
        """;
    
        String updateScheduleQuery = """
        UPDATE DoctorSchedule
        SET totalBooked = totalBooked - 1
        WHERE doctorID = ? AND dayOfWeek = ?
        """;
    

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement fetchStmt = connection.prepareStatement(fetchAppointmentQuery);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateScheduleQuery)) {
    
            // Fetch doctorID and appointedDay
            fetchStmt.setInt(1, Id);
            fetchStmt.setString(2, Name);
            fetchStmt.setInt(3, Id);
            fetchStmt.setString(4, Name);
    
            ResultSet resultSet = fetchStmt.executeQuery();
    
            if (resultSet.next()) {
                int doctorID = resultSet.getInt("doctorID");
                String appointedDay = resultSet.getString("appointedDay");
    
                // Delete the appointment
                deleteStmt.setInt(1, Id);
                deleteStmt.setString(2, Name);
                deleteStmt.setInt(3, Id);
                deleteStmt.setString(4, Name);
    
                int rowsAffected = deleteStmt.executeUpdate();
    
                if (rowsAffected > 0) {
                    // Update the DoctorSchedule to decrease totalBooked
                    updateStmt.setInt(1, doctorID);
                    updateStmt.setString(2, appointedDay);
                    updateStmt.executeUpdate();
                    return true; // Successfully canceled and updated totalBooked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Cancellation failed
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
    
    public static int getDoctorIDByName(String doctorName){
        int doctorID = 0;
        String query = "SELECT doctorID FROM Doctor WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, doctorName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                doctorID = rs.getInt("doctorID");
                return doctorID;
            }
            else{
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient IDs: " + e.getMessage());
            return 0;
        }
    }

    public static Admin getAdminPersonalDeatils(int adminId) {
        Admin admin = null;
        String query = "SELECT email, name, phoneNumber, hireDate FROM Admin WHERE adminID = ?";
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

    public static Patient getPatientPersonalDetails(int patientId) {
        Patient patient = null;
        String query = """
        SELECT email, name, phoneNumber, checkupDate
        FROM Patient
        WHERE patientID = ?;
        """;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the doctorId and patientName parameters
            statement.setInt(1, patientId);

            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phoneNumber");
                Date checkupDate = resultSet.getDate("checkupDate");
    
                // Create and populate the Patient object
                patient=new Patient(patientId, name, email, phoneNumber, checkupDate);
            }
            return patient;

        } catch (SQLException e) {
            e.printStackTrace();
            return patient;
        }
    }

    public static boolean addNewPatient(Patient patient) {
        String sql = "INSERT INTO Patient (name, email, phoneNumber, checkupDate) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhoneNumber());
            stmt.setDate(4, patient.getCheckupDate());
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addNewDoctor(Doctor doctor) {
        String sql = "INSERT INTO Doctor (name, email, phoneNumber, hireDate) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getEmail());
            stmt.setString(3, doctor.getPhoneNumber());
            stmt.setDate(4, doctor.getHireDate());
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
            SELECT doctorID, name, email, phoneNumber, specialization, hireDate
            FROM Doctor;
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setID(rs.getInt("doctorID"));
                d.setName(rs.getString("name"));
                d.setEmail(rs.getString("email"));
                d.setPhoneNumber(rs.getString("phoneNumber"));
                d.setSpecialization(rs.getString("specialization"));
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
            SELECT patientID, name, email, phoneNumber, checkupDate
            FROM Patient;
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setID(rs.getInt("patientID"));
                p.setName(rs.getString("name"));
                p.setEmail(rs.getString("email"));
                p.setPhoneNumber(rs.getString("phoneNumber"));
                p.setCheckupDate(rs.getDate("checkupDate"));
                patientList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientList;
    }

    public static LinkedList<Appointment> getAllAppointments(){
        LinkedList<Appointment> appointmentList = new LinkedList<>();
        String sql = """
            SELECT * 
            FROM appointments 
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setID(rs.getInt("appointmentID"));
                appointmentList.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    public static LinkedList<Bill> getAllBills(){
        LinkedList<Bill> billList = new LinkedList<>();
        String sql = """
            SELECT b.billID, p.name, b.amount, b.paid
            FROM Bills b
            JOIN Patient p ON b.patientID = p.patientID;
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                b.setID(rs.getInt("billID"));
                b.setAmount(rs.getDouble("amount"));
                b.setPaid(rs.getBoolean("paid"));
                b.setPatientName(rs.getString("name"));
                billList.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billList;
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
    
    public static boolean bookAppointmentWithDoctor(int patientId, String specialization, int startTime, int endTime, LocalDate date, int doctorID) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO Appointments (patientID, specialization, start_time, end_time, date, doctorID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setString(2, specialization);
            stmt.setInt(3, startTime);
            stmt.setInt(4, endTime);
            stmt.setDate(5, java.sql.Date.valueOf(date));
            stmt.setInt(6, doctorID);
    
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getDoctorsBySpecialization(String specialization) {
        List<String> doctors = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT name FROM Doctor WHERE specialization = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, specialization);
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    

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

    public static ObservableList<Bill> getSpecificPatientBill(Patient patient) {
        ObservableList<Bill> billList = FXCollections.observableArrayList();
        
        // Corrected SQL query
        String query = """

            SELECT b.billID, p.name, b.amount, b.paid, b.remainingAmount
            FROM Bills b
            JOIN Patient p ON b.patientID = p.patientID
            WHERE b.patientID = ?;
        """;
    
        // Database connection and data fetching
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the patient ID parameter
            stmt.setInt(1, patient.getID());
            
            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // Process each row in the result set
                while (rs.next()) {
                    Bill b=new Bill();
                    b.setID(rs.getInt("billID"));
                    b.setPatientName(rs.getString("name"));
                    b.setAmount(rs.getDouble("amount"));
                    b.setRemainingAmount(rs.getDouble("RemainingAmount"));
                    b.setPaid(rs.getBoolean("paid"));
                    // Create a Bill object and add it to the LinkedList
                    billList.add(b);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }
        
        return billList;
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
    
    public static ObservableList<Schedule> SpecializedAppoint(String specialization) {
        String query = """
            SELECT d.doctorID AS DoctorID, 
                   d.name AS DoctorName, 
                   GROUP_CONCAT(ds.dayOfWeek SEPARATOR ', ') AS DaysAvailable
            FROM 
                Doctor d
            JOIN 
                DoctorSchedule ds ON d.doctorID = ds.doctorID
            WHERE 
                d.specialization = ?
            GROUP BY 
                d.doctorID, d.name
            ORDER BY 
                d.name
        """;
    
        ObservableList<Schedule> doctorAvailable = FXCollections.observableArrayList();
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialization); // Bind specialization parameter
            ResultSet result = statement.executeQuery();
    
            while (result.next()) {
                String doctorName = result.getString("DoctorName");
                String daysAvailable = result.getString("DaysAvailable");
                int docID=result.getInt("DoctorID");
                doctorAvailable.add(new Schedule(docID,doctorName, daysAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return doctorAvailable;
    }
    
    public static boolean checkAndBookAppointment(int doctorID, String appointedDay, int patientID, Timestamp appointmentTime, String specialization) {
        String selectQuery = """
            SELECT totalBooked, TIME_TO_SEC(TIMEDIFF(endTime, startTime)) / 30 AS totalSlots, startTime, endTime
            FROM DoctorSchedule
            WHERE doctorID = ? AND dayOfWeek = ?
        """;
    
        String updateQuery = """
            UPDATE DoctorSchedule
            SET totalBooked = totalBooked + 1
            WHERE doctorID = ? AND dayOfWeek = ?;
        """;
    
        String insertQuery = """
            INSERT INTO Appointments (patientID, doctorID, specialization, start_time, end_time, date, appointedDay)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
    
            // Fetch the schedule details for the doctor and day
            selectStmt.setInt(1, doctorID);
            selectStmt.setString(2, appointedDay);
    
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                int totalBooked = resultSet.getInt("totalBooked");
                int totalSlots = resultSet.getInt("totalSlots");
                String startTime = resultSet.getString("startTime"); // Fetch as string
                String endTime = resultSet.getString("endTime");     // Fetch as string
    
                if (totalBooked < totalSlots) {
                    // Update DoctorSchedule to increment totalBooked
                    updateStmt.setInt(1, doctorID);
                    updateStmt.setString(2, appointedDay);
                    updateStmt.executeUpdate();
    
                    // Insert the appointment
                    insertStmt.setInt(1, patientID);
                    insertStmt.setInt(2, doctorID);
                    insertStmt.setString(3, specialization);
                    insertStmt.setString(4, startTime); // Use fetched start time
                    insertStmt.setString(5, endTime);   // Use fetched end time
                    insertStmt.setDate(6, new java.sql.Date(appointmentTime.getTime())); // Use the appointment date
                    insertStmt.setString(7, appointedDay);
                    insertStmt.executeUpdate();
    
                    return true; // Successfully booked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Booking failed
    }
     
    public static boolean isSlotAvailable(int doctorID, String dayOfWeek) {
        String query = """
            SELECT totalBooked, TIME_TO_SEC(TIMEDIFF(endTime, startTime)) / 3600 AS timeDiff
            FROM DoctorSchedule
            WHERE doctorID = ? AND dayOfWeek = ?
        """;
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, doctorID);
            statement.setString(2, dayOfWeek);
    
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                int totalBooked = resultSet.getInt("totalBooked");
                int maxSlots = resultSet.getInt("timeDiff") * 2; // Calculate slots based on hours
                return totalBooked < maxSlots; // Return true if slots are available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Default to no slots available if an error occurs
    }
    
    public static List<String> getDoctorAvailableDays(int doctorID) {
        String query = "SELECT dayOfWeek FROM DoctorSchedule WHERE doctorID = ?";
        List<String> daysAvailable = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, doctorID);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                daysAvailable.add(resultSet.getString("dayOfWeek"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return daysAvailable;
    }

    public static boolean hasExistingAppointment(int patientID, int doctorID) {
        String query = """
            SELECT COUNT(*) AS appointmentCount
            FROM Appointments
            WHERE patientID = ? AND doctorID = ?
        """;
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, patientID);
            statement.setInt(2, doctorID);
    
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("appointmentCount");
                return count > 0; // Return true if there is an existing appointment
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Default to no existing appointment
    }
    
    public static String getDoctorSpecializationById(int doctorID) {
        String query = "SELECT specialization FROM Doctor WHERE doctorID = ?";
        String specialization = null;
    
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, doctorID);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                specialization = resultSet.getString("specialization");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return specialization;
    }

    public static boolean addPatient(String name, String email, String phone, String password, Date date) {
        String sql = "INSERT INTO Patient (name, email, phoneNumber, checkupDate) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setDate(4, date);
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addDoctor(String name, String email, String phone, String password, Date date) {
        String sql = "INSERT INTO Doctor (name, email, phoneNumber, hireDate) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setDate(4, date);
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    public static boolean updatePatientDetails(Patient patient) {
        String sql = """
            UPDATE Patient
            SET name = ?, email = ?, phoneNumber = ?
            WHERE patientID = ?
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhoneNumber());
            //stmt.setString(4, patient.getAddress());
            stmt.setInt(4, patient.getID());
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Bill getBillByPatientID(int patientID) {
        String sql = "SELECT * FROM Bills WHERE patientID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                return new Bill(
                    rs.getInt("billID"),
                    rs.getDouble("amount"),
                    rs.getDouble("remainingAmount"),
                    rs.getString("accountNumber"),
                    rs.getBoolean("paid")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateBillStatus(int billID, double remainingAmount, boolean isPaid) {
        String sql = "UPDATE Bills SET remainingAmount = ?, Paid = ? WHERE billID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, remainingAmount);
            stmt.setBoolean(2, isPaid);
            stmt.setInt(3, billID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static boolean updateHealthRecords(int patientId, String allergies, String medications, String pastIllnesses,
                                          String surgeries, String familyHistory, String notes) {
    String query = "UPDATE MedicalHistory SET allergies = ?, medications = ?, pastIllnesses = ?, " +
                   "surgeries = ?, familyHistory = ?, notes = ? WHERE patientID = ?";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, allergies);
        ps.setString(2, medications);
        ps.setString(3, pastIllnesses);
        ps.setString(4, surgeries);
        ps.setString(5, familyHistory);
        ps.setString(6, notes);
        ps.setInt(7, patientId);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public static boolean removeAppointment(int patientId, int doctorId) {
        String query = "DELETE FROM Appointments WHERE patientID = ? AND doctorID = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addBill(int patientId,int doctorID) {
        String query = "INSERT INTO Bills (patientID, amount, paid,doctorID,remainingAmount) VALUES (?, 1500, FALSE,?,1500)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateDoctorProfile(int doctorId, String name, String phone, String address, String specialization) {
        String sql = "UPDATE Doctor SET name = ?, phoneNumber = ?, address = ?, specialization = ? WHERE doctorID = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setString(4, specialization);
            pstmt.setInt(5, doctorId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating doctor profile: " + e.getMessage());
            return false;
        }
    }
  
    public static boolean updatePatientProfile(int patientID, String name, String phone, String address) {
        String sql = "UPDATE Patient SET name = ?, phoneNumber = ?, address = ? WHERE patientID = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setInt(4, patientID);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating patient profile: " + e.getMessage());
            return false;
        }
    }
   
    public static boolean updatePassword(String email, String currentPassword, String newPassword) {
        // First verify current password
        String verifySql = "SELECT password FROM login WHERE username = ?";
        String updateSql = "UPDATE login SET password = ? WHERE username = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            // Verify current password
            try (PreparedStatement verifyStmt = connection.prepareStatement(verifySql)) {
                verifyStmt.setString(1, email);
                ResultSet rs = verifyStmt.executeQuery();
                
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    // You should use proper password hashing in production
                    if (!currentPassword.equals(storedPassword)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            
            // Update to new password
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                // You should hash the password in production
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, email);
                
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            System.out.println("Error updating doctor password: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertComplaint(int patientID, String complaintText) {
        String insertComplaintQuery = "INSERT INTO PatientComplaints (PatientID, ComplaintText) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertComplaintQuery)) {

            preparedStatement.setInt(1, patientID);
            preparedStatement.setString(2, complaintText);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Return false if an error occurred
        }
    }

    public static ObservableList<Complaint> getUnresolvedComplaints() {
        ObservableList<Complaint> complaints = FXCollections.observableArrayList();
        String query = "SELECT * FROM PatientComplaints WHERE Status = 'Pending'";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int complaintID = resultSet.getInt("ComplaintID");
                int patientID = resultSet.getInt("PatientID");
                String complaintText = resultSet.getString("ComplaintText");
                String status = resultSet.getString("Status");
                Timestamp submissionDate = resultSet.getTimestamp("SubmissionDate");
                
                complaints.add(new Complaint(complaintID, patientID, complaintText, status, submissionDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaints;
    }

    
    public static boolean markComplaintAsResolved(int complaintID) {
        String updateQuery = "UPDATE PatientComplaints SET Status = 'Resolved' WHERE ComplaintID = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, complaintID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
