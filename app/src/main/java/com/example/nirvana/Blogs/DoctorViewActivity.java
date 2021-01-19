package com.example.nirvana.Blogs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirvana.Adapter.BlogAdapter;
import com.example.nirvana.Model.BlogModel;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorViewActivity extends AppCompatActivity {
    private Integer mscroll=0;
    String doctor_Id,Id,liked_blog;
    ArrayList<String> BlogLiked_List;
    ArrayList<String> Like_List,Doctor_List,Doctor_Type,Date_List,Time_List,body_list,blog_No,TitleList,Link_List;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    BlogAdapter blogAdapter;
    public Integer total_blog=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        doctor_Id=intent.getStringExtra("doctor_Id");
        Id=intent.getStringExtra("Id");
        BlogLiked_List=new ArrayList<>();
        Like_List=new ArrayList<>();
        Doctor_List=new ArrayList<>();
        Doctor_Type=new ArrayList<>();
        Date_List=new ArrayList<>();
        Time_List=new ArrayList<>();
        body_list=new ArrayList<>();
        blog_No=new ArrayList<>();
        TitleList=new ArrayList<>();
        Link_List=new ArrayList<>();
        progressDialog =new ProgressDialog(this);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1=firebaseDatabase.getReference("Blogs").child("blogs");
        databaseReference1.orderByChild("date").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        Object data=hashMap.get(key);
                        HashMap<String,Object> userdata= (HashMap<String, Object>) data;
                        total_blog=Integer.parseInt(userdata.get("blog_no").toString());
                        retrieveData();
                    }
                }
                else
                {
                    progressDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void retrieveData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Blogs").child("blogs");
        databaseReference.orderByChild("date").limitToLast(total_blog).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    BlogLiked_List.clear();
                    Like_List.clear();
                    Doctor_List.clear();
                    Doctor_Type.clear();
                    Date_List.clear();
                    Time_List.clear();
                    body_list.clear();
                    blog_No.clear();
                    TitleList.clear();
                    Link_List.clear();
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                    for(String key:hashMap.keySet()) {
                        Object data = hashMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        String title=(String)userData.get("title");
                        String like=(String)userData.get("like");
                        String body=(String)userData.get("body");
                        String date=(String)userData.get("date");
                        String time=(String)userData.get("time");
                        String blog_no=(String)userData.get("blog_no");
                        String doctor_name=(String)userData.get("doctor_name");
                        String doctor_type=(String)userData.get("doctor_type");
                        String link=(String)userData.get("link");
                        String id=(String)userData.get("Id");
                        if(id.equals(doctor_Id))
                        {
                            retrieveLikeDetails(blog_no,time,date,doctor_name,doctor_type,title,like,body,link);
                        }
                    }
                }
                else
                {
                    TextView textView=findViewById(R.id.text_view);
                    textView.setText("We don't have any blog at this time");
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void initRecyclerView()
    {
        blogAdapter=new BlogAdapter(this,BlogLiked_List,Like_List,Doctor_List,Link_List,body_list,TitleList,Time_List,Date_List,Doctor_Type);
        recyclerView.setAdapter(blogAdapter);
        recyclerView.scrollToPosition(mscroll);
        progressDialog.dismiss();
        blogAdapter.setOnItemClickListener(new BlogAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                String title=TitleList.get(position);
                String like=Like_List.get(position);
                String body=body_list.get(position);
                String date= Date_List.get(position);
                String time=Time_List.get(position);
                String blog_no=blog_No.get(position);
                String doctor_name=Doctor_List.get(position);
                String doctor_type=Doctor_Type.get(position);
                String link=Link_List.get(position);
                liked_blog=BlogLiked_List.get(position);
                ArrayList<String> arr=new ArrayList<>();
                arr.add(title);
                arr.add(like);
                arr.add(body);
                arr.add(date);
                arr.add(time);
                arr.add(blog_no);
                arr.add(doctor_name);
                arr.add(doctor_type);
                arr.add(link);
                arr.add(liked_blog);
                arr.add(Id);
                Intent intent=new Intent(DoctorViewActivity.this,DetailDoctorViewActivity.class);
                intent.putStringArrayListExtra("arr",arr);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
            @Override
            public void onLikeChange(int position) {
                mscroll=position;
                if(BlogLiked_List.get(position).equals("1"))
                {
                    Task<Void> firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(Id).child(blog_No.get(position)).removeValue();
                    BlogLiked_List.set(position,"0");
                    int num=Integer.parseInt(Like_List.get(position))-1;
                    Like_List.set(position,String.valueOf(num));
                    Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Blogs").child("blogs").child("Blogno"+blog_No.get(position)).child("like").setValue(String.valueOf(num));
                }
                else
                {
                    BlogModel blogModel=new BlogModel(
                            "1"

                    );
                    DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(Id).child(blog_No.get(position));
                    firebaseDatabase.setValue(blogModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            BlogLiked_List.set(position,"1");
                            int num=Integer.parseInt(Like_List.get(position))+1;
                            Like_List.set(position,String.valueOf(num));
                            Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Blogs").child("blogs").child("Blogno"+blog_No.get(position)).child("like").setValue(String.valueOf(num));
                        }
                    });
                }
                blogAdapter.notifyItemRangeChanged(0,Doctor_List.size());
                blogAdapter.notifyDataSetChanged();
            }
        });
    }
    private void retrieveLikeDetails(String blog,String time,String date,String doctor_name,String doctor_type,String title,String like,String body,String link) {
        DatabaseReference firebaseDatabase=FirebaseDatabase.getInstance().getReference("Blogs").child("Like_blogs").child(Id).child(blog);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)snapshot.getValue();
                    BlogLiked_List.add("1");
                    blog_No.add(blog);
                    Time_List.add(time);
                    Date_List.add(date);
                    Doctor_List.add(doctor_name);
                    Doctor_Type.add(doctor_type);
                    TitleList.add(title);
                    Like_List.add(like);
                    body_list.add(body);
                    Link_List.add(link);
                    initRecyclerView();
                }
                else
                {
                    blog_No.add(blog);
                    BlogLiked_List.add("0");
                    Time_List.add(time);
                    Date_List.add(date);
                    Doctor_List.add(doctor_name);
                    Doctor_Type.add(doctor_type);
                    TitleList.add(title);
                    Like_List.add(like);
                    body_list.add(body);
                    Link_List.add(link);
                    initRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}