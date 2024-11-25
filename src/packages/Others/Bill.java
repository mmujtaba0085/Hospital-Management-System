package packages.Others;

import java.util.List;

public class Bill {
    private int billId;
    private double amount;
    private double remainingAmount;
    private boolean paid;
    private String patientName;
    private String accountNumber;
    private List<Service> services; // List of services for the patient

    public Bill() {
        this.amount = 0;
        this.paid = false;
    }

    public Bill(int amount) {
        this.amount = amount;
    }

    public Bill(int billID, double amount, double remaining, String accountNumber, boolean paid){
        this.billId = billID;
        this.amount = amount;
        this.remainingAmount = remaining;
        this.accountNumber = accountNumber;
        this.paid = paid;
    }

    public double getRemainingAmount(){
        return this.remainingAmount;
    }

    // Method to generate the bill from a list of services
    public void generateBill(List<Service> services) {
        this.services = services;
        double totalAmount = 0;
        for (Service service : services) {
            totalAmount += service.getCost(); // Add the cost of each service to the total
        }
        this.amount = totalAmount; // Set the total amount in the bill
    }

    public void payBill(int amt) {
        if (amt >= amount) {
            amount = 0;
            paid = true;
        } else {
            amount -= amt;
        }
    }

    public void displayBill() {
        System.out.println("Amount to be paid: " + amount + "\n");
    }

    public int getID(){
        return billId;
    }

    public double getAmount() {
        return amount;
    }

    public boolean getPaid() {
        return paid;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setID(int id){
        this.billId = id;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public void setPaid(boolean paid){
        this.paid = paid;
    }

    public void setPatientName(String name){
        this.patientName = name;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount=remainingAmount;
    }
}
