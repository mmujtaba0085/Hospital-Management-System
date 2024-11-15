package packages.Person;
import packages.Others.Appointments;
import packages.Others.Bill;

public class Patient extends Person{
    String record;
    Bill bill;
    Appointments appointment;
    Patient(){
        record="";
        appointment=new Appointments();
    }
    void patientDetails(){
        System.out.println("Patient Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
    void addPatientMedicalReport(String record){
        this.record=record;
    }
    void viewDoctorProfilesList(){
        System.out.println("Doctor Profiles List:\n");
    }
    void payBill(int amt){
        bill.payBill(amt);
    }
    void bookAppointment(){
        
    }
    public void setPatientDetails(String name, String email, long phoneNumber){
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
}