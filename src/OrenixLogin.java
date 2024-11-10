import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OrenixLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Orenix Hospital Management System - Login");

        // Load and display the logo
        ImageView logo = new ImageView(new Image("images/orenix1.png")); // adjust path if needed
        logo.setFitHeight(150);
        logo.setFitWidth(150);

        // Username and Password fields with fixed width
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(250);  // Set fixed width
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(250);  // Set fixed width

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font(16));
        loginButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-background-radius: 10;");
        loginButton.setMaxWidth(150); // Optional: Fix button width too

        // Error message label
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorMessage.setText("Please enter both username and password.");
            } else {
                // Check credentials using DatabaseConnection
                boolean isAuthenticated = DatabaseConnection.authenticateUser(username, password);
                
                if (isAuthenticated) {
                    errorMessage.setTextFill(Color.GREEN);
                    errorMessage.setText("Login successful!");
                } else {
                    errorMessage.setTextFill(Color.RED);
                    errorMessage.setText("Invalid username or password.");
                }
            }
        });

        // Layout for login fields and button
        VBox loginBox = new VBox(10, usernameField, passwordField, loginButton, errorMessage);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));

        // Main layout
        VBox mainLayout = new VBox(20, logo, loginBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));

        // Background gradient inspired by the logo's color palette
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#2E8BC0")),
            new Stop(0.5, Color.web("#FF6F61")),
            new Stop(1, Color.web("#FFD662"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, null, stops);

        mainLayout.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Scene setup
        Scene scene = new Scene(mainLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true); // Optional: Set to fullscreen if desired
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
