import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HospitalManagementLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title label
        Label titleLabel = new Label("Orenix");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Hospital logo 
        ImageView logoView = new ImageView(new Image("file:logo.png"));
        logoView.setFitWidth(100);
        logoView.setFitHeight(100);

        // Labels and fields for username and password
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        // Login button action
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Simple authentication logic for demonstration
                if ("admin".equals(username) && "password".equals(password)) {
                    messageLabel.setText("Login Successful!");
                    messageLabel.setTextFill(Color.GREEN);
                } else {
                    messageLabel.setText("Invalid Username or Password");
                    messageLabel.setTextFill(Color.RED);
                }
            }
        });

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(loginButton, 1, 3);
        gridPane.add(messageLabel, 1, 4);

        // VBox layout to add title and logo
        VBox vbox = new VBox(10, titleLabel, logoView, gridPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Scene and stage setup
        Scene scene = new Scene(vbox, 350, 400);
        primaryStage.setTitle("Hospital Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
