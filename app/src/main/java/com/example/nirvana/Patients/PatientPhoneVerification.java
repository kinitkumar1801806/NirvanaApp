package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nirvana.R;
import com.example.nirvana.Model.details_patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PatientPhoneVerification extends AppCompatActivity {

    PhoneAuthCredential credential;
    private FirebaseAuth mAuth;
    Task<Void> databaseReference;
    EditText code;
    ProgressBar progressBar;
    String Uname,Email,Phone,Address,Gender,Fname,Lname,Password,Id,code1;
    String mVerificationId;
    ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_verify1);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Uname=arr.get(0);
        Email=arr.get(1);
        Phone=arr.get(2);
        Address=arr.get(3);
        Gender=arr.get(4);
        Fname=arr.get(5);
        Lname=arr.get(6);
        Password=arr.get(7);
        sendVerificationCode(Phone);
    }
    public void verify_patient(View view){
        credential = PhoneAuthProvider.getCredential(mVerificationId, code1);
        progressBar.setVisibility(View.VISIBLE);
        signInWithPhoneAuthCredential(credential);
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,                 //phoneNo that is given by user
                60,                             //Timeout Duration
                TimeUnit.SECONDS,                   //Unit of Timeout
                TaskExecutors.MAIN_THREAD,          //Work done on main Thread
                mCallbacks);                       // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    //Getting the code sent by SMS
                    code1 = phoneAuthCredential.getSmsCode();

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code1 != null) {
                        code=findViewById(R.id.code_patient);
                        code.setText(code1);
                        //verifying the code
                        verifyVerificationCode(code1);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(PatientPhoneVerification.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                //when the code is generated then this method will receive the code.
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);

                    //storing the verification id that is sent to the user
                    mVerificationId = s;
                }
            };

    private void verifyVerificationCode(String code) {
        //creating the credential
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            progressBar.setVisibility(View.VISIBLE);
            signInWithPhoneAuthCredential(credential);
        }
        catch(Exception e)
        {
            Toast.makeText(PatientPhoneVerification.this,"Please make sure that sim of entered number is available in your phone",Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PatientPhoneVerification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Id=mAuth.getCurrentUser().getUid();
                                    //verification successful we will start the profile activity
                                    details_patient details_patient=new details_patient(
                                            Email,
                                            Phone,
                                            Address,
                                            Gender,
                                            Fname,
                                            Lname,
                                            Password,
                                            Id,
                                            "None"
                                    );
                                    databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(Phone)
                                            .setValue(details_patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(PatientPhoneVerification.this,"Successfully signed up",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(PatientPhoneVerification.this, Patient_Welcome_Activity.class);
                                                    startActivity(intent);

                                                }
                                            });

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(PatientPhoneVerification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }
}
