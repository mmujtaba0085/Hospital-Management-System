package packages.Person;

import java.sql.Date;

public class Doctor extends Person {
    String specialization;
    int DoctorID;
    Date hirDate;

    public Doctor(int doctorID, String name, String specialization, String email, String phnumber, java.util.Date hireDate) {
        this.name=name;
        this.specialization = specialization;
        this.DoctorID=doctorID;
        this.email=email;
        this.phoneNumber=phnumber;
        this.hirDate=(Date) hireDate;

    }
    public Doctor(){
        this.name="";
        this.specialization = "";
    }

    public String getName(){
        return name;
    }

    public String getSpecialization(){
        return specialization;
    }
    public void setDoctorDetails(String name, String email,String phoneNumber,String Specialization)
    {
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.specialization=Specialization;
    }
}
