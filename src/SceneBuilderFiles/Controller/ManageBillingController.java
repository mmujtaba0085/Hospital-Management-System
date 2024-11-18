package SceneBuilderFiles.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import packages.Person.Patient;
//import packages.Others.Bill;
import packages.Others.Service;
import java.io.IOException;
import java.util.List;
import packages.Others.BillItem;

public class ManageBillingController {

    @FXML
    private TableView<Patient> patientTableView; // TableView for selecting patient
    @FXML
    private TableView<BillItem> billSummaryTable; // TableView for displaying the bill details

    @FXML
    private Button confirmBillButton;
    @FXML
    private Button disputeBillButton;
    @FXML
    private Button backButton;

    @FXML
    private TableColumn<BillItem, String> serviceColumn;

    @FXML
    private TableColumn<BillItem, Double> amountColumn;

    @FXML
    private void initialize() {
        serviceColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());

    }

    @FXML
    private void handlePatientSelection() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            generateBill(selectedPatient);
        }
    }

    private void generateBill(Patient patient) {
        List<Service> services = patient.getBill().getServices();
        billSummaryTable.getItems().clear();
        for (Service service : services) {
            // Create BillItem and add it to the table
            BillItem billItem = new BillItem(service.getName(), service.getCost());
            billSummaryTable.getItems().add(billItem);
        }
    }

    @FXML
    private void handleConfirmBill() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            sendBillToPatientPortal(selectedPatient);
        }
    }

    @FXML
    private void handleDisputeBill() {
        Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            flagBillForReview(selectedPatient);
        }
    }

    private void sendBillToPatientPortal(Patient patient) {
        System.out.println("Bill sent to patient portal for " + patient.getName());
    }

    private void flagBillForReview(Patient patient) {
        System.out.println("Bill disputed and flagged for review for " + patient.getName());
    }

    @FXML
    private void goBackToDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        AnchorPane root = loader.load();
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();
    }
}
