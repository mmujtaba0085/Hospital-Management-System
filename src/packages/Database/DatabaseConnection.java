package packages.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import packages.Person.*;
import packages.Others.*;;

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


}
