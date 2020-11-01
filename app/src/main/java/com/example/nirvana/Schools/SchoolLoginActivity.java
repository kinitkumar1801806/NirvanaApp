package com.example.nirvana.Schools;

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

public class SchoolLoginActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_school_login);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
       SchoolLoginFragment schoolLoginFragment=new SchoolLoginFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_school_login,schoolLoginFragment);
        fragmentTransaction.commit();
    }

    public void login_school(View view) {
        EditText phone;
        spinner = findViewById(R.id.spinnerCountries);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        phone=findViewById(R.id.phone1_school);
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
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(phonenumber);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();

                    Bundle bundle=new Bundle();
                    bundle.putString("phone",phonenumber);
                    SchoolLoginVerification schoolLoginVerification=new SchoolLoginVerification();
                    schoolLoginVerification.setArguments(bundle);
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_school_login,schoolLoginVerification);
                    fragmentTransaction.commit();

                }
                else
                {
                    Toast.makeText(SchoolLoginActivity.this,"This number does not attach with any account.",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void verify_school(View view) {
        mAuth=FirebaseAuth.getInstance();
        progressBar1=findViewById(R.id.progress_verify);
        progressBar1.setVisibility(View.VISIBLE);
        try {

            signInWithPhoneAuthCredential(SchoolLoginVerification.credential);
        }
        catch(Exception e)
        {
            progressBar1.setVisibility(View.GONE);
            Toast.makeText(this,"Please wait for the code or try again",Toast.LENGTH_SHORT).show();

        }

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SchoolLoginActivity.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity

                                    progressBar1.setVisibility(View.GONE);
                                    Intent intent=new Intent(SchoolLoginActivity.this, SchoolWelcomeActivity.class);
                                    intent.putExtra("phone",phone1);
                                    startActivity(intent);


                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(SchoolLoginActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    public void signup_school(View view) {
        SchoolSignupFragment schoolSignupFragment=new SchoolSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_school_login,schoolSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void LoginByPassword(View view) {
        Intent intent=new Intent(this, LoginByPasswordActivity.class);
        intent.putExtra("phone",phone1);
        intent.putExtra("who","school");
        startActivity(intent);
    }
}
