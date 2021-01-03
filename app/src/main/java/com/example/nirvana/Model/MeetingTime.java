package com.example.nirvana.Model;

public class MeetingTime {
    public String lastChangeDate,fromTime,toTime,no_of_slots,slot_details;

    public MeetingTime(String lastChangeDate, String fromTime, String toTime, String no_of_slots, String slot_details) {
        this.lastChangeDate = lastChangeDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.no_of_slots = no_of_slots;
        this.slot_details = slot_details;
    }
}
