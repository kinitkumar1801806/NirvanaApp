package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nirvana.Clinics.ClinicWelcomeActivity;
import com.example.nirvana.Doctors.Doctor_Welcome_Activity;
import com.example.nirvana.Hospitals.HospitalWelcomeActivity;
import com.example.nirvana.Offices.OfficeWelcomeActivity;
import com.example.nirvana.Patients.Patient_Welcome_Activity;
import com.example.nirvana.Schools.SchoolLoginActivity;
import com.example.nirvana.Schools.SchoolLoginVerification;
import com.example.nirvana.Schools.SchoolWelcomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginByPasswordActivity extends AppCompatActivity {
    public String phone, who,res;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        who = intent.getStringExtra("who");
    }

    public void login(View view) {
        EditText Password;
        ProgressBar progressBar;
        Password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(Password.getText())) {
            Password.setError("Enter the password");
            progressBar.setVisibility(View.GONE);
        } else {
            String password = Password.getText().toString();
            if (who.equals("doctor")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Doctors").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, Doctor_Welcome_Activity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            } else if (who.equals("patient")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Patients").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, Patient_Welcome_Activity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            } else if (who.equals("clinic")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Clinics").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, ClinicWelcomeActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            } else if (who.equals("school")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Schools").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, SchoolWelcomeActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            } else if (who.equals("hospital")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Hospitals").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, HospitalWelcomeActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }

            } else if (who.equals("office")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Offices").child(phone);
                String result=CheckPassword(password);
                progressBar.setVisibility(View.GONE);
                if(result.equals("Success")) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(LoginByPasswordActivity.this, OfficeWelcomeActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }
            }
        }
    }

    public String CheckPassword(String pass) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    String password = hashMap.get("password").toString();
                    if (pass.equals(password)) {
                       res="Success";
                    }
                } else {
                    Toast.makeText(LoginByPasswordActivity.this, "This number does not attach with any account.", Toast.LENGTH_SHORT).show();
                    res="Failure";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return res;
    }
}