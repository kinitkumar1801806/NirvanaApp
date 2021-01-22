package com.example.nirvana.Model;

public class doctor_details {
    public String email,phone,address,gender,fname,lname,pass,affiliation,place_of_practice,year_of_practice,
            linkedIn,Id,link,amount,total_rating,rated_by,age;

    public doctor_details(String email, String phone, String address, String gender, String fname, String lname, String pass, String affiliation, String place_of_practice, String year_of_practice, String linkedIn, String id,
                          String link, String amount, String total_rating, String rated_by,String age) {
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.fname = fname;
        this.lname = lname;
        this.pass = pass;
        this.affiliation = affiliation;
        this.place_of_practice = place_of_practice;
        this.year_of_practice = year_of_practice;
        this.linkedIn = linkedIn;
        this.Id = id;
        this.link = link;
        this.amount = amount;
        this.total_rating = total_rating;
        this.rated_by = rated_by;
        this.age=age;
    }
}
