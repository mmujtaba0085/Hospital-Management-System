package packages.Person;

public class Doctor extends Person {
    String specialization;
    Doctor(){
        specialization="";
    }
    Doctor(int ID, String name, String email, long phoneNumber, String specialization){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.specialization=specialization;
    }
    void patientDetails(){
        System.out.println("Doctor Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"Specialization: "+specialization+"\n");
    }
    void viewAppointments(){

    }
    void addTimeSlots(){
        
    }
}