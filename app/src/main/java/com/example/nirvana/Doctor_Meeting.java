package com.example.nirvana;

public class Doctor_Meeting {
   public String p_name,p_age,p_problem,p_gender,p_phone,time,date,submission_date,d_phone,submission_time,d_username,link;

    public Doctor_Meeting(String p_name, String p_age, String p_problem, String p_gender, String p_phone, String time,String submission_time, String date,String submission_date,String d_phone,String d_username,String link) {
        this.p_name = p_name;
        this.p_age = p_age;
        this.p_problem = p_problem;
        this.p_gender = p_gender;
        this.p_phone = p_phone;
        this.time = time;
        this.date = date;
        this.submission_date=submission_date;
        this.d_phone=d_phone;
        this.submission_time=submission_time;
        this.d_username=d_username;
        this.link=link;
    }
}
