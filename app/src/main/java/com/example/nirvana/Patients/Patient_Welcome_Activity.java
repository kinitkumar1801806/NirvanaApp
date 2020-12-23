package com.example.nirvana.Patients;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nirvana.Blogs.BlogActivity;
import com.example.nirvana.Doctors.Doctors_GridView;
import com.example.nirvana.GoalPlanning.GoalPlanning;
import com.example.nirvana.HomeFragment;
import com.example.nirvana.MainActivity;
import com.example.nirvana.Niri;
import com.example.nirvana.ProfileActivity;
import com.example.nirvana.R;
import com.example.nirvana.Call.SinchService;
import com.example.nirvana.YogaTutorials.YogaVideosActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Patient_Welcome_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    public TextView patient_name,patient_address;
    FirebaseAuth auth;
    ImageView Niri,Blogs,Home,Goal_Planning,Profile,p_profile;
    private String Id,phone,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__welcome_);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.patient_drawer);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        patient_name=hView.findViewById(R.id.header_name);
        patient_address=hView.findViewById(R.id.header_address);
        p_profile=hView.findViewById(R.id.doctor_image);
        Home=findViewById(R.id.home);
        Blogs=findViewById(R.id.blogs);
        Niri=findViewById(R.id.niri);
        Goal_Planning=findViewById(R.id.goal_planning);
        Profile=findViewById(R.id.profile);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            Id=intent.getStringExtra("Id");
            phone=intent.getStringExtra("phone");
            Intent intent1=new Intent(this, SinchService.class);
            intent1.putExtra("phone",phone);
            startService(intent1);
        }
        RetrievePatientDetails();
        PatientHomeFragment homeFragment=new PatientHomeFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
        fragmentTransaction.commit();
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientHomeFragment homeFragment=new PatientHomeFragment();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
                fragmentTransaction.commit();
            }
        });
        Blogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Patient_Welcome_Activity.this, BlogActivity.class);
                intent.putExtra("Id",Id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Niri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Patient_Welcome_Activity.this, Niri.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Goal_Planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Patient_Welcome_Activity.this,GoalPlanning.class);
                intent.putExtra("Id",Id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Patient_Welcome_Activity.this, ProfileActivity.class);
                intent.putExtra("Id",Id);
                intent.putExtra("who","patient");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
    }
    private void RetrievePatientDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                String fname=hashMap.get("fname").toString();
                String lname=hashMap.get("lname").toString();
                String address=hashMap.get("address").toString();
                link=hashMap.get("link").toString();
                if(!link.equals("None"))
                {
                    Glide.with(Patient_Welcome_Activity.this).load(link).into(Profile);
                    Glide.with(Patient_Welcome_Activity.this).load(link).into(p_profile);
                }
                patient_name.setText("Mr."+fname+" "+lname);
                patient_address.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.doctor_consultation)
        {
            HomeFragment homeFragment=new HomeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
            fragmentTransaction.commit();
        }
        else if(id==R.id.book_appointment)
        {
            ECounselFragment eCounselFragment=new ECounselFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,eCounselFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if(id==R.id.yoga_tutorial)
        {
            Intent intent=new Intent(this,YogaVideosActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.meditation_tutorial)
        {

        }
        else if(id==R.id.product)
        {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fix_meeting(View view) {
        Intent intent=new Intent(this, Doctors_GridView.class);
        startActivity(intent);
    }

    public void meeting_already_fixed(View view) {
        Intent intent=new Intent(this, Meeting_Already_Fixed_step1.class);
        intent.putExtra("Id",Id);
        startActivity(intent);
    }

}
