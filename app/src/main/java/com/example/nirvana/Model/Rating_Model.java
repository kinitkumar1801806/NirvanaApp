package com.example.nirvana.Model;

public class Rating_Model {
    public String patient_name,rating,review,date,time;

    public Rating_Model(String patient_name, String rating, String review, String date, String time) {
        this.patient_name = patient_name;
        this.rating = rating;
        this.review = review;
        this.date = date;
        this.time = time;
    }
}
