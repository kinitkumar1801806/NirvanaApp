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

import com.example.nirvana.Blogs.BlogActivity;
import com.example.nirvana.Doctors.Doctors_GridView;
import com.example.nirvana.GoalPlanning.GoalPlanning;
import com.example.nirvana.HomeFragment;
import com.example.nirvana.MainActivity;
import com.example.nirvana.Niri;
import com.example.nirvana.ProfileActivity;
import com.example.nirvana.R;
import com.example.nirvana.Call.SinchService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Patient_Welcome_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth auth;
    private String phone;
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
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            phone=intent.getStringExtra("phone");
            Intent intent1=new Intent(this, SinchService.class);
            intent1.putExtra("phone",phone);
            startService(intent1);
        }
        HomeFragment homeFragment=new HomeFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
        fragmentTransaction.commit();
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
        else if(id==R.id.blogs)
        {
           Intent intent=new Intent(this, BlogActivity.class);
           startActivity(intent);
        }
        else if(id==R.id.niri)
        {
            Intent intent=new Intent(this, Niri.class);
            startActivity(intent);
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

        }
        else if(id==R.id.meditation_tutorial)
        {

        }
        else if(id==R.id.niri)
        {
            Intent intent=new Intent(this,Niri.class);
            startActivity(intent);
        }
        else if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.patient_profile)
        {
            Intent intent=new Intent(this, ProfileActivity.class);
            intent.putExtra("phone",phone);
            intent.putExtra("who","patient");
            startActivity(intent);
        }
        else if(id==R.id.goal_planning)
        {
          Intent intent=new Intent(this,GoalPlanning.class);
          intent.putExtra("phone",phone);
          startActivity(intent);
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
        intent.putExtra("phone",phone);
        startActivity(intent);
    }
}
