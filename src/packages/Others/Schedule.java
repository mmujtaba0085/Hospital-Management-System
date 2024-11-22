package packages.Others;

import javafx.beans.property.SimpleStringProperty;

public class Schedule {
    private SimpleStringProperty day;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private int doctorId;
    private String DoctorName;
    private String daysAvailable;


    public Schedule(String day, String startTime, String endTime) {
        this.day = new SimpleStringProperty(day);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.doctorId=0;
        daysAvailable="";
    }
    public Schedule(int docID,String DoctorName,String daysAvailable) {
        this.DoctorName = DoctorName;
        this.doctorId=docID;
        this.daysAvailable=daysAvailable;
    }

    public String getDay() {
        return day.get();
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public int getDoctorID(){
        return this.doctorId;
    }

    public void setDoctorID(int docID){
        this.doctorId=docID;
    }

    public String getDoctorName() {
        return this.DoctorName;
    }
    

    public void setDoctorName(String DoctorName){
        this.DoctorName=DoctorName;
    }

    public String getAvailableDays(){
        return daysAvailable;
    }
    public void setAvailableDays(String daysAvailable){
        this.daysAvailable=daysAvailable;
    }


}
