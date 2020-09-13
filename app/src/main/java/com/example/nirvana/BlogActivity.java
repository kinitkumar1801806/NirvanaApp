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

import org.w3c.dom.Comment;
import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.HashMap;

public class BlogActivity extends AppCompatActivity {
    ArrayList<String> TitleList;
    ArrayList<String> Like_List,Comment_List,head_List,Date_List,Time_List;
    GridView androidGridView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        TitleList=new ArrayList<String>();
        Like_List=new ArrayList<String>();
        Comment_List =new ArrayList<String>();
        head_List=new ArrayList<String>();
        Date_List =new ArrayList<String>();
        Time_List=new ArrayList<String>();
        AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Blogs");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                        for(String key:hashMap.keySet()) {
                            Object data = hashMap.get(key);
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String title=(String)userData.get("title");
                            String head=(String)userData.get("head");
                            String like=(String)userData.get("like");
                            String comment=(String)userData.get("comment");
                            String date=(String)userData.get("submission_date");
                            String time=(String)userData.get("submission_time");
                            Comment_List.add(comment);
                            Time_List.add(time);
                            Date_List.add(date);
                            Like_List.add(like);
                            Comment_List.add(comment);
                            TitleList.add(title);
                            head_List.add(head);
                        }
                    }
                    else
                    {
                        TextView textView=findViewById(R.id.text_view);
                        textView.setText("You have no meetings");
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
            progressDialog =new ProgressDialog(BlogActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s) {
            BlogsAdapter adapterViewAndroid = new BlogsAdapter(BlogActivity.this, TitleList, head_List,Like_List,Comment_List,Date_List,Time_List);
            androidGridView=(GridView)findViewById(R.id.gridview);
            androidGridView.setAdapter(adapterViewAndroid);

            androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent,View v, int position, long id){
                    // Send intent to SingleViewActivity


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
