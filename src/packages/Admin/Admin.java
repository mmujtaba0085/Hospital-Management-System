package packages.Admin;

public class Admin {
    int ID;
    String name;
    String email;
    long phoneNumber;
    Admin(){
        ID=0;
        name="";
        email="";
        phoneNumber=0;
    }
    Admin(int ID, String name, String email, long phoneNumber){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
    void patientDetails(){
        System.out.println("Admin Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
}
