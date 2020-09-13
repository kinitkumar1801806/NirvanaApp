package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Meeting_Already_Fixed_step1 extends AppCompatActivity {
    ArrayList<String> ImageArray;
    ArrayList<String> NameList;
    ArrayList<String> Username_List,Phone_List,arr,Date_List,Time_List;
    GridView androidGridView;
    ProgressDialog progressDialog;
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
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }
    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
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
                           String link=(String)userData.get("link");
                           ImageArray.add(link);
                           phone1=phone;
                           NameList.add(Name);
                           Username_List.add(username);
                           Phone_List.add(phone);
                           Date_List.add(date);
                           Time_List.add(time);
                       }
                   }
                   else
                   {
                       TextView textView=findViewById(R.id.text_view);
                       textView.setText("You have no meetings");
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
            progressDialog =new ProgressDialog(Meeting_Already_Fixed_step1.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s) {
               Meeting_Adapter adapterViewAndroid = new Meeting_Adapter(Meeting_Already_Fixed_step1.this, ImageArray, NameList,Username_List,Date_List,Time_List);
                androidGridView=(GridView)findViewById(R.id.gridview);
                androidGridView.setAdapter(adapterViewAndroid);
                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                        Intent intent=new Intent(Meeting_Already_Fixed_step1.this,Meeting_Alresdy_fixed_step2.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("phone1",phone1);
                        startActivity(intent);
                    }
                });
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}
