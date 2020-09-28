package com.example.nirvana;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.nirvana.Clinics.ClinicWelcomeActivity;
import com.example.nirvana.Doctors.Doctor_Welcome_Activity;
import com.example.nirvana.Hospitals.HospitalWelcomeActivity;
import com.example.nirvana.Offices.OfficeWelcomeActivity;
import com.example.nirvana.Patients.Patient_Welcome_Activity;
import com.example.nirvana.Schools.SchoolWelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {
     FirebaseAuth auth;
     private String Id;
     private String check="no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher=new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread
    {
        public void run()
        {
            try
            {
                sleep(3000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            auth= FirebaseAuth.getInstance();
            if(auth.getCurrentUser()!=null)
            {
                Id=auth.getCurrentUser().getUid();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors");
                databaseReference.addValueEventListener(new ValueEventListener() {
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
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, Doctor_Welcome_Activity.class);
                                    String phone=(String)userData.get("phone");
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference Reference=firebaseDatabase.getReference().child("Patient");
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
                                String phone=(String)userData.get("phone");
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, Patient_Welcome_Activity.class);
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference Reference1=firebaseDatabase.getReference().child("Schools");
                Reference1.addValueEventListener(new ValueEventListener() {
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
                                String phone=(String)userData.get("contact");
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, SchoolWelcomeActivity.class);
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference Reference2=firebaseDatabase.getReference().child("Clinics");
                Reference2.addValueEventListener(new ValueEventListener() {
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
                                String phone=(String)userData.get("contact");
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, ClinicWelcomeActivity.class);
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference Reference3=firebaseDatabase.getReference().child("Hospitals");
                Reference3.addValueEventListener(new ValueEventListener() {
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
                                String phone=(String)userData.get("contact");
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, HospitalWelcomeActivity.class);
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference Reference4=firebaseDatabase.getReference().child("Offices");
                Reference4.addValueEventListener(new ValueEventListener() {
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
                                String phone=(String)userData.get("contact");
                                if(uid.equals(Id))
                                {
                                    Intent intent=new Intent(SplashScreen.this, OfficeWelcomeActivity.class);
                                    check="yes";
                                    intent.putExtra("phone",phone);
                                    startActivity(intent);
                                    SplashScreen.this.finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
           else
            {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                SplashScreen.this.finish();
            }

        }
    }
}

