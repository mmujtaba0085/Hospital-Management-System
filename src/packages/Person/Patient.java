package packages.Person;

import packages.Others.Appointment;
import packages.Others.Bill;
import packages.Others.Service;
import java.util.List;

public class Patient extends Person {
    String record;
    Bill bill;
    Appointment appointment;

    public Patient() {
        record = "";
        appointment = new Appointment();
        this.bill = new Bill();
    }

    public Patient(int ID, String name, String email, String phoneNumber, String record) {
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.record=record;
    }

    public String getName() {
        return this.name;
    }

    public void patientDetails() {
        System.out.println("Patient Details:\n");
        System.out.println(
                "ID: " + ID + "\t name: " + name + "\t email: " + email + "\tPhone Number: " + phoneNumber + "\n");
    }

    public void addPatientMedicalReport(String record) {
        this.record = record;
    }

    public void viewDoctorProfilesList() {
        System.out.println("Doctor Profiles List:\n");
    }

    public void payBill(int amt) {
        bill.payBill(amt);
    }

    public void bookAppointment() {

    }

    public Bill getBill() {
        return this.bill;
    }
    public String getEmail() {
        return email;
    }
    public String getRecord(){
        return record;
    }
    public int getID() {
        return ID;
    }


    public void generateBill(List<Service> services) {
        this.bill.generateBill(services);
    }

    public void setPatientDetails(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
