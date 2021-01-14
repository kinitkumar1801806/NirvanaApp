package com.example.nirvana.Model;

public class doctor_details {
    public String email,phone,address,gender,fname,lname,pass,affiliation,place_of_practice,year_of_practice,
            linkedIn,Id,link;
    public doctor_details( String email, String phone, String address, String gender,String fname,String lname,String pass,String affiliation,String year_of_practice,String place_of_practice,String linkedIn,
                         String link,String Id) {
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.fname=fname;
        this.lname=lname;
        this.pass=pass;
        this.affiliation=affiliation;
        this.year_of_practice=year_of_practice;
        this.place_of_practice=place_of_practice;
        this.linkedIn=linkedIn;
        this.Id=Id;
        this.link=link;
    }

}
