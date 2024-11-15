package packages.Person;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;

import packages.Others.Appointments;
import packages.Others.TimeSlots;

public class Doctor extends Person {
    String specialization;
    LinkedList<Appointments> appointments;
    LinkedList<TimeSlots> timeSlots;
    Doctor(){
        specialization="";
        appointments=new LinkedList<>();
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
    void setDoctorDetails(String name, String email, long phoneNumber, String specialization){
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.specialization=specialization;
    }
    void viewAppointments(){

    }
    void addTimeSlots(){
        
    }
    void addAppointments(){
        appointments.add(new Appointments());
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Enter start time (HH:mm:ss): ");
            String t1 = scanner.nextLine();
            System.out.println("Enter end time (HH:mm:ss): ");
            String t2 = scanner.nextLine();
            LocalTime startTime, endTime;
            // parseing the input string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            // Parse the string input to a LocalTime object
            try {
                startTime = LocalTime.parse(t1, formatter);
                endTime = LocalTime.parse(t2, formatter);
                appointments.getLast().setTimeSlot(startTime, endTime);
                break;
            } catch (Exception e) {
                System.out.println("Invalid time format. Please use HH:mm:ss.");
            }
        }
        // Close the scanner
        scanner.close();
    }
}