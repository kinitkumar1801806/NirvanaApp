package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nirvana.Blogs.BlogActivity;
import com.example.nirvana.MainActivity;
import com.example.nirvana.MusicPlayer.NirvanaAudioPlayer;
import com.example.nirvana.Patients.PatientQueue;
import com.example.nirvana.ProfileActivity;
import com.example.nirvana.R;
import com.example.nirvana.Call.SinchService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Doctor_Welcome_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth auth;
    public TextView doctor_name,doctor_address;
    private String phone,Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__welcome_);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.doctor_drawer);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        auth=FirebaseAuth.getInstance();
        doctor_name=hView.findViewById(R.id.header_name);
        doctor_address=hView.findViewById(R.id.header_address);
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            phone=intent.getStringExtra("phone");
            Id=intent.getStringExtra("Id");
            Intent intent1=new Intent(this, SinchService.class);
            intent1.putExtra("phone",phone);
            startService(intent1);
        }
        RetrieveDoctorDetails();
        Doctor_Home_Fragment doctor_home_fragment=new Doctor_Home_Fragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,doctor_home_fragment);
        fragmentTransaction.commit();
    }

    private void RetrieveDoctorDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                String fname=hashMap.get("fname").toString();
                String lname=hashMap.get("lname").toString();
                String address=hashMap.get("address").toString();
                doctor_name.setText("Mr."+fname+" "+lname);
                doctor_address.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.welcome_doctor_schedule)
        {
            Intent intent=new Intent(this, PatientQueue.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        else if(id==R.id.welcome_doctor_profile)
        {
            Intent intent=new Intent(this, ProfileActivity.class);
            intent.putExtra("phone",phone);
            intent.putExtra("who","doctor");
            startActivity(intent);
        }
        else if(id==R.id.logout_doctor)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.welcome_doctor_blogs)
        {
            Intent intent=new Intent(this, BlogActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
        else if(id==R.id.doctor_product)
        {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
