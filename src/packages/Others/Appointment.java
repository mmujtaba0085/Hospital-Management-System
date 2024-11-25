package packages.Others;

import java.sql.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Appointment {
    private int appointmentID;
    private String patientName;
    private String doctorName;
    private int start_time;
    private int end_time;
    private Date date;
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private Date appointedDay;

    // Add getter and setter
    public Date getAppointedDay() {
        return appointedDay;
    }

    public void setAppointedDay(Date appointedDay) {
        this.appointedDay = appointedDay;
    }

    // Update constructor
    public Appointment(int appointmentID, String patientName, String doctorName, Date appointedDay) {
        this.appointmentID = appointmentID;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointedDay = appointedDay;
    }

    


    public Appointment(int appointmentID, String patientName, String doctorName, int start, int end, Date date) {
        this.appointmentID = appointmentID;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.start_time = start;
        this.end_time=end;
        this.date=date;
    }
    public Appointment(){
        this.appointmentID = 0;
        this.patientName = "";
        this.doctorName = "";
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    
    public boolean isSelected() {
        return selected.get();
    }
    public void setID(int ID){
        this.appointmentID = ID;
    }
    public void setPatientName(String name){
        patientName=name;
    }
    public void setDoctorName(String name){
        doctorName=name;
    }
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
    public int getAppointmentID() {
        return appointmentID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public int getStartTime() {
        return start_time;
    }

    public int getEndTime(){
        return end_time;
    }

    public Date getDate(){
        return date;
    }
}
