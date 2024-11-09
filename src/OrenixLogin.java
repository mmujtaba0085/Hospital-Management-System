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

        // Username and Password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font(16));
        loginButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-background-radius: 10;");

        // Error message label
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);

        loginButton.setOnAction(e -> {
            // Mock login check
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                errorMessage.setText("Please enter both username and password.");
            } else {
                errorMessage.setText("");
                // Insert login logic here
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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
