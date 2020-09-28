package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class Not_Fixed_Meetings extends AppCompatActivity {
    ArrayList<String> NameList;
    ArrayList<String> Username_List,Phone_List,date1,Patient_Phone,time,Image_Array;
    GridView androidGridView;
    ProgressDialog progressDialog;
    private String phone,username,link;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not__fixed__meetings);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NameList=new ArrayList<String>();
        Username_List=new ArrayList<String>();
        Phone_List=new ArrayList<String>();
        Patient_Phone=new ArrayList<String>();
        time=new ArrayList<String>();
        date1=new ArrayList<String>();
        Image_Array=new ArrayList<String>();
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        Not_Fixed_Meetings.AsyncTaskRunner runner=new Not_Fixed_Meetings.AsyncTaskRunner();
        runner.execute();
    }

    public void expand_information(View view) {

    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            System.out.println(phone);
            DatabaseReference databaseReference=firebaseDatabase.getReference("Doctor_Meetings").child("Not_Fixed_Meetings").child(phone);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        for (String key : hashMap.keySet()) {
                            Object data = hashMap.get(key);
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String Name = (String) userData.get("p_name");
                            String problem = (String) userData.get("p_problem");
                            String Phone = (String) userData.get("d_phone");
                            String date = (String) userData.get("date");
                            String phone1=(String)userData.get("p_phone");
                            String submission_date = (String) userData.get("submission_date");
                            String time1=(String)userData.get("submission_time");
                            username=(String)userData.get("d_username");
                            link=(String) userData.get("link");
                            if (date.equals("Not fixed")) {
                                NameList.add(i,Name);
                                Username_List.add(i,username);
                                Phone_List.add(i,Phone);
                                Patient_Phone.add(i,phone1);
                                date1.add(i,submission_date);
                                time.add(i,time1);
                                Image_Array.add(i,link);
                                i++;
                            }
                        }
                    }
                    if(NameList.size()==0)
                    {

                        TextView textView = findViewById(R.id.text_view);
                        textView.setText("You have no unfixed meetings");

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
            progressDialog =new ProgressDialog(Not_Fixed_Meetings.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


        @Override
        protected void onPostExecute(String s) {
            Not_Fixed_Meeting_Adapter adapterViewAndroid = new Not_Fixed_Meeting_Adapter(Not_Fixed_Meetings.this, NameList,Username_List,date1,time,Image_Array);
                androidGridView=(GridView)findViewById(R.id.gridview);
                androidGridView.setAdapter(adapterViewAndroid);
                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                        // Send intent to SingleViewActivity
                        String phone=Phone_List.get(position);
                        String phone1=Patient_Phone.get(position);
                        Intent i = new Intent(getApplicationContext(), Not_Fixed_Meeting_step2.class);
                        // Pass image index
                        i.putExtra("phone",phone);
                        i.putExtra("phone1",phone1);
                        startActivity(i);
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
