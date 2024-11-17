package packages.Others;

import java.sql.Timestamp;

public class Appointment {
    private int appointmentID;
    private String patientName;
    private String doctorName;
    private Timestamp timeOfAppointment;

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
