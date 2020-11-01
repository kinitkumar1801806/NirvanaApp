package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nirvana.Model.CountryCode;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientSignupActivity extends AppCompatActivity {
    String Age,Email,Pass,Phone,Address,Gender,Fname,Lname,Password;
    private Spinner spinner;
    private ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCode.countryNames));

    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void register_patient(View view) {
        EditText age,password,email,confirm_password,phone,address,fname,lname;
        RadioGroup gender;
        RadioButton patient_gender,male,female;
        final ProgressBar progressBar;
        age=findViewById(R.id.age_patient);
        fname=findViewById(R.id.fname_patient);
        lname=findViewById(R.id.lname_patient);
        email=findViewById(R.id.email_patient);
        phone=findViewById(R.id.mob_patient);
        address=findViewById(R.id.address_patient);
        password=findViewById(R.id.password1_patient);
        confirm_password=findViewById(R.id.password2_patient);
        progressBar=findViewById(R.id.progressBar2_patient);
        progressBar.setVisibility(View.VISIBLE);
        gender=findViewById(R.id.patient_gender);
        String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        int selectedId=gender.getCheckedRadioButtonId();
        patient_gender=findViewById(selectedId);
        String pw=password.getText().toString().trim();
        String cpw=confirm_password.getText().toString().trim();
        if(TextUtils.isEmpty(age.getText()))
        {
            age.setError("Please enter the age");
            progressBar.setVisibility(View.GONE);
        }

        else if(TextUtils.isEmpty(fname.getText()))
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
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the city and state");
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

        else if(!isValidPassword(pw))
        {
            password.setError("Password should contain one digit from 0-9, one lowercase characters,one uppercase characters," +
                    "one special symbols and contain at least 8 characters ");
            progressBar.setVisibility(View.GONE);
        }
        else if(!cpw.equals(pw))
        {
            confirm_password.getText().toString().trim();
            password.setError("Password do not matched");
            progressBar.setVisibility(View.GONE);
        }
        else if(gender.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this,"Please select the gender",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String email1 = email.getText().toString().trim();
            String phone1 = phone.getText().toString().trim();
            String address1 = address.getText().toString().trim();
            String age1 = age.getText().toString().trim();
            String gender1 = patient_gender.getText().toString().trim();
            String fname1 = fname.getText().toString().trim();
            String lname1 = lname.getText().toString().trim();
            String pass1=password.getText().toString();
            String phonenumber = "+" + code + phone1;
            Age=age1;
            Email=email1;
            Phone=phonenumber;
            Address=address1;
            Gender=gender1;
            Fname=fname1;
            Lname=lname1;
            Pass=pass1;
            arr=new ArrayList<String>();
            arr.add(0,Age);
            arr.add(1,Email);
            arr.add(2,Phone);
            arr.add(3,Address);
            arr.add(4,Gender);
            arr.add(5,Fname);
            arr.add(6,Lname);
            arr.add(7,Password);

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patients").child(phonenumber);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Toast.makeText(PatientSignupActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Intent intent=new Intent(PatientSignupActivity.this, PatientPhoneVerification.class);
                        intent.putStringArrayListExtra("arr",arr);
                        startActivity(intent);
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


    public void login_patient(View view) {
        Intent intent=new Intent(this, PatientLoginActivity.class);
        startActivity(intent);
    }
}