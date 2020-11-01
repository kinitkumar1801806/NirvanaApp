package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nirvana.LoginByPasswordActivity;
import com.example.nirvana.Model.CountryCode;
import com.example.nirvana.R;
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

public class PatientLoginActivity extends AppCompatActivity {
    private Spinner spinner;
    String phone1;
    ProgressBar progressBar1;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    String code;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        PatientLoginFragment patientLoginFragment=new PatientLoginFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.patient_login_layout,patientLoginFragment);
        fragmentTransaction.commit();
    }

    public void signup_patient(View view) {
        Intent intent=new Intent(this, PatientSignupActivity.class);
        startActivity(intent);
    }

    public void login_patient(View view) {
        EditText phone;

        spinner = findViewById(R.id.spinnerCountries);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        phone=findViewById(R.id.phone1_patient);
        code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(phone.getText()))
        {
            phone.setError("Enter  the phone number");
            progressBar.setVisibility(View.GONE);
        }
        phone1=phone.getText().toString();
        phonenumber = "+" + code +phone1;
        if(phone1.length()<10)
        {
            phone.setError("Enter  the valid phone number");
            progressBar.setVisibility(View.GONE);
        }
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient").child(phonenumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("phone",phonenumber);
                    progressBar.setVisibility(View.GONE);
                    PatientLoginVerification patientLoginVerification=new PatientLoginVerification();
                    patientLoginVerification.setArguments(bundle);
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.patient_login_layout,patientLoginVerification);
                    fragmentTransaction.commit();
                }
                else
                {
                    Toast.makeText(PatientLoginActivity.this,"Please sign up first",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void verify_patient(View view) {
        mAuth=FirebaseAuth.getInstance();
        progressBar1=findViewById(R.id.progress_verify);
        progressBar1.setVisibility(View.VISIBLE);
        try {

            signInWithPhoneAuthCredential(PatientLoginVerification.credential);
        }
        catch(Exception e)
        {
            progressBar1.setVisibility(View.GONE);
            Toast.makeText(this,"Please wait for the code or try again",Toast.LENGTH_SHORT).show();

        }

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PatientLoginActivity.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity

                                    progressBar1.setVisibility(View.GONE);
                                    Intent intent=new Intent(PatientLoginActivity.this, Patient_Welcome_Activity.class);
                                    intent.putExtra("phone",phone1);
                                    startActivity(intent);


                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(PatientLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }
    public void LoginByPassword(View view) {
        Intent intent=new Intent(this, LoginByPasswordActivity.class);
        intent.putExtra("phone",phone1);
        intent.putExtra("who","patient");
        startActivity(intent);
    }
}
