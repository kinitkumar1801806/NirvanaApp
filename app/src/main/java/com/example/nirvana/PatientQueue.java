package com.example.nirvana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent intent=new Intent(this,PendingMeetingsStep1.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }

    public void finished_meetings(View view) {
    }

    public void unscheduled_meetings(View view) {
        Intent intent=new Intent(this,Not_Fixed_Meetings.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }
}
