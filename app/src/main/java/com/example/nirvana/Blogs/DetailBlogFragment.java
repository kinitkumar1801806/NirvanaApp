package com.example.nirvana.Blogs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailBlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBlogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String blog,time,date,doctor_name,doctor_type,title,like,body,link,liked_blog,Id;
    public TextView Doctor_name,Doctor_type,Title,Body,Likes_Count,Date,Time;
    public ImageView like_image,doctor_image;
    public View view1;
    public DetailBlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailBlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailBlogFragment newInstance(String param1, String param2) {
        DetailBlogFragment fragment = new DetailBlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle=this.getArguments();
        ArrayList<String> arr=new ArrayList<>();
        arr=bundle.getStringArrayList("arr");
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_detail_blog, container, false);
        doctor_image=view1.findViewById(R.id.doctor_image);
        Doctor_name=view1.findViewById(R.id.doctor_name);
        Doctor_type=view1.findViewById(R.id.doctor_type);
        Time=view1.findViewById(R.id.time);
        Date=view1.findViewById(R.id.date);
        Likes_Count=view1.findViewById(R.id.likes_count);
        Title=view1.findViewById(R.id.title);
        Body=view1.findViewById(R.id.body);
        like_image=view1.findViewById(R.id.like_image);
        Glide.with(getActivity()).load(link).into(doctor_image);
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
        return view1;
    }
}