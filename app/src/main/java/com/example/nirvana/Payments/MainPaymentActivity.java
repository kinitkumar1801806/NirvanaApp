package com.example.nirvana.Payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.nirvana.Doctors.Doctor_Meeting;
import com.example.nirvana.Patients.PatientPhoneVerification;
import com.example.nirvana.Patients.Patient_Meeting;
import com.example.nirvana.Patients.Patient_Welcome_Activity;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ArrayList<String> Patient_Detail,Doctor_Detail;
    public String name1,email1,p_name,problem1,doctor_name,time,date,currentTime,thisDate,link,bio,amount,Did,Pid,email,phone,link1;
    ProgressDialog progressDialog;
    public TextView Bio,Name,Problem,Phone,Amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_payment);
        Intent intent=getIntent();
        Patient_Detail=intent.getStringArrayListExtra("Patient_Detail");
        Doctor_Detail=intent.getStringArrayListExtra("Doctor_Detail");
        Pid=Patient_Detail.get(0);
        name1=Patient_Detail.get(1);
        problem1=Patient_Detail.get(2);
        doctor_name=Patient_Detail.get(3);
        time=Patient_Detail.get(4);
        date=Patient_Detail.get(5);
        thisDate=Patient_Detail.get(6);
        currentTime=Patient_Detail.get(7);
        link=Doctor_Detail.get(0);
        bio=Doctor_Detail.get(1);
        Did=Doctor_Detail.get(2);
        Bio=findViewById(R.id.bio);
        Problem=findViewById(R.id.problem);
        Name=findViewById(R.id.name);
        Amount=findViewById(R.id.amount);
        Phone=findViewById(R.id.phone);
        Bio.setText(bio);
        Problem.setText(problem1);
        Name.setText(doctor_name);
        amount=Doctor_Detail.get(3);
        Amount.setText("Rs. "+amount);
        retrieveData();
    }

    private void retrieveData() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient").child(Pid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                email=hashMap.get("email").toString();
                phone=hashMap.get("phone").toString();
                link1=hashMap.get("link").toString();
                p_name=hashMap.get("fname").toString()+hashMap.get("lname").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1=firebaseDatabase1.getReference().child("Doctors").child(Did);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                email1=hashMap.get("email").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        progressDialog =new ProgressDialog(MainPaymentActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        Patient_Meeting patient_meeting=new Patient_Meeting(
                name1,
                problem1,
                doctor_name,
                time,
                date,
                thisDate,
                bio,
                link,
                currentTime,
                Did,
                "0"
        );
        final Doctor_Meeting doctor_meeting=new Doctor_Meeting(
                name1,
                problem1,
                time,
                date,
                thisDate,
                currentTime,
                Pid,
                "0",
                link1
        );

        Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(Pid).child(Did).child(Patient_Detail.get(8))
                .setValue(patient_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Task<Void> databaseReference1 = FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child(Did).child(Pid).child(Patient_Detail.get(8))
                .setValue(doctor_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        progressDialog.dismiss();
        ArrayList<String> arr=new ArrayList<>();
        arr.add(thisDate);
        arr.add(date);
        arr.add(time);
        arr.add(amount);
        arr.add(doctor_name);
        sendEmail();
        Intent intent=new Intent(this,PaymentSuccessActivity.class);
        intent.putStringArrayListExtra("arr",arr);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
    }

    private void sendEmail() {
        BackgroundMail.newBuilder(MainPaymentActivity.this)
                .withUsername("nirvana.ieee.01@gmail.com")
                .withPassword("nirvana_IEEE")
                .withMailto(email)
                .withType(BackgroundMail.TYPE_HTML)
                .withSubject("Nirvana")
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
                        "        <h1>Hello "+p_name+",</h1><br><h3>WELCOME to NIRVANA. <br> You've successfully booked your session with DR. "+doctor_name+", a professional Therapist <br> On "+date+" at "+time+"<br>Problem: Depression and Anxiety.<br>You are now just one tap away from NIRVANA.</h3>\n" +
                        "    <br><div class=\"btn\"><a href=\"\" class=\"button\">SET REMINDER</a></div><br>\n" +
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
        BackgroundMail.newBuilder(MainPaymentActivity.this)
                .withUsername("nirvana.ieee.01@gmail.com")
                .withPassword("nirvana_IEEE")
                .withMailto(email1)
                .withType(BackgroundMail.TYPE_HTML)
                .withSubject("Nirvana")
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
                        "        <h1>Hello Dr. "+doctor_name+",</h1> <br> You have an appointment today with "+p_name+", Gender- Female, Age- 20 Years <br> On "+date+" at"+time+"<br>Problem: Depression and Anxiety.<br>You are now just one tap away from NIRVANA.</h3>\n" +
                        "    <br><div class=\"btn\"><a href=\"\" class=\"button\">SET REMINDER</a></div><br>\n" +
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
    }

    @Override
    public void onPaymentError(int i, String s) {
        Intent intent=new Intent(this,PaymentFailureActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
    }




    public void Proceed_For_Payment(View view) {
            Checkout.preload(getApplicationContext());
            Checkout checkout = new Checkout();

            /**
             * Set your logo here
             */
            checkout.setImage(R.mipmap.niri);

            /**
             * Reference to current activity
             */
            final Activity activity = this;

            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */

            try {
                int amt=Integer.parseInt(amount)*100;
                JSONObject options = new JSONObject();
                options.put("name", name1);
                options.put("description", "Reference No. #123456");
                options.put("image",""+R.drawable.green_logo);
                options.put("theme.color", "#339966");
                options.put("currency", "INR");
                options.put("amount", String.valueOf(amt));//pass amount in currency subunits
                JSONObject prefill=new JSONObject();
                prefill.put("email",email);
                prefill.put("contact",phone);
                options.put("prefill",prefill);
                checkout.open(activity, options);
            } catch(Exception e) {
                Toast.makeText(activity,"Error Occurred: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
    }
}