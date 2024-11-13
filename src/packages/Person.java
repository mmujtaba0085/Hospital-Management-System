package packages;

public abstract class Person {
    int ID;
    String name;
    String email;
    long phoneNumber;
    String address;
    Person(){
        ID=0;
        name="";
        email="";
        phoneNumber=0;
        address="";
    }
    Person(int ID, String name, String email, long phoneNumber, String address){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }
}
