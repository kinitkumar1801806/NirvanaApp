package com.example.nirvana.Schools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nirvana.R;
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

public class SchoolPhoneVerification extends AppCompatActivity {

    private ArrayList<String> arr;
    private  String Name,Address,Email,Contact,Specific_need,Purpose,mVerificationId,Id,code1,password;
    private EditText code;
    private ProgressBar progressBar;
    private PhoneAuthCredential credential;
    private FirebaseAuth mAuth;
    private Task<Void> databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_phone_verification);
        progressBar=findViewById(R.id.progress_verify1);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Name=arr.get(0);
        mAuth=FirebaseAuth.getInstance();
        Address=arr.get(1);
        Email=arr.get(2);
        Specific_need=arr.get(3);
        Contact=arr.get(4);
        Purpose=arr.get(5);
        password=arr.get(6);
        sendVerificationCode(Contact);
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
                        code=findViewById(R.id.code_school);
                        code.setText(code1);
                        //verifying the code
                        verifyVerificationCode(code1);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(SchoolPhoneVerification.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SchoolPhoneVerification.this,"Please make sure that sim of entered number is available in your phone",Toast.LENGTH_SHORT).show();
        }
    }
    public void verify_school(View view) {
        credential = PhoneAuthProvider.getCredential(mVerificationId, code1);
        progressBar.setVisibility(View.VISIBLE);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SchoolPhoneVerification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Id=mAuth.getCurrentUser().getUid();
                                    //verification successful we will start the profile activity
                                    School_details school_details=new School_details(
                                            Name,
                                            Address,
                                            Email,
                                            Contact,
                                            Specific_need,
                                            Purpose,
                                            "None",
                                            Id,
                                            password
                                    );
                                    databaseReference= FirebaseDatabase.getInstance().getReference("Schools").child(Contact)
                                            .setValue(school_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(SchoolPhoneVerification.this,"Successfully signed up",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(SchoolPhoneVerification.this, SchoolWelcomeActivity.class);
                                                    intent.putExtra("phone",Contact);
                                                    startActivity(intent);
                                                }
                                            });

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(SchoolPhoneVerification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

}
