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

import com.example.nirvana.Doctors.Doctor_Meeting;
import com.example.nirvana.Patients.Patient_Meeting;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ArrayList<String> Patient_Detail,Doctor_Detail;
    public String name1,age1,problem1,patient_phone,doctor_phone,username_doctor,patient_gender1,
    doctor_name,time,date,time1,date1,currentTime,thisDate,link,bio,link1,amount;
    ProgressDialog progressDialog;
    public TextView Bio,Name,Problem,Phone,Amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_payment);
        Intent intent=getIntent();
        Patient_Detail=intent.getStringArrayListExtra("Patient_Detail");
        Doctor_Detail=intent.getStringArrayListExtra("Doctor_Detail");
        name1=Patient_Detail.get(0);
        age1=Patient_Detail.get(1);
        problem1=Patient_Detail.get(2);
        patient_gender1=Patient_Detail.get(3);
        doctor_name=Patient_Detail.get(4);
        doctor_phone=Patient_Detail.get(5);
        time=Patient_Detail.get(6);
        date=Patient_Detail.get(7);
        thisDate=Patient_Detail.get(8);
        username_doctor=Patient_Detail.get(9);
        link=Patient_Detail.get(10);
        currentTime=Patient_Detail.get(11);
        patient_phone=Doctor_Detail.get(0);
        time1=Doctor_Detail.get(1);
        date1=Doctor_Detail.get(2);
        link1=Doctor_Detail.get(3);
        bio=Doctor_Detail.get(4);
        Bio=findViewById(R.id.bio);
        Problem=findViewById(R.id.problem);
        Name=findViewById(R.id.name);
        Amount=findViewById(R.id.amount);
        Phone=findViewById(R.id.phone);
        Bio.setText(bio);
        Problem.setText(problem1);
        Name.setText(doctor_name);
        amount="100";
        Amount.setText("Rs. "+amount);

    }

    @Override
    public void onPaymentSuccess(String s) {
        progressDialog =new ProgressDialog(MainPaymentActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        Patient_Meeting patient_meeting=new Patient_Meeting(
                name1,
                age1,
                problem1,
                patient_gender1,
                doctor_name,
                doctor_phone,
                time,
                date,
                thisDate,
                username_doctor,
                "None",
                currentTime
        );
        final Doctor_Meeting doctor_meeting=new Doctor_Meeting(
                name1,
                age1,
                problem1,
                patient_gender1,
                patient_phone,
                time1,
                currentTime,
                date1,
                thisDate,
                doctor_phone,
                username_doctor,
                link1,
                bio
        );

        Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(patient_phone).child(doctor_phone)
                .setValue(patient_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Task<Void> databaseReference1 = FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Not_Fixed_Meetings").child(doctor_phone).child(patient_phone)
                .setValue(doctor_meeting).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        progressDialog.dismiss();
        Toast.makeText(this, "Payment Successful "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failure", Toast.LENGTH_SHORT).show();
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
                prefill.put("email","kinitkumar1801@gmail.com");
                prefill.put("contact",patient_phone);
                options.put("prefill",prefill);
                checkout.open(activity, options);
            } catch(Exception e) {
                Toast.makeText(activity,"Error Occurred: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
    }
}