package packages.System;

import packages.Others.TimeSlots;
import packages.Person.Admin;
import packages.Person.Doctor;
import packages.Person.Patient;

import java.util.LinkedList;

public class System {
    LinkedList<Patient> Patients;
    LinkedList<Doctor> Doctors;
    Admin admin;
    LinkedList<TimeSlots> timeSlots;
    System(){
        Patients=new LinkedList<>();
        Doctors=new LinkedList<>();
        admin=new Admin();
        timeSlots=new LinkedList<>();
    }
}