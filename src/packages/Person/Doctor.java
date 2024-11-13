package packages.Doctor;

public class Doctor {
    int ID;
    String name;
    String email;
    long phoneNumber;
    Doctor(){
        ID=0;
        name="";
        email="";
        phoneNumber=0;
    }
    Doctor(int ID, String name, String email, long phoneNumber){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
    void patientDetails(){
        System.out.println("Doctor Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
}
