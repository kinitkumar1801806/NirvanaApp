package com.example.nirvana.Patients;

public class Patient_Meeting {

   public String p_name,p_problem,d_name,time,date,submission_date,d_bio,submission_time,Did,complete;

    public Patient_Meeting(String p_name, String p_problem, String d_name, String time, String date, String submission_date, String d_bio, String submission_time, String did, String complete) {
        this.p_name = p_name;
        this.p_problem = p_problem;
        this.d_name = d_name;
        this.time = time;
        this.date = date;
        this.submission_date = submission_date;
        this.d_bio = d_bio;
        this.submission_time = submission_time;
        Did = did;
        this.complete = complete;
    }
}
