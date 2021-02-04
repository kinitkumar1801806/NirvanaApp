package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nirvana.Doctors.Doctor_Welcome_Activity;
import com.example.nirvana.Patients.PatientLoginActivity;
import com.example.nirvana.Patients.Patient_Welcome_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginByPasswordActivity extends AppCompatActivity {
    public String phone, who, Id,Email;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        who = intent.getStringExtra("who");
    }

    public void login(View view) {
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
                databaseReference = firebaseDatabase.getReference().child("Doctors");
                CheckPassword(password,phone);
            } else if (who.equals("patient")) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Patient");
                CheckPassword(password,phone);
            }
        }
    }

    public void CheckPassword(String pass,String phone) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        Object data=hashMap.get(key);
                        HashMap<String,Object> userData= (HashMap<String, Object>) data;
                        String phone1=userData.get("phone").toString();
                        Id=userData.get("Id").toString();
                        Email=userData.get("email").toString();
                        if(phone.equals(phone1))
                        {
                            String password = userData.get("pass").toString();
                            if (pass.equals(password)) {
                                if(who.equals("doctor"))
                                {
                                        progressBar.setVisibility(View.GONE);
                                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                        mAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Intent intent=new Intent(LoginByPasswordActivity.this, Doctor_Welcome_Activity.class);
                                                intent.putExtra("phone",phone);
                                                intent.putExtra("Id",Id);
                                                startActivity(intent);
                                            }
                                        });


                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                    mAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Intent intent=new Intent(LoginByPasswordActivity.this, Patient_Welcome_Activity.class);
                                            intent.putExtra("phone",phone);
                                            intent.putExtra("Id",Id);
                                            startActivity(intent);
                                        }
                                    });

                                }
                            }
                            else
                            {
                                Password.setError("Enter the correct password");
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    Toast.makeText(LoginByPasswordActivity.this, "This number does not attach with any account.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}