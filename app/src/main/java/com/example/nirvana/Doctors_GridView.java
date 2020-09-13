package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.HashMap;

public class Doctors_GridView extends AppCompatActivity {
    ArrayList<String> ImageArray;
    ArrayList<String> NameList;
    ArrayList<String> Username_List,Phone_List,arr;
    GridView androidGridView;
    ProgressDialog progressDialog;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors__grid_view);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        NameList=new ArrayList<String>();
        Username_List=new ArrayList<String>();
        Phone_List=new ArrayList<String>();
        arr=new ArrayList<String>();
        ImageArray=new ArrayList<String>();
        textview=findViewById(R.id.text_view);
        AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
{
    @Override
    protected String doInBackground(String... strings) {

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
               {
                   HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                   for(String key:hashMap.keySet()) {
                       Object data = hashMap.get(key);
                       HashMap<String, Object> userData = (HashMap<String, Object>) data;
                       String fname=(String)userData.get("fname");
                       String lname=(String)userData.get("lname");
                       String Name=fname+" "+lname;
                       String username=(String)userData.get("username");
                       String phone=(String)userData.get("phone");
                       String link=(String)userData.get("link");
                       NameList.add(Name);
                       Username_List.add(username);
                       Phone_List.add(phone);
                       ImageArray.add(link);
                   }
               }
               else
               {
                   textview.setText("Sorry,We have no counsellor till now");
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
        progressDialog =new ProgressDialog(Doctors_GridView.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onPostExecute(String s) {
           ImageAdapter_Doctor adapterViewAndroid = new ImageAdapter_Doctor(Doctors_GridView.this, ImageArray, NameList,Username_List);
           androidGridView=(GridView)findViewById(R.id.gridview);
           androidGridView.setAdapter(adapterViewAndroid);

           androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               public void onItemClick(AdapterView<?> parent,View v, int position, long id){
                   // Send intent to SingleViewActivity
                   String phone=Phone_List.get(position);
                   String name=NameList.get(position);
                   String username=Username_List.get(position);
                   arr.add(0,name);
                   arr.add(1,phone);
                   arr.add(2,username);
                   arr.add(3,ImageArray.get(position));
                   Intent i = new Intent(getApplicationContext(), Fix_Meeting_step2.class);
                   // Pass image index
                   i.putStringArrayListExtra("arr",arr);
                   startActivity(i);
               }
           });
        progressDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}

}
