package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
public String Phone,Who;
public ImageView image;
public TextView Email,Mobile,Adobe_App,Adobe_Xd,WHO,Address;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent =getIntent();
        Phone=intent.getStringExtra("phone");
        Who=intent.getStringExtra("who");
        Email=findViewById(R.id.email);
        Mobile=findViewById(R.id.mobile);
        Adobe_App=findViewById(R.id.adope_app);
        Adobe_Xd=findViewById(R.id.adobe_xd);
        WHO=findViewById(R.id.who);
        image=findViewById(R.id.imageView5);
        Address=findViewById(R.id.address);
       AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.setting:
                Toast.makeText(this,"Setting option selected",Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_profile:
                Toast.makeText(this,"Edit Profile option selected",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            if(Who.equals("doctor"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                            String fname=(String)userData.get("fname");
                            String lname=(String)userData.get("lname");
                            String Name=fname+" "+lname;
                            String email=(String)userData.get("email");
                            String phone=(String)userData.get("phone");
                            String link=(String)userData.get("link");
                            String address=(String)userData.get("address");
                            WHO.setText(Name);
                            Email.setText(email);
                            Mobile.setText(phone);
                            Address.setText(address);
                           if(!link.equals("None"))
                               Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(Who.equals("patient"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String fname=(String)userData.get("fname");
                        String lname=(String)userData.get("lname");
                        String Name=fname+" "+lname;
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("phone");
                        String link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None"))
                            Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(Who.equals("clinic"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Clinics").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String Name=(String)userData.get("hospital_name");
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("contact");
                        String link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None"))
                            Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(Who.equals("hospital"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Hospitals").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String Name=(String)userData.get("hospital_name");
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("contact");
                        String link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None"))
                            Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(Who.equals("school"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String Name=(String)userData.get("school_name");
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("contact");
                        String link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None"))
                            Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if(Who.equals("office"))
            {
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Offices").child(Phone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String Name=(String)userData.get("school_name");
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("contact");
                        String link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None"))
                            Glide.with(ProfileActivity.this).load(link).into(image);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            return "Finish";
        }

        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(ProfileActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}