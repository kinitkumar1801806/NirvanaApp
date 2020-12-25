package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nirvana.Doctors.DoctorPhoneVerification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    public String Id, Who, fname, lname, age, email, address, link,link1;
    public EditText First_Name, Last_Name, Age, Email, City_State;
    public ImageView Profile_Image;
    private Uri Imagepath;
    private StorageReference mStorageRef;
    public Integer img=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Id = intent.getStringExtra("Id");
        Who = intent.getStringExtra("Who");
        First_Name = findViewById(R.id.fname_patient);
        Last_Name = findViewById(R.id.lname_patient);
        Age = findViewById(R.id.age_patient);
        Email = findViewById(R.id.email_patient);
        City_State = findViewById(R.id.address_patient);
        Profile_Image = findViewById(R.id.profile_image);
        RetrieveData();
    }

    private void RetrieveData() {
        if (Who.equals("doctor")) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Doctors").child(Id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userData = (HashMap<String, Object>) dataSnapshot.getValue();
                    fname = (String) userData.get("fname");
                    lname = (String) userData.get("lname");
                    email = (String) userData.get("email");
                    link1 = (String) userData.get("link");
                    address = (String) userData.get("address");
                    AssignData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (Who.equals("patient")) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Patient").child(Id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userData = (HashMap<String, Object>) dataSnapshot.getValue();
                    fname = (String) userData.get("fname");
                    lname = (String) userData.get("lname");
                    email = (String) userData.get("email");
                    link1 = (String) userData.get("link");
                    address = (String) userData.get("address");
                    age=(String)userData.get("age");
                    AssignData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void AssignData() {
        First_Name.setText(fname);
        Last_Name.setText(lname);
        Age.setText(age);
        Email.setText(email);
        City_State.setText(address);
        if(!link1.equals("None"))
        {
            Glide.with(this).load(link1).into(Profile_Image);
        }
    }

    public void change_image(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data.getData()!=null)
        {
            Imagepath=data.getData();
            img=1;
            Glide.with(this).load(Imagepath).into(Profile_Image);
        }
    }

    public void Cancel(View view) {
        finish();
        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void Save(View view) {
        ProgressBar progressBar;
        ImageView Save;
        progressBar=findViewById(R.id.progressBar);
        Save=findViewById(R.id.save_btn);
        Save.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(First_Name.getText()))
        {
            First_Name.setError("Please enter the first name");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else if(TextUtils.isEmpty(Last_Name.getText()))
        {
            Last_Name.setError("Please enter the last name");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else if(TextUtils.isEmpty(Age.getText()))
        {
            Age.setError("Please enter the age");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else if(TextUtils.isEmpty(Email.getText()))
        {
            Email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else if(!(isValidEmailId(Email.getText().toString())))
        {
            Email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else if(TextUtils.isEmpty(City_State.getText()))
        {
            City_State.setError("Please enter the city/state");
            progressBar.setVisibility(View.GONE);
            Save.setVisibility(View.VISIBLE);
        }
        else{
            if(fname.equals(First_Name.getText().toString())&&lname.equals(Last_Name.getText().toString())&&age.equals(Age.getText().toString())&&
                    email.equals(Email.getText().toString())&&address.equals(City_State.getText().toString())&&img==0)
            {
                progressBar.setVisibility(View.GONE);
                finish();
                overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
            }
            fname=First_Name.getText().toString();
            lname=Last_Name.getText().toString();
            age=Age.getText().toString();
            email=Email.getText().toString();
            address=City_State.getText().toString();
            Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("fname").setValue(fname);
            Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("lname").setValue(lname);
            Task<Void> databaseReference2=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("age").setValue(age);
            Task<Void> databaseReference3=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("email").setValue(email);
            Task<Void> databaseReference4=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("address").setValue(address);
            if(img==1)
            {
                StorageReference Ref =mStorageRef.child("Profile Images").child(Id);
                Ref.putFile(Imagepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressBar.setVisibility(View.GONE);
                                        link=uri.toString();
                                        Task<Void> databaseReference4=FirebaseDatabase.getInstance().getReference("Patient").child(Id).child("link").setValue(link);
                                        RetrieveData();
                                        finish();
                                        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
                                    }
                                }) ;

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                progressBar.setVisibility(View.GONE);
                                Save.setVisibility(View.VISIBLE);
                                Toast.makeText(EditProfileActivity.this,"Profile Image is not Changed",Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        });
            }
        }
    }
}