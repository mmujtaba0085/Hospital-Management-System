package packages.Others;

import java.sql.Timestamp;

public class Complaint {
    private int complaintID;
    private int patientID;
    private String complaintText;
    private String status;
    private Timestamp submissionDate;

    // Constructor
    public Complaint(int complaintID, int patientID, String complaintText, String status, Timestamp submissionDate) {
        this.complaintID = complaintID;
        this.patientID = patientID;
        this.complaintText = complaintText;
        this.status = status;
        this.submissionDate = submissionDate;
    }

    // Getters and Setters
    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }
}
