package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
public String Id,Who,link;
public ImageView image;
DrawerLayout navDrawer;
public TextView Email,Mobile,Adobe_App,Adobe_Xd,WHO,Address;
public Integer Launch_Activity=123;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent =getIntent();
        Id=intent.getStringExtra("Id");
        Who=intent.getStringExtra("who");
        Email=findViewById(R.id.email);
        Mobile=findViewById(R.id.mobile);
        WHO=findViewById(R.id.who);
        image=findViewById(R.id.imageView5);
        Address=findViewById(R.id.address);
        navDrawer = findViewById(R.id.drawer_layout);
        ChangeMenuItemColor();
        AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }

    private void ChangeMenuItemColor() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu myMenu = navigationView.getMenu();
        MenuItem data_and_history= myMenu.findItem(R.id.data_and_history);
        MenuItem login_security=myMenu.findItem(R.id.login_security);
        MenuItem about=myMenu.findItem(R.id.about);
        MenuItem notifications=myMenu.findItem(R.id.notification);
        SpannableString s = new SpannableString(data_and_history.getTitle());
        SpannableString s1=new SpannableString(login_security.getTitle());
        SpannableString s2=new SpannableString(about.getTitle());
        SpannableString s3=new SpannableString(notifications.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MyTheme), 0, s.length(), 0);
        s1.setSpan(new TextAppearanceSpan(this, R.style.MyTheme), 0, s1.length(), 0);
        s2.setSpan(new TextAppearanceSpan(this, R.style.MyTheme), 0, s2.length(), 0);
        s3.setSpan(new TextAppearanceSpan(this, R.style.MyTheme), 0, s3.length(), 0);
        data_and_history.setTitle(s);
        login_security.setTitle(s1);
        about.setTitle(s2);
        notifications.setTitle(s3);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.setting:

                // If the navigation drawer is not open then open it, if its already open then close it.
                if(!navDrawer.isDrawerOpen(Gravity.RIGHT)) navDrawer.openDrawer(Gravity.RIGHT);
                else navDrawer.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.edit_profile:
                Intent intent=new Intent(this,EditProfileActivity.class);
                intent.putExtra("Id",Id);
                intent.putExtra("Who",Who);
                startActivityForResult(intent,Launch_Activity);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
                break;
        }
        return true;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Launch_Activity) {
            if(resultCode == Activity.RESULT_OK){
               link=data.getStringExtra("link");
                AsyncTaskRunner runner=new AsyncTaskRunner();
                runner.execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
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
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(Id);
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
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient").child(Id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String,Object> userData=(HashMap<String, Object>)dataSnapshot.getValue();
                        String fname=(String)userData.get("fname");
                        String lname=(String)userData.get("lname");
                        String Name=fname+" "+lname;
                        String email=(String)userData.get("email");
                        String phone=(String)userData.get("phone");
                        link=(String)userData.get("link");
                        String address=(String)userData.get("address");
                        WHO.setText(Name);
                        Email.setText(email);
                        Mobile.setText(phone);
                        Address.setText(address);
                        if(!link.equals("None")) {
                            try {
                                Glide.with(ProfileActivity.this).load(link).into(image);
                            }
                            catch(Exception e)
                            {

                            }
                        }
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent=getIntent();
        intent.putExtra("link",link);
        setResult(Activity.RESULT_OK,intent);
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}