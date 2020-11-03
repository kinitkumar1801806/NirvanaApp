package com.example.nirvana.Schools;

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

import com.example.nirvana.Doctors.Doctor_Home_Fragment;
import com.example.nirvana.MainActivity;
import com.example.nirvana.Niri;
import com.example.nirvana.ProfileActivity;
import com.example.nirvana.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SchoolWelcomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth auth;
    public TextView school_name,school_address;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_welcome);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.drawer);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        school_name=hView.findViewById(R.id.header_name);
        school_address=hView.findViewById(R.id.header_address);
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            phone=intent.getStringExtra("phone");
        }
        RetrieveSchoolDetails();
        Doctor_Home_Fragment doctor_home_fragment=new Doctor_Home_Fragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,doctor_home_fragment);
        fragmentTransaction.commit();
    }

    private void RetrieveSchoolDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                String name=hashMap.get("school_name").toString();
                String address=hashMap.get("address").toString();
                school_name.setText(name);
                school_address.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.appointment)
        {

        }
        else if(id==R.id.profile)
        {
            Intent intent=new Intent(this, ProfileActivity.class);
            intent.putExtra("phone",phone);
            intent.putExtra("who","school");
            startActivity(intent);
        }
        else if(id==R.id.my_doctors)
        {
            Intent intent=new Intent(this, MyDoctorActivity.class);
            startActivity(intent);

        }
        else if(id==R.id.niri)
        {
            Intent intent=new Intent(this, Niri.class);
            startActivity(intent);
        }
        else if(id==R.id.patient_managementSystem)
        {

        }
        else if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}