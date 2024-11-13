package packages.Patient;
import packages.*;

import packages.Others.Bill;

public class Patient extends Person{
    String record;
    Bill bill;
    Patient(){
        record="";
    }
    void patientDetails(){
        System.out.println("Patient Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
    void addPatientMedicalReport(String record){
        this.record=record;
    }
    void viewDoctorProfiles(){

    }
    void payBill(){
        bill.payBill();
    }
    void bookAppointment(){

    }
}