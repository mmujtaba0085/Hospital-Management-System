package SceneBuilderFiles.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class PayBillController {

    @FXML
    private TextArea nameField;

    @FXML
    private TextArea emailField;

    @FXML
    private TextArea phoneField;

    @FXML
    private TextArea accountNumberField;

    @FXML
    private TextArea pinField;

    @FXML
    private TextArea cvvField;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label totalAmountValue;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ImageView backButton;

    /**
     * Initializes the controller class. 
     * This method is called after the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        // Set up default actions or data bindings if necessary
        totalAmountValue.setText("2200"); // Example: setting default value
    }

    /**
     * Handles the "Submit" button click event.
     */
    @FXML
    private void handleSubmitAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String accountNumber = accountNumberField.getText();
        String pin = pinField.getText();
        String cvv = cvvField.getText();

        // Perform validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            accountNumber.isEmpty() || pin.isEmpty() || cvv.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Example processing (e.g., send data to a server)
        System.out.println("Submitting payment:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("PIN: " + pin);
        System.out.println("CVV: " + cvv);
        System.out.println("Total Amount: " + totalAmountValue.getText());
    }

    /**
     * Handles the "Cancel" button click event.
     */
    @FXML
    private void handleCancelAction() {
        // Example: clear all fields
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        accountNumberField.clear();
        pinField.clear();
        cvvField.clear();

        System.out.println("Payment form cleared.");
    }

    /**
     * Handles the back button click event.
     */
    @FXML
    private void handleBackAction() {
        System.out.println("Navigating back to the previous screen.");
        // Implement navigation logic here
    }
}
