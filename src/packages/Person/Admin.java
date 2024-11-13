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
    public void addPatient(LinkedList<Patient> Patients){
        Patient p=new Patient();
        Scanner scanner = new Scanner(System.in);
        String n, em; long pn;
        System.out.println("Enter Name: ");
        n=scanner.nextLine();
        System.out.println("Enter Email: ");
        em=scanner.nextLine();
        System.out.println("Enter Phone Number: ");
        pn=scanner.nextLong();
        p.setPatientDetails(n, em, pn);
        Patients.add(p);
        scanner.close();
    }
    public void addDoctor(LinkedList<Doctor> Doctors){
        Doctor d=new Doctor();
        Scanner scanner = new Scanner(System.in);
        String n, em, sp; long pn;
        System.out.println("Enter Name: ");
        n=scanner.nextLine();
        System.out.println("Enter Email: ");
        em=scanner.nextLine();
        System.out.println("Enter Phone Number: ");
        pn=scanner.nextLong();
        System.out.println("Enter Specialization: ");
        sp=scanner.nextLine();
        d.setDoctorDetails(n, em, pn, sp);
        Doctors.add(d);
        scanner.close();
    }
}
