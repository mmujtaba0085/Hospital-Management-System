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
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public String getName(){
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail(){
        return email;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
}
