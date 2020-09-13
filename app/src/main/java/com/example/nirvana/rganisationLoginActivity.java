package com.example.nirvana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class rganisationLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rganisation_login);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    public void school_btn(View view) {
        Intent intent=new Intent(this,SchoolLoginActivity.class);
        startActivity(intent);
    }

    public void office_btn(View view) {
        Intent intent=new Intent(this,OfficeLoginActivity.class);
        startActivity(intent);
    }

    public void hospital_btn(View view) {
        Intent intent=new Intent(this,HospitalLoginActivity.class);
        startActivity(intent);
    }

    public void clinic_btn(View view) {
        Intent intent=new Intent(this,ClinicLoginActivity.class);
        startActivity(intent);
    }
}