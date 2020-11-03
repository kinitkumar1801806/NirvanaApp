package com.example.nirvana.Schools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.nirvana.Adapter.ImageAdapter_Doctor;
import com.example.nirvana.Patients.Fix_Meeting_step2;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDoctorActivity extends AppCompatActivity {
    ArrayList<String> ImageArray;
    ArrayList<String> NameList;
    ArrayList<String> Email_List,Phone_List,arr,Expand_List,BioList;
    RecyclerView recyclerView;
    ImageAdapter_Doctor imageAdapter_doctor;
    ProgressDialog progressDialog;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NameList=new ArrayList<>();
        Email_List=new ArrayList<>();
        Phone_List=new ArrayList<>();
        arr=new ArrayList<>();
        BioList=new ArrayList<>();
        Expand_List=new ArrayList<>();
        ImageArray=new ArrayList<>();
        textview=findViewById(R.id.text_view);
        progressDialog =new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        retrieveData();
    }

    public void retrieveData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                    for(String key:hashMap.keySet()) {
                        Object data = hashMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        String fname=(String)userData.get("fname");
                        String lname=(String)userData.get("lname");
                        String Name=fname+" "+lname;
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("phone");
                        String link=(String)userData.get("link");
                        String affiliation=(String)userData.get("affiliation");
                        NameList.add(Name);
                        Email_List.add(email);
                        Phone_List.add(phone);
                        ImageArray.add(link);
                        BioList.add(affiliation);
                        Expand_List.add("0");
                        initRecyclerView();
                    }
                }
                else
                {
                    textview.setText("Sorry,We have no counsellor till now");
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void initRecyclerView()
    {
        imageAdapter_doctor= new ImageAdapter_Doctor(MyDoctorActivity.this, ImageArray, NameList,Email_List,Expand_List,BioList);
        recyclerView.setAdapter(imageAdapter_doctor);
        imageAdapter_doctor.setOnItemClickListener(new ImageAdapter_Doctor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String phone=Phone_List.get(position);
                String name=NameList.get(position);
                String email=Email_List.get(position);
                arr.add(0,name);
                arr.add(1,phone);
                arr.add(2,email);
                arr.add(3,BioList.get(position));
                arr.add(4,ImageArray.get(position));
                Intent i = new Intent(getApplicationContext(), Fix_Meeting_step2.class);
                // Pass image index
                i.putStringArrayListExtra("arr",arr);

            }

            @Override
            public void onExpand(int position) {
                if(Expand_List.get(position).equals("1"))
                {
                    Expand_List.set(position,"0");
                }
                else
                {
                    Expand_List.set(position,"1");
                }
                imageAdapter_doctor.notifyItemChanged(position);
            }
        });
        progressDialog.dismiss();
    }
}
