package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Fix_Meeting_step3 extends AppCompatActivity {
ArrayList<String>arr;
private String doctor_name,doctor_phone,Uid,patient_phone,username_doctor;
FirebaseAuth mauth;
Task<Void> databaseReference;
Task<Void> databaseReference1;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix__meeting_step3);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        doctor_name=arr.get(0);
        doctor_phone=arr.get(1);
        username_doctor=arr.get(2);
        mauth=FirebaseAuth.getInstance();
        Uid=mauth.getCurrentUser().getUid();
        DatabaseReference Reference=FirebaseDatabase.getInstance().getReference().child("Patient");
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        Object data = hashMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        String uid=(String)userData.get("Id");
                        if(uid.equals(Uid))
                        {
                            patient_phone=(String)userData.get("phone");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Fix_the_Meeting(View view) {
        TextView name,age,problem;
        RadioGroup gender;
        RadioButton patient_gender;
        progressBar=findViewById(R.id.progressBar_patient);
        progressBar.setVisibility(View.VISIBLE);
        name=findViewById(R.id.patient_name);
        age=findViewById(R.id.patient_age);
        problem=findViewById(R.id.patient_problem);
        gender=findViewById(R.id.patient_gender);
        int selectedId=gender.getCheckedRadioButtonId();
        patient_gender=findViewById(selectedId);
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please enter your name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(age.getText()))
        {
            age.setError("Please enter your age");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(problem.getText()))
        {
            problem.setError("Please enter your problem");
            progressBar.setVisibility(View.GONE);
        }
        else if(gender.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this,"Please select the gender",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String name1=name.getText().toString().trim();
            String age1=age.getText().toString().trim();
            String problem1=problem.getText().toString().trim();
            String patient_gender1=patient_gender.getText().toString().trim();
            String time="We will inform you soon";
            String date="We will inform you soon";
            String time1="Not fixed";
            String date1="Not fixed";
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);
            Patient_Meeting patient_meeting=new Patient_Meeting(
                    name1,
                    age1,
                    problem1,
                    patient_gender1,
                    doctor_name,
                    doctor_phone,
                    time,
                    date,
                    thisDate,
                    username_doctor,
                    "None",
                    currentTime
            );
            final Doctor_Meeting doctor_meeting=new Doctor_Meeting(
                    name1,
                    age1,
                    problem1,
                    patient_gender1,
                    patient_phone,
                    time1,
                    currentTime,
                    date1,
                    thisDate,
                    doctor_phone,
                    username_doctor,
                    arr.get(3)
            );

               databaseReference= FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(patient_phone).child(doctor_phone)
                       .setValue(patient_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                           }
                       });

          databaseReference1=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings").child(doctor_phone).child(patient_phone)
                    .setValue(doctor_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(Fix_Meeting_step3.this,Fix_Meeting_step2.class);
                            intent.putStringArrayListExtra("arr",arr);
                            startActivity(intent);

                        }
                    });
        }
    }
}
