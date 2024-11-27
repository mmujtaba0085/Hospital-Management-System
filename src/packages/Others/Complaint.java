package packages.Others;

import java.sql.Timestamp;

public class Complaint {
    private int complaintID;
    private String userType;
    private String complaintText;
    private String status;
    private Timestamp submissionDate;

    // Constructor
    public Complaint(int complaintID, String UserType, String complaintText, String status, Timestamp submissionDate) {
        this.complaintID = complaintID;
        this.userType = UserType;
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

    public String getuserType() {
        return userType;
    }

    public void setuserType(String userType) {
        this.userType = userType;
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
