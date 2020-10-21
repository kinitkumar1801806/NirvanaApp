package com.example.nirvana.Doctors;

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

import com.example.nirvana.Adapter.Not_Fixed_Meeting_Adapter;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
public class PendingMeetingsStep1 extends AppCompatActivity {
    ArrayList<String> NameList;
    ArrayList<String> Username_List,Phone_List,date1,Patient_Phone,time,Image_Array,Expand_List,Problem_List;
    RecyclerView recyclerView;
    Not_Fixed_Meeting_Adapter not_fixed_meeting_adapter;
    ProgressDialog progressDialog;
    private String phone,username,link;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not__fixed__meetings);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NameList=new ArrayList<>();
        Username_List=new ArrayList<>();
        Phone_List=new ArrayList<>();
        Patient_Phone=new ArrayList<>();
        time=new ArrayList<>();
        date1=new ArrayList<>();
        Image_Array=new ArrayList<>();
        Problem_List=new ArrayList<>();
        Expand_List=new ArrayList<>();
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
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
    DatabaseReference databaseReference=firebaseDatabase.getReference("Doctor_Meetings").child("Fixed_Meetings").child(phone
    );
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : hashMap.keySet()) {
                    Object data = hashMap.get(key);
                    HashMap<String, Object> userData = (HashMap<String, Object>) data;
                    String Name = (String) userData.get("p_name");
                    String Phone = (String) userData.get("d_phone");
                    String date = (String) userData.get("date");
                    String phone1=(String)userData.get("p_phone");
                    String problem=(String)userData.get("p_problem");
                    String submission_date = (String) userData.get("submission_date");
                    String time1=(String)userData.get("submission_time");
                    username=(String)userData.get("d_username");
                    link=(String) userData.get("link");
                    NameList.add(i,Name);
                    Username_List.add(i,username);
                    Phone_List.add(i,Phone);
                    Patient_Phone.add(i,phone1);
                    date1.add(i,submission_date);
                    Problem_List.add(problem);
                    Expand_List.add("0");
                    time.add(i,time1);
                    Image_Array.add(i,link);
                    i++;
                    initRecyclerView();
                }
            }
            if(NameList.size()==0)
            {
                TextView textView = findViewById(R.id.text_view);
                textView.setText("You have no scheduled meetings");
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
        not_fixed_meeting_adapter = new Not_Fixed_Meeting_Adapter(PendingMeetingsStep1.this,Image_Array,NameList,Username_List,Expand_List,Problem_List,date1,time);
        recyclerView.setAdapter(not_fixed_meeting_adapter);
        not_fixed_meeting_adapter.setOnItemClickListener(new Not_Fixed_Meeting_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String phone=Phone_List.get(position);
                String phone1=Patient_Phone.get(position);
                Intent i = new Intent(getApplicationContext(), PendingMeetingsStep2.class);
                // Pass image index
                i.putExtra("phone",phone);
                i.putExtra("phone1",phone1);
                startActivity(i);
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
