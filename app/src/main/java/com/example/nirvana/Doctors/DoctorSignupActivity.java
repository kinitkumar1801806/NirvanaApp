package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Model.CountryCode;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorSignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_File_REQUEST = 134;
    String Email,Phone,Address,Gender,Fname,Lname,Password,Affiliation,LinkedIn,Year_Of_Practice,Place_Of_Practice;
    private Spinner spinner;
    private Uri Imagepath,Filepath;
    private StorageReference mStorageRef;
    private Button upload_file,upload_image;
    ArrayList<String> arr;
    StorageReference Ref;
    int img,file=0;
    private TextView image_text,file_text;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCode.countryNames));
        upload_file=findViewById(R.id.certi_file);
        upload_image=findViewById(R.id.image);
        upload_file.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        image_text=findViewById(R.id.image_text);
        file_text=findViewById(R.id.certi_text);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void register_doctor(View view) {
        EditText password,email,confirm_password,phone,address,fname,lname,affiliation,year_of_practice,place_of_practice,linkedIn;
        RadioGroup gender;
        RadioButton doctor_gender;
        final ProgressBar progressBar;
        fname=findViewById(R.id.fname_doctor);
        lname=findViewById(R.id.lname_doctor);
        email=findViewById(R.id.email_doctor);
        phone=findViewById(R.id.mob_doctor);
        affiliation=findViewById(R.id.affiliation);
        year_of_practice=findViewById(R.id.year_of_practice);
        place_of_practice=findViewById(R.id.place_of_practice);
        linkedIn=findViewById(R.id.linkedIn_doctor);
        address=findViewById(R.id.address_doctor);
        password=findViewById(R.id.password1_doctor);
        confirm_password=findViewById(R.id.password2_doctor);
        progressBar=findViewById(R.id.progressBar2_doctor);
        progressBar.setVisibility(View.VISIBLE);
        gender=findViewById(R.id.doctor_gender);
        String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        int selectedId=gender.getCheckedRadioButtonId();
        doctor_gender=findViewById(selectedId);
        String pw=password.getText().toString().trim();
        String cpw=confirm_password.getText().toString().trim();
        if(TextUtils.isEmpty(fname.getText()))
        {
            fname.setError("Please enter the first name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(lname.getText()))
        {
            lname.setError("Please enter the last name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(email.getText()))
        {
            email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
        }
        else if(!(isValidEmailId(email.getText().toString())))
        {
            email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
        }

        else if(TextUtils.isEmpty(phone.getText()))
        {
            phone.setError("Please enter the phone number");
            progressBar.setVisibility(View.GONE);
        }
        else if(phone.getText().toString().length()<10)
        {
            phone.setError("Valid Phone number required");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(place_of_practice.getText()))
        {
            place_of_practice.setError("Please enter your place of practice");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(year_of_practice.getText()))
        {
            year_of_practice.setError("Please enter your year of practice");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(affiliation.getText()))
        {
            affiliation.setError("Please enter your affiliation");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(linkedIn.getText()))
        {
            linkedIn.setError("Please enter your LinkedIn Profile");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(password.getText()))
        {
            password.setError("Please enter the password");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(confirm_password.getText()))
        {
            confirm_password.setError("Please confirm password");
            progressBar.setVisibility(View.GONE);
        }
        else if(!cpw.equals(pw))
        {
            password.setError("Password do not matched");
            progressBar.setVisibility(View.GONE);
        }
        else if(!isValidPassword(pw))
        {
            password.setError("Password should contain one digit from 0-9, one lowercase characters,one uppercase characters," +
                    "one special symbols and contain at least 8 characters ");
            progressBar.setVisibility(View.GONE);
        }
        else if(gender.getCheckedRadioButtonId()==-1)
        {
         Toast.makeText(this,"Please select the gender",Toast.LENGTH_SHORT).show();
         progressBar.setVisibility(View.GONE);
        }
        else if(img==0)
        {
            Toast.makeText(this,"Please Choose the Image",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else if(file==0)
        {
            Toast.makeText(this,"Please Choose the Certificate",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String email1=email.getText().toString().trim();
            String phone1=phone.getText().toString().trim();
            String address1=address.getText().toString().trim();
            String gender1=doctor_gender.getText().toString().trim();
            String fname1=fname.getText().toString().trim();
            String lname1=lname.getText().toString().trim();
            String pass1=password.getText().toString();
            String phonenumber = "+" + code +phone1;
            Email=email1;
            Phone=phonenumber;
            Address=address1;
            Gender=gender1;
            Fname=fname1;
            Lname=lname1;
            Password=pass1;
            Affiliation=affiliation.getText().toString();
            Year_Of_Practice=year_of_practice.getText().toString();
            Place_Of_Practice=place_of_practice.getText().toString();
            LinkedIn=linkedIn.getText().toString();
            arr=new ArrayList<String>();
            arr.add(0,"");
            arr.add(1,Email);
            arr.add(2,Phone);
            arr.add(3,Address);
            arr.add(4,Gender);
            arr.add(5,Fname);
            arr.add(6,Lname);
            arr.add(7,Password);
            arr.add(8,Affiliation);
            arr.add(9,LinkedIn);
            arr.add(10,Year_Of_Practice);
            arr.add(11,Place_Of_Practice);

            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(Phone);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Toast.makeText(DoctorSignupActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {

                        StorageReference Ref1 =mStorageRef.child("Doctors").child(Phone).child("Certificate");
                        Ref1.putFile(Filepath);
                        Ref =mStorageRef.child("Doctors").child(Phone).child("Image");
                        Ref.putFile(Imagepath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                      Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                          @Override
                                          public void onSuccess(Uri uri) {
                                              String link=uri.toString();
                                              arr.add(12,link);
                                              Intent intent=new Intent(DoctorSignupActivity.this, DoctorPhoneVerification.class);
                                              intent.putStringArrayListExtra("arr",arr);
                                              startActivity(intent);
                                          }
                                      }) ;

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(DoctorSignupActivity.this,"We are getting some issue.We will come back very soon",Toast.LENGTH_SHORT).show();
                                        // ...
                                    }
                                });
           }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void login_doctor(View view) {
        Intent intent =new Intent(this, DoctorLoginActivity.class);
        startActivity(intent);
    }
    private void showImagechooser()
    {
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
           image_text.setText("Image Selected");
           img=1;
        }
        if(requestCode==PICK_File_REQUEST && resultCode==RESULT_OK && data.getData()!=null)
        {
            Filepath=data.getData();
            file_text.setText("File Selected");
            file=1;
        }
    }
   private void showfilechooser()
   {
       Intent intent=new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select an file"),PICK_File_REQUEST);
    }
    @Override
    public void onClick(View v) {
      if(v==upload_file)
      {
          showfilechooser();
      }
      if(v==upload_image)
      {
      showImagechooser();
      }
    }
}
