package com.example.nirvana.Blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nirvana.Adapter.BlogAdapter;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BlogActivity extends AppCompatActivity {
    ArrayList<String> TitleList;
    ArrayList<String> Like_List,Comment_List,head_List,Date_List,Time_List,body_list,blog_No,Expand_List;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    public String phone;
    BlogAdapter blogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TitleList=new ArrayList<>();
        Expand_List=new ArrayList<>();
        Like_List=new ArrayList<>();
        Intent intent=getIntent();
        phone=intent.getStringExtra("phone");
        blog_No=new ArrayList<>();
        Comment_List =new ArrayList<>();
        head_List=new ArrayList<>();
        body_list=new ArrayList<>();
        Date_List =new ArrayList<>();
        Time_List=new ArrayList<>();
        AsyncTaskRunner runner=new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference("Blogs").child("blogs");
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
                            String body=(String)userData.get("body");
                            String comment=(String)userData.get("comment");
                            String date=(String)userData.get("submission_date");
                            String time=(String)userData.get("submission_time");
                            String blog_no=(String)userData.get("blog_no");
                            Comment_List.add(comment);
                            body_list.add(body);
                            blog_No.add(blog_no);
                            Expand_List.add("0");
                            Time_List.add(time);
                            Date_List.add(date);
                            Like_List.add(like);
                            TitleList.add(title);
                            head_List.add(head);
                        }
                    }
                    else
                    {
                        TextView textView=findViewById(R.id.text_view);
                        textView.setText("We don't have any blog at this time");
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

            blogAdapter=new BlogAdapter(BlogActivity.this,Like_List,Comment_List,body_list,TitleList,head_List,Time_List,Date_List,Expand_List);
            recyclerView.setAdapter(blogAdapter);
            progressDialog.dismiss();
            blogAdapter.setOnItemClickListener(new BlogAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                   String blog=blog_No.get(position);
                }

                @Override
                public void onExpand(int position) {
                    if(Expand_List.get(position).equals("1"))
                    {
                        Expand_List.set(position,"0");
                    }
                    else
                    {
                        Expand_List.set(position,"1");
                    }
                    blogAdapter.notifyItemChanged(position);
                }


            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
 public  void UpdateLikeInfo(int value,int position)
 {
     DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(phone).child(blog_No.get(position));
     DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Blogs").child("blogs").child(blog_No.get(position));
     if(value==1)
     {
         int like=Integer.parseInt(Like_List.get(position))+1;
         databaseReference1.child("like").setValue(String.valueOf(like));
         HashMap<String,Object> hashMap=new HashMap<>();
         hashMap.put("Like","Yes");
         databaseReference.push().setValue(hashMap);
     }
     else
     {
         int like=Integer.parseInt(Like_List.get(position))-1;
         databaseReference1.child("like").setValue(String.valueOf(like));
        databaseReference.removeValue();
     }
     AsyncTaskRunner runner=new AsyncTaskRunner();
     runner.execute();
 }
}
