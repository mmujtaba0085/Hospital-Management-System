package packages.Person;

import java.util.LinkedList;
import java.util.Scanner;

public class Admin extends Person {
    public Admin(){

    }
    Admin(int ID, String name, String email, long phoneNumber){

    }
    void patientDetails(){
        System.out.println("Admin Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
    void ViewPatients(LinkedList<Patient> Patients){

    }
    void viewDoctors(LinkedList<Patient> Doctors){
        
    }
    public void addPatient(LinkedList<Patient> Patients, String name, String email, long phoneNumber){
        Patient p;
        if(Patients.isEmpty()){
            Scanner in = new Scanner(System.in);
            String stringInput;
            long longInput;
            System.out.println("Enter your ame: ");
            stringInput=in.next();
            System.out.println("Enter your Email: ");
            stringInput=in.next();
            System.out.println("Enter your Phone Number: ");
            longInput=in.nextLong();
            p.setPatientDetails(name, email, phoneNumber);
            Patients.addFirst(p);
        }
    }
}
