package com.example.nirvana.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nirvana.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Blog_Detail_Activity extends AppCompatActivity {
public TextView BlogNo,BlogTitle,BlogBody,Comment,Like,Date,Time;
public ImageView Like_btn;
public String blog,title,date,time,like,comment,body,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog__detail_);
        BlogNo=findViewById(R.id.blogno);
        BlogTitle=findViewById(R.id.blog_title);
        BlogBody=findViewById(R.id.blog_body);
        Comment=findViewById(R.id.comment);
        Like=findViewById(R.id.like);
        Date=findViewById(R.id.date);
        Time=findViewById(R.id.Time);
        Like_btn=findViewById(R.id.imageView6);
        Intent intent=getIntent();
        blog=intent.getStringExtra("blog");
        title=intent.getStringExtra("title");
        body=intent.getStringExtra("body");
        comment=intent.getStringExtra("comment");
        like=intent.getStringExtra("like");
        date=intent.getStringExtra("date");
        time=intent.getStringExtra("time");
        phone=intent.getStringExtra("phone");
        BlogNo.setText(blog);
        BlogTitle.setText(title);
        BlogBody.setText(body);
        Comment.setText(comment);
        Like.setText(like);
        Date.setText(date);
        Time.setText(time);
        checkLike();
    }
    public void checkLike()
    {
     DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(phone).child(blog);
     firebaseDatabase.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists())
             {
                 HashMap<String,Object> hashMap=(HashMap<String, Object>)snapshot.getValue();
                 String liked=(String)hashMap.get("like");
                 if(liked.equals("1"))
                 {
                     Like_btn.setImageResource(R.drawable.like_heart_white);
                 }
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }

    public void Updatelikes(View view) {
        if(Like_btn.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState())
        {
            update();
            Like_btn.setImageResource(R.drawable.like_heart_white);
        }
    }
    public void update()
    {
        int l=Integer.parseInt(like);
        System.out.println(l);
        l++;
       like=String.valueOf(l);
       Task<Void> firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(phone).child(blog).child("like")
               .setValue("1");
        FirebaseDatabase firebaseDatabase1=FirebaseDatabase.getInstance();
       Like.setText(String.valueOf(l));
    }

    @Override
    public void onBackPressed() {
        final Intent data = new Intent();
        data.putExtra("like",like);
        data.putExtra("blog",blog);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
}