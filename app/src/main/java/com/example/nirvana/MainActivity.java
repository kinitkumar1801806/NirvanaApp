package com.example.nirvana;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nirvana.Doctors.DoctorLoginActivity;
import com.example.nirvana.Doctors.DoctorSignupActivity;
import com.example.nirvana.Organisations.OrganisationActivity;
import com.example.nirvana.Organisations.rganisationLoginActivity;
import com.example.nirvana.Patients.PatientLoginActivity;
import com.example.nirvana.Patients.PatientSignupActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private int MULTIPLE_PERMISSION = 1;
    FirebaseAuth auth;
    private String Id;
    String[] permission={Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.BLUETOOTH,Manifest.permission.ACCESS_WIFI_STATE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)+ ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.BLUETOOTH)+ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_WIFI_STATE)== PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPhonePermission();
        }
             Toolbar toolbar=findViewById(R.id.toolBar);
             setSupportActionBar(toolbar);
             mDrawerLayout=findViewById(R.id.drawer_layout);
             mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
             mDrawerLayout.addDrawerListener(mDrawerToggle);
             mDrawerToggle.syncState();
             //Navigation View
             NavigationView navigationView=findViewById(R.id.nav_view);
             navigationView.setNavigationItemSelectedListener(this);
             //adding home fragment



             HomeFragment homeFragment=new HomeFragment();
             FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
             fragmentTransaction.commit();


    }
    private void requestPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_NETWORK_STATE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BLUETOOTH)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_WIFI_STATE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    permission, MULTIPLE_PERMISSION);
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    permission,MULTIPLE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSION)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.nav_login)
        {
            LoginFragment loginFragment=new LoginFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,loginFragment,"Login");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if(id==R.id.nav_home)
        {
            HomeFragment homeFragment=new HomeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,homeFragment,"Home");
            fragmentTransaction.commit();
        }
        else if(id==R.id.nav_signup)
        {

            SignupFragment signupFragment=new SignupFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,signupFragment,"SignUp");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id==R.id.nav_about)
        {
            AboutFragment aboutFragment=new AboutFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,aboutFragment,"About Us");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id==R.id.nav_support)
        {
            Support supportFragment=new Support();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,supportFragment,"Support");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id==R.id.nav_what_we_do)
        {
            WhatToDo whatToDo=new WhatToDo();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,whatToDo,"What we offer");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if(id==R.id.nav_what_we_offer)
        {
            WhatWeOffer whatWeOffer=new WhatWeOffer();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,whatWeOffer,"What we offer");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
       mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
                super.onBackPressed();
            }
    }


    public void doctor_register(View view) {
        Intent intent =new Intent(this, DoctorSignupActivity.class);
        startActivity(intent);
    }

    public void patient_register(View view) {
        Intent intent =new Intent(this, PatientSignupActivity.class);
        startActivity(intent);
    }

    public void doctor_signin(View view) {
        Intent intent =new Intent(this, DoctorLoginActivity.class);
        startActivity(intent);
    }

    public void patient_signin(View view) {
        Intent intent =new Intent(this, PatientLoginActivity.class);
        startActivity(intent);
    }

    public void organisation_register(View view) {
        Intent intent=new Intent(this, OrganisationActivity.class);
        startActivity(intent);
    }

    public void organisation_signin(View view) {

        Intent intent=new Intent(this, rganisationLoginActivity.class);
        startActivity(intent);
    }
}
