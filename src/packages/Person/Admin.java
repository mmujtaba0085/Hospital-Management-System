package packages.Person;

public class Admin extends Person {
    Admin(){

    }
    Admin(int ID, String name, String email, long phoneNumber){

    }
    void patientDetails(){
        System.out.println("Admin Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
}
