package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.nirvana.R;
import com.example.nirvana.Model.doctor_details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DoctorPhoneVerification extends AppCompatActivity {
    private PhoneAuthCredential credential;
    private FirebaseAuth mAuth;
    private Task<Void> databaseReference;
    private Uri Imagepath,Filepath;
    final String username ="nirvana.ieee.01@gmail.com";
    final String password ="nirvana_IEEE";
    private StorageReference mStorageRef,Ref;
    private EditText code;
    private ProgressBar progressBar;
    private String Email,Phone,Address,Gender,Fname,Lname,Password,Affiliation,LinkedIn,Year_Of_Practice,Place_Of_Practice,Id,link;
    private String mVerificationId,code1;
    private ArrayList<String> arr;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressBar=findViewById(R.id.progress_verify);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Email=arr.get(1);
        Phone=arr.get(2);
        Address=arr.get(3);
        Gender=arr.get(4);
        Fname=arr.get(5);
        Lname=arr.get(6);
        Password=arr.get(7);
        Affiliation=arr.get(8);
        LinkedIn=arr.get(9);
        Year_Of_Practice=arr.get(10);
        Place_Of_Practice=arr.get(11);
        Filepath=Uri.parse(intent.getStringExtra("Filepath"));
        Imagepath=Uri.parse(intent.getStringExtra("Imagepath"));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        sendVerificationCode(Phone);
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
                        code=findViewById(R.id.code_doctor);
                        code.setText(code1);
                        //verifying the code
                        verifyVerificationCode(code1);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(DoctorPhoneVerification.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
            verify=findViewById(R.id.doctor_verify);
            verify.setEnabled(false);
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            progressBar.setVisibility(View.VISIBLE);
            signInWithPhoneAuthCredential(credential);
        }
        catch(Exception e)
        {
            verify.setEnabled(true);
            Toast.makeText(DoctorPhoneVerification.this,"Please make sure that sim of entered number is available in your phone",Toast.LENGTH_SHORT).show();
        }
    }
    public void verify_doctor(View view) {
        credential = PhoneAuthProvider.getCredential(mVerificationId, code1);
        progressBar.setVisibility(View.VISIBLE);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(DoctorPhoneVerification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Id=mAuth.getCurrentUser().getUid();
                                    //verification successful we will start the profile activity

                                    StorageReference Ref1 =mStorageRef.child("Doctors").child(Id).child("Certificate");
                                    Ref1.putFile(Filepath);
                                    Ref =mStorageRef.child("Profile Images").child(Id);
                                    Ref.putFile(Imagepath)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // Get a URL to the uploaded content
                                                    Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            link=uri.toString();
                                                            PostDetails();
                                                        }
                                                    }) ;

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle unsuccessful uploads
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(DoctorPhoneVerification.this,"We are getting some issue.We will come back very soon",Toast.LENGTH_SHORT).show();
                                                    // ...
                                                }
                                            });

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(DoctorPhoneVerification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    private void PostDetails() {
        doctor_details doctor_details=new doctor_details(
                Email,
                Phone,
                Address,
                Gender,
                Fname,
                Lname,
                Password,
                Affiliation,
                Year_Of_Practice,
                Place_Of_Practice,
                LinkedIn,
                Id,
                link,
                "0",
                "5",
                "5",
                "18"
        );
        databaseReference= FirebaseDatabase.getInstance().getReference("Doctors").child(Id)
                .setValue(doctor_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AuthCredential credential1 = EmailAuthProvider.getCredential(Email, Password);
                        mAuth.getCurrentUser().linkWithCredential(credential1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        sendMail();
                        Toast.makeText(DoctorPhoneVerification.this,"Successfully signed up.Please check your email for your doctor id.",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(DoctorPhoneVerification.this, Doctor_Welcome_Activity.class);
                        intent.putExtra("phone",Phone);
                        intent.putExtra("Id",Id);
                        startActivity(intent);
                        DoctorPhoneVerification.this.finish();
                    }
                });
    }

    private void sendMail() {
        BackgroundMail.newBuilder(this)
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
                        "        <h1>Greetings!!</h1><br><h3>WELCOME to NIRVANA. <br> You've successfully registerd with us as and have joined our exuberant team of Practioners, <br> which includes doctors with specialisation under categories like Councellors, Psychiatrist, Therapist and Psycologist.<br>Explore with US!</h3><h2>Please fill your appointment charges and your area of Expertise so as to facilitate the meeting process</h2>\n" +
                        "    <br><div class=\"btn\"><a href=\"#\" class=\"button\">VISIT WEBSITE</a></div><br>\n" +
                        "    <h4>We hope you will have a good time with us <br>Thanks <br>Team NIRVANA</h4>\n" +
                        "\t</div>\n" +
                        "</div>\n" +
                        "    <footer>\n" +
                        "    \t<div class=\"container\" style=\"position: relative;\">\n" +
                        "        <h4>Reach out to us at</h4> <br><a href=\"#\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/instagramicon.png\" alt=\"\" srcset=\"\"></a><a href=\"#\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/facebook_icon.png\" alt=\"\" srcset=\"\"></a><a href=\"#\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/email.png\" alt=\"\" srcset=\"\"></a><a href=\"#\"><img class=\"social\" src=\"https://nirvana-logintolife.github.io/Nirvana-email/calender.png\" alt=\"\" srcset=\"\"></a>\n" +
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
    }
}
