package com.example.nirvana.Clinics;

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

import java.util.HashMap;

public class ClinicLoginActivity extends AppCompatActivity {
    private Spinner spinner;
    ProgressBar progressBar1;
    FirebaseAuth mAuth;
    String phone1;
    ProgressBar progressBar;
    String code,Id1;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_login);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ClinicLoginFragment clinicLoginFragment=new ClinicLoginFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_clinic_login,clinicLoginFragment);
        fragmentTransaction.commit();
    }
    public void signup_clinic(View view) {
        ClinicSignupFragment clinicSignupFragment=new ClinicSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_clinic_login,clinicSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void login_clinic(View view) {
        EditText phone;
        spinner = findViewById(R.id.spinnerCountries);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        phone=findViewById(R.id.phone1_clinic);
        code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(phone.getText()))
        {
            phone.setError("Enter the phone number");
            progressBar.setVisibility(View.GONE);
        }
        phone1=phone.getText().toString();
        phonenumber = "+" + code +phone1;
        if(phone1.length()<10)
        {
            phone.setError("Please enter  the valid phone number");
            progressBar.setVisibility(View.GONE);
        }
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Clinics").child(phonenumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();

                    Bundle bundle=new Bundle();
                    bundle.putString("phone",phonenumber);
                    ClinicLoginVerification clinicLoginVerification=new ClinicLoginVerification();
                    clinicLoginVerification.setArguments(bundle);
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_clinic_login,clinicLoginVerification);
                    fragmentTransaction.commit();

                }
                else
                {
                    Toast.makeText(ClinicLoginActivity.this,"This number does not attach with any account.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void verify_clinic(View view) {
        mAuth=FirebaseAuth.getInstance();
        progressBar1=findViewById(R.id.progress_verify);
        progressBar1.setVisibility(View.VISIBLE);
        try {

            signInWithPhoneAuthCredential(ClinicLoginVerification.credential);
        }
        catch(Exception e)
        {
            progressBar1.setVisibility(View.GONE);
            Toast.makeText(this,"Please wait for the code or try again",Toast.LENGTH_SHORT).show();

        }

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ClinicLoginActivity.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity

                                    progressBar1.setVisibility(View.GONE);
                                    Intent intent=new Intent(ClinicLoginActivity.this, ClinicWelcomeActivity.class);
                                    intent.putExtra("phone",phone1);
                                    startActivity(intent);


                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(ClinicLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void LoginByPassword(View view) {
        Intent intent=new Intent(this, LoginByPasswordActivity.class);
        intent.putExtra("phone",phone1);
        intent.putExtra("who","clinic");
        startActivity(intent);
    }
}
