package packages.Person;


public class Doctor extends Person {
    String specialization;



    public Doctor(String name,String specialization) {
        this.name=name;
        this.specialization = specialization;
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
