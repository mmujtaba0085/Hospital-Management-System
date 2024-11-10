import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/HMS";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "icu321@"; // Replace with your MySQL password

    // Method to check login credentials from login table
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM login WHERE username = ? AND password = ?"; // updated to check from login table
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            // Set the values for the placeholders
            statement.setString(1, username);
            statement.setString(2, password);
            
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            
            // Check if any record matches
            return resultSet.next(); // Returns true if a record is found, false otherwise

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
