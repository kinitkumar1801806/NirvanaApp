package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nirvana.Blogs.DetailBlogFragment;
import com.example.nirvana.Blogs.HomeBlogFragment;
import com.example.nirvana.GoalPlanning.GoalPlanning;
import com.example.nirvana.Model.MeetingTime;
import com.example.nirvana.Patients.PatientQueue;
import com.example.nirvana.ProfileActivity;
import com.example.nirvana.R;
import com.example.nirvana.Call.SinchService;
import com.example.nirvana.YogaTutorials.YogaVideosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Doctor_Welcome_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth auth;
    public TextView doctor_name,doctor_address;
    private int mHour,mMinute;
    ImageView Niri,VideoPlayer,Goal_Planning,Profile,d_profile,Home;
    private String phone,Id,link,time,from_time,to_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__welcome_);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.doctor_drawer);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        auth=FirebaseAuth.getInstance();
        doctor_name=hView.findViewById(R.id.header_name);
        doctor_address=hView.findViewById(R.id.header_address);
        d_profile=hView.findViewById(R.id.doctor_image);
        Home=findViewById(R.id.home);
        VideoPlayer=findViewById(R.id.video_player);
        Niri=findViewById(R.id.niri);
        Goal_Planning=findViewById(R.id.goal_planning);
        Profile=findViewById(R.id.profile);
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            phone=intent.getStringExtra("phone");
            Id=intent.getStringExtra("Id");
            Intent intent1=new Intent(this, SinchService.class);
            intent1.putExtra("phone",phone);
            startService(intent1);
        }
        RetrieveDoctorDetails();
        Bundle bundle=new Bundle();
        bundle.putString("Id",Id);
        HomeBlogFragment homeFragment=new HomeBlogFragment();
        homeFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,homeFragment);
        fragmentTransaction.commit();
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("Id",Id);
                bundle.putString("who","doctor");
                HomeBlogFragment homeFragment=new HomeBlogFragment();
                homeFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,homeFragment);
                fragmentTransaction.commit();
            }
        });
        VideoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Doctor_Welcome_Activity.this, YogaVideosActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Niri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Doctor_Welcome_Activity.this, com.example.nirvana.Niri.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Goal_Planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Doctor_Welcome_Activity.this, GoalPlanning.class);
                intent.putExtra("Id",Id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Doctor_Welcome_Activity.this, ProfileActivity.class);
                intent.putExtra("Id",Id);
                intent.putExtra("who","doctor");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        checkforMeetingTime();
    }

    private void checkforMeetingTime() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Doctors_Meeting_Time").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                  DoAlter();
                }
                else {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    String date = hashMap.get("lastChangeDate").toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date todaydate = new Date();
                    String thisDate = simpleDateFormat.format(todaydate);
                    try {
                        Date BeforeDate=simpleDateFormat.parse(date);
                        Date AfterDate=simpleDateFormat.parse(thisDate);
                        long difference = AfterDate.getTime() - BeforeDate.getTime();
                        float daysBetween =  (difference / (1000*60*60*24));
                        if(daysBetween>=7){
                            DoAlter();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DoAlter() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(Doctor_Welcome_Activity.this).create();
        LayoutInflater inflater = Doctor_Welcome_Activity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.doctor_meeting_time, null);
        EditText fromTime=dialogView.findViewById(R.id.editText3);
        EditText toTime=dialogView.findViewById(R.id.editText4);
        Button Cancel = (Button) dialogView.findViewById(R.id.cancel);
        Button Save = (Button) dialogView.findViewById(R.id.save);
        ImageView fromTimeClock=dialogView.findViewById(R.id.fromtimeclock);
        ImageView toTimeClock=dialogView.findViewById(R.id.totimeclock);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        fromTimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Doctor_Welcome_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String min=null;
                                String hour=null;
                                if(minute<10)
                                {
                                    min="0"+String.valueOf(minute);
                                }
                                else if(minute>10)
                                {
                                    min=String.valueOf(minute);
                                }
                                if(hourOfDay<10)
                                {
                                    hour="0"+String.valueOf(hourOfDay);
                                }
                                else if(hourOfDay>10){
                                    hour=String.valueOf(hourOfDay);
                                }
                                time=hour+":"+min;
                                fromTime.setText(time);
                                from_time=time;
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        toTimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Doctor_Welcome_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String min=null;
                                String hour=null;
                                if(minute<10)
                                {
                                    min="0"+String.valueOf(minute);
                                }
                                else if(minute>10)
                                {
                                    min=String.valueOf(minute);
                                }
                                if(hourOfDay<10)
                                {
                                    hour="0"+String.valueOf(hourOfDay);
                                }
                                else if(hourOfDay>10){
                                    hour=String.valueOf(hourOfDay);
                                }
                                time=hour+":"+min;
                                toTime.setText(time);
                                to_time=time;
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(fromTime.getText()))
                {
                    fromTime.setError("Please set the time for meetings.");
                }
                else if(TextUtils.isEmpty(toTime.getText()))
                {
                    toTime.setError("Please set the time for meetings.");
                }
                else{
                    try {
                        SaveMeetingTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialogBuilder.dismiss();
                }

            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
    }

    private void SaveMeetingTime() throws ParseException {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        String fromHr=from_time.substring(0,2);
        String toHr=to_time.substring(0,2);
        int diff=Integer.parseInt(toHr)-Integer.parseInt(fromHr);
        HashMap<String,String> hashMap=new HashMap<>();
        int i=1,j=1;
        for(j=0;j<7;j++)
        {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            Date date=simpleDateFormat.parse(thisDate);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR,j);
            String nextDate=simpleDateFormat.format(calendar.getTime());
            String day=nextDate;
            String obj="{'slot1':'false'";
            for(i=2;i<=diff;i++)
            {
                String slot="slot"+String.valueOf(i);
                obj+=","+slot+":'false'";
            }
            obj+="}";
            hashMap.put(day,obj);
        }
        JSONObject jsonObject=new JSONObject(hashMap);
        String slot_details=jsonObject.toString();
        MeetingTime meetingTime=new MeetingTime(
                thisDate,
                from_time,
                to_time,
                String.valueOf(diff),
                slot_details
        );
        Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctors_Meeting_Time").child(Id).setValue(meetingTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Doctor_Welcome_Activity.this,"Time set for meetings of this week",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RetrieveDoctorDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                String fname=hashMap.get("fname").toString();
                String lname=hashMap.get("lname").toString();
                String address=hashMap.get("address").toString();
                doctor_name.setText("Mr."+fname+" "+lname);
                doctor_address.setText(address);
                link=hashMap.get("link").toString();
                if(!link.equals("None"))
                {
                    Glide.with(Doctor_Welcome_Activity.this).load(link).into(Profile);
                    Glide.with(Doctor_Welcome_Activity.this).load(link).into(d_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.welcome_doctor_schedule)
        {
            Intent intent=new Intent(this, PatientQueue.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
