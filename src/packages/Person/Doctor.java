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

    
    public int getID(){
        return DoctorID;
    }
    public String getSpecialization(){
        return specialization;
    }
    public void setID(int ID){
        this.DoctorID=ID;
    }
    
    public void setSpecialization(String specialization){
        this.specialization=specialization;
    }
    public void setHireDate(Date date) {
        hirDate=date;
    }
   
    public Date getHireDate() {
        return hirDate;
    }
}
