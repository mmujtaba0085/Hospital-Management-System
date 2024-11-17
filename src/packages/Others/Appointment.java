package packages.Others;

import java.sql.Timestamp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Appointment {
    private int appointmentID;
    private String patientName;
    private String doctorName;
    private Timestamp timeOfAppointment;
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    


    public Appointment(int appointmentID, String patientName, String doctorName, Timestamp timeOfAppointment) {
        this.appointmentID = appointmentID;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.timeOfAppointment = timeOfAppointment;
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

    public Timestamp getTimeOfAppointment() {
        return timeOfAppointment;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID + 
               ", Patient: " + patientName + 
               ", Doctor: " + doctorName + 
               ", Time: " + timeOfAppointment;
    }
}
