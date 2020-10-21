package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.nirvana.Adapter.Meeting_Adapter;
import com.example.nirvana.Adapter.Not_Fixed_Meeting_Adapter;
import com.example.nirvana.Doctors.Not_Fixed_Meeting_step2;
import com.example.nirvana.Doctors.Not_Fixed_Meetings;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Meeting_Already_Fixed_step1 extends AppCompatActivity {
    ArrayList<String> ImageArray;
    ArrayList<String> NameList;
    ArrayList<String> Username_List,Phone_List,arr,Date_List,Time_List,Expand_List,BioList;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Not_Fixed_Meeting_Adapter not_fixed_meeting_adapter;
    private String phone,phone1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__already__fixed_step1);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NameList=new ArrayList<String>();
        Username_List=new ArrayList<String>();
        Time_List=new ArrayList<String>();
        Phone_List=new ArrayList<String>();
        arr=new ArrayList<String>();
        Date_List=new ArrayList<String>();
        ImageArray=new ArrayList<String>();
        Expand_List=new ArrayList<>();
        BioList=new ArrayList<>();
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        progressDialog =new ProgressDialog(Meeting_Already_Fixed_step1.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        retrieveData();
    }

public void retrieveData()
{
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference("Patient_Meetings").child(phone);
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                for(String key:hashMap.keySet()) {
                    Object data = hashMap.get(key);
                    HashMap<String, Object> userData = (HashMap<String, Object>) data;
                    String Name=(String)userData.get("d_name");
                    String username=(String)userData.get("d_username");
                    String phone=(String)userData.get("d_phone");
                    String date=(String)userData.get("submission_date");
                    String time=(String)userData.get("submission_time");
                    String bio=(String)userData.get("bio");
                    String link=(String)userData.get("link");
                    ImageArray.add(link);
                    phone1=phone;
                    NameList.add(Name);
                    Expand_List.add("0");
                    BioList.add(bio);
                    Username_List.add(username);
                    Phone_List.add(phone);
                    Date_List.add(date);
                    Time_List.add(time);
                    initRecyclerView();
                }
            }
            else
            {
                TextView textView=findViewById(R.id.text_view);
                textView.setText("You have no meetings");
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
    not_fixed_meeting_adapter = new Not_Fixed_Meeting_Adapter(Meeting_Already_Fixed_step1.this,ImageArray,NameList,Username_List,Expand_List,BioList,Date_List,Time_List);
    recyclerView.setAdapter(not_fixed_meeting_adapter);
    not_fixed_meeting_adapter.setOnItemClickListener(new Not_Fixed_Meeting_Adapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent=new Intent(Meeting_Already_Fixed_step1.this, Meeting_Alresdy_fixed_step2.class);
            intent.putExtra("phone",phone);
            intent.putExtra("phone1",phone1);
            startActivity(intent);
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
            not_fixed_meeting_adapter.notifyItemChanged(position);
        }
    });
    progressDialog.dismiss();
}
}
