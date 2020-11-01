package com.example.nirvana.Hospitals;

public class Hospital_details {
    public String hospital_name,address,email,contact,specific_need,link,Id,password;

    public Hospital_details(String hospital_name, String address, String email, String contact, String specific_need,String link,String Id,String password) {
        this.hospital_name = hospital_name;
        this.address = address;
        this.email = email;
        this.contact = contact;
        this.specific_need = specific_need;
        this.link=link;
        this.Id=Id;
        this.password=password;
    }
}
