package com.example.nirvana.Patients;

public class Patient_Meeting {

   public String p_name,p_age,p_problem,p_gender,d_name,d_phone,time,date,submission_date,d_username,link,submission_time;
    public Patient_Meeting(String p_name, String p_age, String p_problem, String p_gender, String d_name, String d_phone,String time,String Date,String submission_date,String d_username,String link,String submission_time) {
        this.p_name = p_name;
        this.p_age = p_age;
        this.p_problem = p_problem;
        this.p_gender = p_gender;
        this.d_name = d_name;
        this.d_phone = d_phone;
        this.time=time;
        this.date=Date;
        this.submission_date=submission_date;
        this.d_username=d_username;
        this.link=link;
        this.submission_time=submission_time;
    }
}
