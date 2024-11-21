package packages.Person;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Admin extends Person {
    @SuppressWarnings("unused")
    private Date hireDate;
    public Admin(){
    
    }
    public Admin(int ID, String name, String email, String phoneNumber, java.util.Date hireDate){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.hireDate=(Date) hireDate;
    }
    void patientDetails(){
        System.out.println("Admin Details:\n");
        System.out.println("ID: "+ID+"\t name: "+name+"\t email: "+email+"\tPhone Number: "+phoneNumber+"\n");
    }
    void ViewPatients(LinkedList<Patient> Patients){
        System.out.println("---------------- Patients List ----------------\n");
        if(Patients.isEmpty()){
            System.out.println("No patient is registered in the System!\n");
        }
        else{
            for(int i=0;i<Patients.size();i++){
                Patients.get(i).patientDetails();       // Fetch details of a particular patient
            }
        }
    }
    void viewDoctors(LinkedList<Patient> Doctors){
        System.out.println("---------------- Doctors List ----------------\n");
        if(Doctors.isEmpty()){
            System.out.println("No doctor is registered in the System!\n");
        }
        else{
            for(int i=0;i<Doctors.size();i++){
                Doctors.get(i).patientDetails();
            }
        }
    }
    public void addPatient(LinkedList<Patient> Patients){
        Patient p=new Patient();
        Scanner scanner = new Scanner(System.in);
        String n, em;
        String pn;
        System.out.println("Enter Name: ");
        n=scanner.nextLine();
        System.out.println("Enter Email: ");
        em=scanner.nextLine();
        System.out.println("Enter Phone Number: ");
        pn=scanner.nextLine();
        //p.setPatientDetails(n, em, pn);
        Patients.add(p);
        scanner.close();
    }
    public void addDoctor(LinkedList<Doctor> Doctors){
        Doctor d=new Doctor();
        Scanner scanner = new Scanner(System.in);
        String n, em, sp, pn;
        System.out.println("Enter Name: ");
        n=scanner.nextLine();
        System.out.println("Enter Email: ");
        em=scanner.nextLine();
        System.out.println("Enter Phone Number: ");
        pn=scanner.nextLine();
        System.out.println("Enter Specialization: ");
        sp=scanner.nextLine();
        //d.setDoctorDetails(n, em, pn, sp);
        Doctors.add(d);
        scanner.close();
    }
    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
