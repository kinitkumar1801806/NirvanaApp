package com.example.nirvana.Blogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Adapter.BlogAdapter;
import com.example.nirvana.Doctors.Doctor_Welcome_Activity;
import com.example.nirvana.Model.BlogModel;
import com.example.nirvana.Model.LastScrolledBlog;
import com.example.nirvana.Patients.Fix_Meeting_step3;
import com.example.nirvana.Patients.Patient_Welcome_Activity;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeBlogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeBlogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Integer mscroll=0;
    private int mScrollY;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,Id,liked_blog,BlogNo="0",Filter;
    View view1;
    ArrayList<String> BlogLiked_List;
    ArrayList<String> filter;
    ArrayList<String> Like_List,Doctor_List,Doctor_Type,Date_List,Time_List,body_list,blog_No,TitleList,Link_List;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    BlogAdapter blogAdapter;
    private boolean isChecking = true;
    private int selectedId=-1,total_blog=0;
    public HomeBlogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeBlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeBlogFragment newInstance(String param1, String param2) {
        HomeBlogFragment fragment = new HomeBlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle bundle=this.getArguments();
        Id=bundle.getString("Id");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        MenuItem filter=menu.findItem(R.id.filter);
        Spannable s=new SpannableString(filter.getTitle());
        s.setSpan(new TextAppearanceSpan(getActivity(),R.style.SpinnerTheme),0,s.length(),0);
        filter.setTitle(s);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.filter:
                DoAlter();
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_home_blog, container, false);
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Filter = preferences.getString("Filter", "");
        progressDialog =new ProgressDialog(getActivity());
        recyclerView=view1.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
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
                        retrieveData(Filter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Last_Scroll_Blog").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                   BlogNo=hashMap.get("blogno").toString();
               }
               else
               {
                   progressDialog.show();
                   progressDialog.setContentView(R.layout.progress_dialog);
                   progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view1;
    }
    public void retrieveData(String type)
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Blogs").child("blogs");
        if(type.equals("date"))
        {
            databaseReference.orderByChild(type).limitToLast(total_blog).addValueEventListener(new ValueEventListener() {
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
                            retrieveLikeDetails(blog_no,time,date,doctor_name,doctor_type,title,like,body,link);
                        }
                    }
                    else
                    {
                        TextView textView=view1.findViewById(R.id.text_view);
                        textView.setText("We don't have any blog at this time");
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            databaseReference.orderByChild("doctor_type").equalTo(type).limitToLast(total_blog).addValueEventListener(new ValueEventListener() {
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
                            retrieveLikeDetails(blog_no,time,date,doctor_name,doctor_type,title,like,body,link);
                        }
                    }
                    else
                    {
                        TextView textView=view1.findViewById(R.id.text_view);
                        textView.setText("We don't have any blog at this time");
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void initRecyclerView()
    {
        blogAdapter=new BlogAdapter(getActivity(),BlogLiked_List,Like_List,Doctor_List,Link_List,body_list,TitleList,Time_List,Date_List,Doctor_Type);
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
                LastScrolledBlog lastScrolledBlog=new LastScrolledBlog(
                  blog_no
                );
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Last_Scroll_Blog").child(Id);
                databaseReference.setValue(lastScrolledBlog).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Bundle bundle=new Bundle();
                        bundle.putStringArrayList("arr",arr);
                        DetailBlogFragment detailBlogFragment=new DetailBlogFragment();
                        detailBlogFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout,detailBlogFragment,"detail");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
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
                if(BlogNo.equals(blog))
                {
                    mscroll=BlogLiked_List.size()-1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void DoAlter()
    {
        final AlertDialog alterDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View dialogueview=inflater.inflate(R.layout.filter_blog, null);
        RadioGroup radioGroup=dialogueview.findViewById(R.id.radio_group);
        RadioGroup radioGroup2=dialogueview.findViewById(R.id.radio_group2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    radioGroup2.clearCheck();
                    selectedId = checkedId;
                }
                isChecking = true;
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    radioGroup.clearCheck();
                    selectedId = checkedId;
                }
                isChecking = true;
            }
        });
        alterDialog.setView(dialogueview);
        alterDialog.show();
        Button apply=dialogueview.findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton filter_by;
                if(selectedId!=-1)
                {
                    if(selectedId==R.id.date_radio||selectedId==R.id.therapist_radio||selectedId==R.id.counsellor_raadio)
                    {
                        filter_by=radioGroup.findViewById(selectedId);
                        Filter=filter_by.getText().toString();
                    }
                    else
                    {
                        filter_by=radioGroup2.findViewById(selectedId);
                        Filter=filter_by.getText().toString();
                    }
                    if(Filter.equals("Date"))
                    {
                        Filter="date";
                    }
                    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Filter",Filter);
                    editor.apply();
                    retrieveData(Filter);
                }
                alterDialog.dismiss();
            }
        });
    }
}