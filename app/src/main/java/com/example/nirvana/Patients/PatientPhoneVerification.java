package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.nirvana.R;
import com.example.nirvana.Model.details_patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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
    String Age,Email,Phone,Address,Gender,Fname,Lname,Password,Id,code1;
    String mVerificationId;
    ArrayList<String> arr;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_verify1);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Age= arr.get(0);
        Email= arr.get(1);
        Phone= arr.get(2);
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

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
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
            verify=findViewById(R.id.patient_verify);
            verify.setEnabled(false);
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            progressBar.setVisibility(View.VISIBLE);
            signInWithPhoneAuthCredential(credential);
        }
        catch(Exception e)
        {
            verify.setEnabled(true);
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
                                            "None",
                                            Age,
                                            Id
                                    );
                                    databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(Id)
                                            .setValue(details_patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    AuthCredential credential1 = EmailAuthProvider.getCredential(Email, Password);
                                                    mAuth.getCurrentUser().linkWithCredential(credential1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                        }
                                                    });
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(PatientPhoneVerification.this,"Successfully signed up",Toast.LENGTH_SHORT).show();
                                                    BackgroundMail.newBuilder(PatientPhoneVerification.this)
                                                            .withUsername("nirvana.ieee.01@gmail.com")
                                                            .withPassword("nirvana_IEEE")
                                                            .withMailto(Email)
                                                            .withType(BackgroundMail.TYPE_HTML)
                                                            .withSubject("Welcome to Nirvana")
                                                            .withBody("<!DOCTYPE html>\n" +
                                                                    "<html lang=\"en\">\n" +
                                                                    "<head>\n" +
                                                                    "    <meta charset=\"UTF-8\">\n" +
                                                                    "    <title>EMAIL</title>\n" +
                                                                    "    <style>\n" +
                                                                    "        .container{\n" +
                                                                    "            margin-left: 10%;\n" +
                                                                    "            margin-right: 10%;\n" +
                                                                    "            word-wrap: break-word ;\n" +
                                                                    "        }\n" +
                                                                    "        .head{\n" +
                                                                    "            width: 100%;\n" +
                                                                    "            height: 15%;\n" +
                                                                    "\n" +
                                                                    "\n" +
                                                                    "        }\n" +
                                                                    "        .button{\n" +
                                                                    "            width: 20%;\n" +
                                                                    "            height: 8vh;\n" +
                                                                    "            border-radius: 8px;\n" +
                                                                    "            margin: auto;\n" +
                                                                    "            background-color: green;\n" +
                                                                    "            font-size: large;\n" +
                                                                    "            text-align: center;\n" +
                                                                    "            text-decoration: none;\n" +
                                                                    "            padding: 15px 30px;\n" +
                                                                    "            color: white;\n" +
                                                                    "        }\n" +
                                                                    "        .button:hover {\n" +
                                                                    "        \tbackground-color: darkgreen;\n" +
                                                                    "        }\n" +
                                                                    "\n" +
                                                                    "        .social{\n" +
                                                                    "            width: 4%;\n" +
                                                                    "            height: 4%;\n" +
                                                                    "            margin-right:2%;\n" +
                                                                    "        }\n" +
                                                                    "        .logo{\n" +
                                                                    "        \tposition: absolute;\n" +
                                                                    "        \tright: 10px;\n" +
                                                                    "        \theight: 100%;\n" +
                                                                    "        \ttop: 0;\n" +
                                                                    "\t\t}\n" +
                                                                    "        div{\n" +
                                                                    "            max-width: inherit;\n" +
                                                                    "        }\n" +
                                                                    "\t\t.btn {\n" +
                                                                    "        \ttext-align: center;\n" +
                                                                    "        \tmargin-top: 15px;\n" +
                                                                    "        \tmargin-bottom: 15px;\n" +
                                                                    "        }\n" +
                                                                    "           \n" +
                                                                    "        footer{\n" +
                                                                    "            background-color: #90EE90;\n" +
                                                                    "            width: 100%;\n" +
                                                                    "            padding: 14px 1px;\n" +
                                                                    "        }\n" +
                                                                    "        .link{\n" +
                                                                    "            width: 40%;\n" +
                                                                    "            height: 8vh;\n" +
                                                                    "            margin-right: 25%;\n" +
                                                                    "        }\n" +
                                                                    "        @media only screen and (max-width: 600px) {\n" +
                                                                    "            .button{\n" +
                                                                    "                font-size: medium;\n" +
                                                                    "                width: 90%;\n" +
                                                                    "                text-align: center;\n" +
                                                                    "            }\n" +
                                                                    "        }\n" +
                                                                    "  \n" +
                                                                    "    </style>\n" +
                                                                    "</head>\n" +
                                                                    "<body>\n" +
                                                                    "\t<div class=\"container\">\n" +
                                                                    "    <img class=\"head\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/todogif.png\" alt=\"\" srcset=\"\">\n" +
                                                                    "    <div>\n" +
                                                                    "        <h1>Hello ,</h1><br><h3>WELCOME to NIRVANA. <br> You've successfully registerd with us as a client, NIRVANA is your mental wellness spa, <br> The simple and secure solution to your mental health<br>Explor with US!</h3>\n" +
                                                                    "    <br><div class=\"btn\"><a href=\"\" class=\"button\">VISIT WEBSITE</a></div><br>\n" +
                                                                    "    <h4>We hope you will have a good time with us <br>Thanks <br>Team NIRVANA</h4>\n" +
                                                                    "\t</div>\n" +
                                                                    "</div>\n" +
                                                                    "    <footer>\n" +
                                                                    "    \t<div class=\"container\" style=\"position: relative;\">\n" +
                                                                    "        <h4>Reach out to us at</h4> <br><a href=\"\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/instagramicon.png\" alt=\"\" srcset=\"\"></a><a href=\"\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/facebook_icon.png\" alt=\"\" srcset=\"\"></a><a href=\"\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/email.png\" alt=\"\" srcset=\"\"></a><a href=\"\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/calender.png\" alt=\"\" srcset=\"\"></a>\n" +
                                                                    "        </div>\n" +
                                                                    "    </footer>\n" +
                                                                    "</body>\n" +
                                                                    "</html>")
                                                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    //do some magic
                                                                }
                                                            })
                                                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                                                @Override
                                                                public void onFail() {
                                                                    //do some magic
                                                                }
                                                            })
                                                            .send();
                                                    Intent intent=new Intent(PatientPhoneVerification.this, Patient_Welcome_Activity.class);
                                                    intent.putExtra("Id",Id);
                                                    intent.putExtra("phone",Phone);
                                                    startActivity(intent);
                                                    PatientPhoneVerification.this.finish();

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
