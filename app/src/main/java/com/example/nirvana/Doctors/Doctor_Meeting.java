package com.example.nirvana.Doctors;

public class Doctor_Meeting {
   public String p_name,p_problem,time,date,submission_date,submission_time,Pid,complete,link;

    public Doctor_Meeting(String p_name, String p_problem, String time, String date, String submission_date, String submission_time, String pid, String complete, String link) {
        this.p_name = p_name;
        this.p_problem = p_problem;
        this.time = time;
        this.date = date;
        this.submission_date = submission_date;
        this.submission_time = submission_time;
        Pid = pid;
        this.complete = complete;
        this.link = link;
    }
}
