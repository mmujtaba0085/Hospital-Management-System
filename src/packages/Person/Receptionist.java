package packages.Person;

import java.sql.Date;

public class Receptionist extends Person {
    private Date hiredate;
    public Receptionist(){
        
    }
    public Receptionist(int receptionistId, String name, String email, String phoneNumber, Date hireDate) {
        this.ID=receptionistId;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.hiredate=hireDate;
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
    public Date getHireDate(){
        return hiredate;
    }


    public void setID(int ID) {
        this.ID=ID;
    }
    public void setName(String name) {
        this.name=name;
    }
    public void setEmail(String email) {
        this.email=email;
    }
    public void setHireDate(Date hiredate){
        this.hiredate=hiredate;
    }
}
