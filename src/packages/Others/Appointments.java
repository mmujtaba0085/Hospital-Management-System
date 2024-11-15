package packages.Others;

import java.time.LocalTime;

public class Appointments {
    TimeSlots timeSlot;
    public Appointments(){
        timeSlot=new TimeSlots();
    }
    public void setTimeSlot(LocalTime starTime, LocalTime endTime){
        timeSlot.setTimeSlot(starTime, endTime);
    }
}