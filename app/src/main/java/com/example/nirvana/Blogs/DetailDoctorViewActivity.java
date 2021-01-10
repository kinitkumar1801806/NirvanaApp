package com.example.nirvana.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nirvana.Model.BlogModel;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailDoctorViewActivity extends AppCompatActivity {
    public String blog,time,date,doctor_name,doctor_type,title,like,body,link,liked_blog,Id;
    public TextView Doctor_name,Doctor_type,Title,Body,Likes_Count,Date,Time;
    public ImageView like_image,doctor_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_doctor_view);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        ArrayList<String> arr=new ArrayList<>();
        arr=intent.getStringArrayListExtra("arr");
        title=arr.get(0);
        like=arr.get(1);
        body=arr.get(2);
        date=arr.get(3);
        time=arr.get(4);
        blog=arr.get(5);
        doctor_name=arr.get(6);
        doctor_type=arr.get(7);
        link=arr.get(8);
        liked_blog=arr.get(9);
        Id=arr.get(10);
        doctor_image=findViewById(R.id.doctor_image);
        Doctor_name=findViewById(R.id.doctor_name);
        Doctor_type=findViewById(R.id.doctor_type);
        Time=findViewById(R.id.time);
        Date=findViewById(R.id.date);
        Likes_Count=findViewById(R.id.likes_count);
        Title=findViewById(R.id.title);
        Body=findViewById(R.id.body);
        like_image=findViewById(R.id.like_image);
        Glide.with(this).load(link).into(doctor_image);
        Doctor_name.setText(doctor_name);
        Doctor_type.setText(doctor_type);
        Time.setText(time);
        Date.setText(date);
        Likes_Count.setText(like);
        Title.setText(title);
        Body.setText(body);
        System.out.println(liked_blog);
        if(liked_blog.equals("1"))
        {
            like_image.setImageResource(R.drawable.green_like);
            Likes_Count.setTextColor(Color.parseColor("#339966"));
        }
        like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like_image.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState())
                {
                    like_image.setImageResource(R.drawable.green_like);
                    Likes_Count.setTextColor(Color.parseColor("#339966"));
                    BlogModel blogModel=new BlogModel(
                            "1"
                    );
                    DatabaseReference firebaseDatabase= FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(Id).child(blog);
                    firebaseDatabase.setValue(blogModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            int num=Integer.parseInt(like)+1;
                            like=String.valueOf(num);
                            Likes_Count.setText(like);
                            Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Blogs").child("blogs").child("Blogno"+blog).child("like").setValue(String.valueOf(num));
                        }
                    });
                }
                else
                {
                    like_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Likes_Count.setTextColor(Color.parseColor("#000000"));
                    Task<Void> firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(Id).child(blog).removeValue();
                    int num=Integer.parseInt(like)-1;
                    like=String.valueOf(num);
                    Likes_Count.setText(like);
                    Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Blogs").child("blogs").child("Blogno"+blog).child("like").setValue(String.valueOf(num));
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}