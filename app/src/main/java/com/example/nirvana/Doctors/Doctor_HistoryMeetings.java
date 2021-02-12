package com.example.nirvana.Doctors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Adapter.History_Doctor_Meetings_Adapter;
import com.example.nirvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Doctor_HistoryMeetings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Doctor_HistoryMeetings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public RecyclerView recyclerView;
    History_Doctor_Meetings_Adapter history_doctor_meetings_adapter;
    public ArrayList<String> LinkList,NameList,ProblemList,DateList,TimeList;
    public String Id,link;
    TextView textView;
    View view1;
    boolean check=false;
    public Doctor_HistoryMeetings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Doctor_HistoryMeetings.
     */
    // TODO: Rename and change types and number of parameters
    public static Doctor_HistoryMeetings newInstance(String param1, String param2) {
        Doctor_HistoryMeetings fragment = new Doctor_HistoryMeetings();
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
        Bundle bundle=getArguments();
        Id=bundle.getString("Id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_doctor__history_meetings, container, false);
        LinkList=new ArrayList<>();
        NameList=new ArrayList<>();
        ProblemList=new ArrayList<>();
        DateList=new ArrayList<>();
        TimeList=new ArrayList<>();
        textView=view1.findViewById(R.id.textView);
        if(!check)
        {
            textView.setVisibility(View.VISIBLE);
        }
        check=false;
        recyclerView=view1.findViewById(R.id.upcoming_historylists);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        retrieveData();
        return view1;
    }
    public void retrieveData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Doctor_Meetings").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        HashMap<String,Object>hashMap1= (HashMap<String, Object>) hashMap.get(key);
                        for(String key1:hashMap1.keySet())
                        {
                            Object data=hashMap1.get(key1);
                            HashMap<String,Object> userData=(HashMap<String,Object>)data;
                            String complete=userData.get("complete").toString();
                            if(complete.equals("1"))
                            {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference=firebaseDatabase.getReference("Patient").child(key);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                            link=hashMap.get("link").toString();
                                            String name=userData.get("p_name").toString();
                                            String date=userData.get("date").toString();
                                            String time=userData.get("time").toString();
                                            String problem=userData.get("p_problem").toString();
                                            NameList.add(name);
                                            LinkList.add(link);
                                            DateList.add(date);
                                            TimeList.add(time);
                                            ProblemList.add(problem);
                                            initRecyclerView();
                                            check=true;
                                        }
                                        if(!check){
                                            textView.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                    }
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView() {
        history_doctor_meetings_adapter =new History_Doctor_Meetings_Adapter(getActivity(),NameList,ProblemList,DateList,TimeList,LinkList);
        recyclerView.setAdapter(history_doctor_meetings_adapter);
        history_doctor_meetings_adapter.setOnItemClickListener(new History_Doctor_Meetings_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),"Button Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}