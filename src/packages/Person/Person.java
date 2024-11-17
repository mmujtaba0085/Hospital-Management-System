package packages.Person;

public abstract class Person {
    int ID;
    protected String name;
    String email;
    String phoneNumber;
    String address;
    Person(){
        ID=0;
        name="";
        email="";
        phoneNumber="";
        address="";
    }
    Person(int ID, String name, String email, String phoneNumber, String address){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }
    public String getEmail(){
        return email;
    }
}
