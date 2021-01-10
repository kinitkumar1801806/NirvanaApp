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
import android.widget.Toast;

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

public class Doctors_GridView extends AppCompatActivity {
    ArrayList<String> ImageArray;
    ArrayList<String> NameList;
    String Id,Id1;
    ArrayList<String> Username_List,Phone_List,arr,Expand_List,BioList,Id_List;
    RecyclerView recyclerView;
    ImageAdapter_Doctor imageAdapter_doctor;
    ProgressDialog progressDialog;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors__grid_view);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        Id1=intent.getStringExtra("Id");
        NameList=new ArrayList<>();
        Username_List=new ArrayList<>();
        Phone_List=new ArrayList<>();
        arr=new ArrayList<>();
        BioList=new ArrayList<>();
        Expand_List=new ArrayList<>();
        ImageArray=new ArrayList<>();
        Id_List=new ArrayList<>();
        textview=findViewById(R.id.text_view);
        progressDialog =new ProgressDialog(Doctors_GridView.this);
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
                    String username=(String)userData.get("email");//username removed,now we will be using email in placxe of username
                    String phone=(String)userData.get("phone");
                    String Id=(String)userData.get("Id");
                    String link=(String)userData.get("link");
                    String affiliation=(String)userData.get("affiliation");
                    NameList.add(Name);
                    Username_List.add(username);
                    Phone_List.add(phone);
                    ImageArray.add(link);
                    BioList.add(affiliation);
                    Expand_List.add("0");
                    Id_List.add(Id);
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
    imageAdapter_doctor= new ImageAdapter_Doctor(Doctors_GridView.this, ImageArray, NameList,Username_List,Expand_List,BioList);
    recyclerView.setAdapter(imageAdapter_doctor);
    imageAdapter_doctor.setOnItemClickListener(new ImageAdapter_Doctor.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String phone=Phone_List.get(position);
            String name=NameList.get(position);
            String username=Username_List.get(position);
            String Id=Id_List.get(position);
            arr.add(0,name);
            arr.add(1,phone);
            arr.add(2,username);
            arr.add(3,BioList.get(position));
            arr.add(4,ImageArray.get(position));
            arr.add(5,Id);
            Intent i = new Intent(getApplicationContext(), Fix_Meeting_step2.class);
            // Pass image index
            i.putStringArrayListExtra("arr",arr);
            i.putExtra("Id1",Id1);
            startActivity(i);
            overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
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
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}
