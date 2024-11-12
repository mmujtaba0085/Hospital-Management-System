package packages.Patient;

public class Patient {
    int ID;
    String name;
    String email;
    long phoneNumber;
    Patient(){
        ID=0;
        name="";
        email="";
        phoneNumber=0;
    }
    Patient(int ID, String name, String email, long phoneNumber){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
    void patientDetails(){
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
}
