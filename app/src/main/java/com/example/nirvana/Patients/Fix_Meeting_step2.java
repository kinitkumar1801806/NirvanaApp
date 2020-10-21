package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nirvana.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Fix_Meeting_step2 extends AppCompatActivity {
    ProgressDialog progressDialog;
    private String phone,linkedIn,doctor_name;
    private TextView nameText,Qualitext,linkedINtext,totaltext,satisfiedtext,ratingtext;
    ArrayList<String> arr;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix__meeting_step2);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        phone=arr.get(1);
        doctor_name=arr.get(0);
        nameText=findViewById(R.id.doctor_name);
        Qualitext=findViewById(R.id.qualification);
        linkedINtext=findViewById(R.id.linkedIn_url);
        totaltext=findViewById(R.id.total_patient);
        satisfiedtext=findViewById(R.id.satisfied_patient);
        ratingtext=findViewById(R.id.rating);
        image=findViewById(R.id.doctor_image);
        Fix_Meeting_step2.AsyncTaskRunner runner=new Fix_Meeting_step2.AsyncTaskRunner();
        runner.execute();
    }

    public void linkedIn(View view) {
        String url = linkedIn;
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void fix_the_meeting(View view) {
        Intent intent =new Intent(this, Fix_Meeting_step3.class);
        intent.putStringArrayListExtra("arr",arr);
        startActivity(intent);
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors").child(phone);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                        String fname=(String)hashMap.get("fname");
                        String lname=(String)hashMap.get("lname");
                        String name=fname+" "+lname;
                        nameText.setText(name);
                        String quali=(String)hashMap.get("qualification");
                        Qualitext.setText(quali);
                        linkedIn=(String)hashMap.get("linkedIn");
                        linkedINtext.setText(linkedIn);
                        String total_patient=(String)hashMap.get("totalpatient");
                        totaltext.setText(total_patient);
                        String satisfied_patient=(String)hashMap.get("satisfiedpatient");
                        satisfiedtext.setText(satisfied_patient);
                        String rating=(String)hashMap.get("rating");
                        String ratingby=(String)hashMap.get("ratingby");
                        ratingtext.setText(rating+" (By "+ratingby+" people)");
                        String phone=(String)hashMap.get("phone");
                        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Doctors").child(phone).child("Image");
                        final long ONE_MEGABYTE = 1024 * 1024 * 5;
                        if(storageReference!=null)
                        {
                            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    image.setImageBitmap(bm);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return "Finish";
        }
        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(Fix_Meeting_step2.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("Finish"))
        {
            progressDialog.dismiss();
         }
}

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


    }
}
