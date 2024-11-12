package packages.Patient;

import packages.Others.Bill;

public class Patient {
    int ID;
    String name;
    String email;
    long phoneNumber;
    String address;
    String record;
    Bill bill;
    Patient(){
        ID=0;
        name="";
        email="";
        phoneNumber=0;
        address="";
        record="";
    }
    Patient(int ID, String name, String email, long phoneNumber, String address){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.address=address;
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