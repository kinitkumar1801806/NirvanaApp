package com.example.nirvana.Patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nirvana.Doctors.Not_Fixed_Meetings;
import com.example.nirvana.Doctors.PendingMeetingsStep1;
import com.example.nirvana.R;

public class PatientQueue extends AppCompatActivity {
private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_queue);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");

    }

    public void scheduled_meetings(View view) {
        Intent intent=new Intent(this, PendingMeetingsStep1.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }

    public void finished_meetings(View view) {
    }

    public void unscheduled_meetings(View view) {
        Intent intent=new Intent(this, Not_Fixed_Meetings.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }
}
