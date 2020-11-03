package com.example.nirvana.Offices;

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

public class OfficeWelcomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    FirebaseAuth auth;
    private String phone;
    public TextView office_name,office_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_welcome);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout=findViewById(R.id.drawer);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        office_name=hView.findViewById(R.id.header_name);
        office_address=hView.findViewById(R.id.header_address);
        auth= FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=getIntent();
            phone=intent.getStringExtra("phone");
        }
        RetrieveOfficeDetails();
        Doctor_Home_Fragment doctor_home_fragment=new Doctor_Home_Fragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,doctor_home_fragment);
        fragmentTransaction.commit();
    }
    private void RetrieveOfficeDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Offices").child(phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                String name=hashMap.get("school_name").toString();//office uses school_detail model file
                String address=hashMap.get("address").toString();
                office_name.setText(name);
                office_address.setText(address);
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
            intent.putExtra("who","office");
            startActivity(intent);
        }
        else if(id==R.id.my_doctors)
        {

        }
        else if(id==R.id.patient_managementSystem)
        {

        }
        else if(id==R.id.niri)
        {
            Intent intent=new Intent(this, Niri.class);
            startActivity(intent);
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