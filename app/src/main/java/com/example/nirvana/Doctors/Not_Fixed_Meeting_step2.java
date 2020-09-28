package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Not_Fixed_Meeting_step2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextView nameView,ageView,genderView,problemView,dateView,timeView;
    private String phone,date,date_period,time_period,time,patient_phone;
    private Calendar calendar;
    private int year, month, day;
    static final int TIME_DIALOG_ID = 1111;
    private int mHour,mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not__fixed__meeting_step2);
        nameView=findViewById(R.id.patient_name);
        ageView=findViewById(R.id.patient_age);
        genderView=findViewById(R.id.patient_gender);
        problemView=findViewById(R.id.patient_problem);
        dateView=findViewById(R.id.date);
        timeView=findViewById(R.id.time);
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        patient_phone=intent.getStringExtra("phone1");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings")
                .child(phone).child(patient_phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                String name=(String)hashMap.get("p_name");
                String age=(String)hashMap.get("p_age");
                String gender=(String)hashMap.get("p_gender");
                String problem=(String)hashMap.get("p_problem");
                date=(String)hashMap.get("date");
                time=(String)hashMap.get("time");
                nameView.setText(name);
                ageView.setText(age);
                genderView.setText(gender);
                problemView.setText(problem);
                dateView.setText(date);
                timeView.setText(time);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void set_date(View view) {
        DialogFragment datePicker = new com.example.nirvana.DatePicker();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String d,m;
        if(month<10)
        {
            m=String.valueOf(month);
            m="0"+m;
        }
        else
        {
            m=String.valueOf(month);
        }
        if(dayOfMonth<10)
        {
            d=String.valueOf(dayOfMonth);
            d="0"+d;
        }
        else
        {
            d=String.valueOf(dayOfMonth);
        }
        String currentDateString = d+"/"+m+"/"+year;
        Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings")
                .child(phone).child(patient_phone).child("date").setValue(currentDateString);
        Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings")
                .child(patient_phone).child(phone).child("date").setValue(currentDateString);
        dateView.setText(currentDateString);
    }
    public void set_time(View view) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if(hourOfDay>=0 && hourOfDay<12){
                            time = hourOfDay + " : " + minute ;
                        } else {
                            if (hourOfDay == 12) {
                                time = hourOfDay + " : " + minute;
                            } else {
                                hourOfDay = hourOfDay - 12;
                                time = hourOfDay + " : " + minute;
                            }
                        }

                        timeView.setText(hourOfDay + ":" + minute);
                        Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings")
                                .child(phone).child(patient_phone).child("time").setValue(time);
                        Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings")
                                .child(patient_phone).child(phone).child("time").setValue(time);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
        if(!time.equals("Not fixed"))
        {
            change();
        }
    }
public void change()
{
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings")
            .child(phone).child(patient_phone);
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Task<Void> databaseReference1= FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Fixed_Meetings")
                    .child(phone).child(patient_phone).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    Task<Void> databaseReference2= FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings")
            .child(phone).child(patient_phone).removeValue();
}
}