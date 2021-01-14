package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nirvana.Adapter.ImageAdapter_Doctor;
import com.example.nirvana.Adapter.Reviews_Adapter;
import com.example.nirvana.Blogs.DoctorViewActivity;
import com.example.nirvana.Doctors.Doctors_GridView;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Fix_Meeting_step2 extends AppCompatActivity {
    ProgressDialog progressDialog;
    private String phone,linkedIn,doctor_name,Id,doctor_Id;
    private TextView nameText,doctor_place,doctor_qualification;
    ArrayList<String> arr,PatientNameList,RatingList,ReviewList,DateList,TimeList;
    ImageView image;
    RecyclerView recyclerView;
    Reviews_Adapter reviews_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix__meeting_step2);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Id=intent.getStringExtra("Id1");
        phone=arr.get(1);
        doctor_Id=arr.get(5);
        doctor_name=arr.get(0);
        nameText=findViewById(R.id.doctor_name);
        doctor_place=findViewById(R.id.doctor_place);
        doctor_qualification=findViewById(R.id.doctor_qualification);
        image=findViewById(R.id.doctor_image);
        PatientNameList=new ArrayList<>();
        RatingList=new ArrayList<>();
        ReviewList=new ArrayList<>();
        DateList=new ArrayList<>();
        TimeList=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Fix_Meeting_step2.AsyncTaskRunner runner=new Fix_Meeting_step2.AsyncTaskRunner();
        runner.execute();
    }

    public void fix_the_meeting(View view) {
        Intent intent =new Intent(this, Fix_Meeting_step3.class);
        intent.putStringArrayListExtra("arr",arr);
        intent.putExtra("Id",Id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
    }

    public void ShowDoctorBlogs(View view) {
        Intent intent=new Intent(Fix_Meeting_step2.this, DoctorViewActivity.class);
        intent.putExtra("Id",Id);
        intent.putExtra("doctor_Id",doctor_Id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(doctor_Id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        String fname = (String) hashMap.get("fname");
                        String lname = (String) hashMap.get("lname");
                        String name = fname + " " + lname;
                        nameText.setText(name);
                        doctor_qualification.setText(arr.get(3));
                        String link=(String)hashMap.get("link");
                        Glide.with(Fix_Meeting_step2.this).load(link).into(image);
                        String address=(String)hashMap.get("address");
                        doctor_place.setText(address);
                        RetrieveRatings();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return "Finish";
        }
        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(Fix_Meeting_step2.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("Finish"))
        {
            progressDialog.dismiss();
         }
}

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


    }

    private void RetrieveRatings() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors_Reviews").child(doctor_Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    HashMap<String,Object> hashMap=(HashMap<String, Object>) dataSnapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        Object data=hashMap.get(key);
                        HashMap<String,Object> userdata=(HashMap<String,Object>)data;
                        String patient_name=(String)userdata.get("patient_name");
                        String ratings=(String)userdata.get("rating");
                        String reviews=(String)userdata.get("review");
                        String date=(String)userdata.get("date");
                        String time=(String)userdata.get("time");
                        PatientNameList.add(patient_name);
                        RatingList.add(ratings);
                        ReviewList.add(reviews);
                        DateList.add(date);
                        TimeList.add(time);
                        initRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initRecyclerView()
    {
        reviews_adapter =new Reviews_Adapter(Fix_Meeting_step2.this,PatientNameList,RatingList,ReviewList,DateList,TimeList);
        recyclerView.setAdapter(reviews_adapter);
        progressDialog.dismiss();
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}
