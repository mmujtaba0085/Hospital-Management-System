package packages.Others;

import java.util.List;

public class Bill {
    private double amount;
    private boolean paid;
    private List<Service> services; // List of services for the patient

    public Bill() {
        this.amount = 0;
        this.paid = false;
    }

    public Bill(int amount) {
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
