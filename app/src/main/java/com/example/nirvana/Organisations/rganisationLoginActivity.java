package com.example.nirvana.Organisations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nirvana.Clinics.ClinicLoginActivity;
import com.example.nirvana.Hospitals.HospitalLoginActivity;
import com.example.nirvana.Offices.OfficeLoginActivity;
import com.example.nirvana.R;
import com.example.nirvana.Schools.SchoolLoginActivity;

public class rganisationLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rganisation_login);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    public void school_btn(View view) {
        Intent intent=new Intent(this, SchoolLoginActivity.class);
        startActivity(intent);
    }

    public void office_btn(View view) {
        Intent intent=new Intent(this, OfficeLoginActivity.class);
        startActivity(intent);
    }

    public void hospital_btn(View view) {
        Intent intent=new Intent(this, HospitalLoginActivity.class);
        startActivity(intent);
    }

    public void clinic_btn(View view) {
        Intent intent=new Intent(this, ClinicLoginActivity.class);
        startActivity(intent);
    }
}