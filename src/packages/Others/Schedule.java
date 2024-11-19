package packages.Others;

import javafx.beans.property.SimpleStringProperty;

public class Schedule {
    private SimpleStringProperty day;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;

    public Schedule(String day, String startTime, String endTime) {
        this.day = new SimpleStringProperty(day);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
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
}
