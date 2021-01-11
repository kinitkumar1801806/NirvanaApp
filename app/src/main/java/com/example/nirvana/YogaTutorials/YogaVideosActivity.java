package com.example.nirvana.YogaTutorials;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nirvana.Adapter.ImageAdapter_Doctor;
import com.example.nirvana.Adapter.YoutubeVideosAdapter;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class YogaVideosActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    YoutubeVideosAdapter youtubeVideosAdapter;
    ProgressDialog progressDialog;
    ArrayList<String> VideosList,TitleList,ThumbnailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_videos);
        progressDialog =new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recyclerView=findViewById(R.id.youtube_videos_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        VideosList=new ArrayList<>();
        TitleList=new ArrayList<>();
        ThumbnailList=new ArrayList<>();
        retrieveData();
    }
    public void retrieveData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("YogaVideos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=(HashMap<String, Object>)dataSnapshot.getValue();
                    for(String key:hashMap.keySet()) {
                        Object data = hashMap.get(key);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        String link=userData.get("link").toString();
                        String title=userData.get("title").toString();
                        String thumbnail=userData.get("thumbnail").toString();
                        VideosList.add(link);
                        TitleList.add(title);
                        ThumbnailList.add(thumbnail);
                        initRecyclerView();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void initRecyclerView()
    {
        youtubeVideosAdapter= new YoutubeVideosAdapter(YogaVideosActivity.this, VideosList,TitleList,ThumbnailList);
        recyclerView.setAdapter(youtubeVideosAdapter);
        youtubeVideosAdapter.setOnItemClickListener(new YoutubeVideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String link=VideosList.get(position);
                Intent intent=new Intent(YogaVideosActivity.this,YoutubePlayerActivity.class);
                intent.putExtra("link",link);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}