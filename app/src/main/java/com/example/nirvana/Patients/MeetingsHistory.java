package com.example.nirvana.Patients;

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

import com.example.nirvana.Adapter.History_Patient_Meetings_Adapter;
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
 * Use the {@link MeetingsHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingsHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View view1;
    public RecyclerView recyclerView;
    History_Patient_Meetings_Adapter history_patient_meetings_adapter;
    public ArrayList<String> LinkList,NameList,TypeList,DateList,TimeList;
    public String Id,link;
    TextView textView;
    Boolean check=false;
    public MeetingsHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingsHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingsHistory newInstance(String param1, String param2) {
        MeetingsHistory fragment = new MeetingsHistory();
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
        view1= inflater.inflate(R.layout.fragment_meetings_history, container, false);
        LinkList=new ArrayList<>();
        NameList=new ArrayList<>();
        TypeList=new ArrayList<>();
        DateList=new ArrayList<>();
        TimeList=new ArrayList<>();
        textView=view1.findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        recyclerView=view1.findViewById(R.id.history_patientlists);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        retrieveData();
        return view1;
    }
    public void retrieveData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Patient_Meetings").child(Id);
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
                                DatabaseReference databaseReference=firebaseDatabase.getReference("Doctors").child(key);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                            link=hashMap.get("link").toString();
                                            String name=userData.get("d_name").toString();
                                            String date=userData.get("date").toString();
                                            String time=userData.get("time").toString();
                                            String type=userData.get("d_bio").toString();
                                            NameList.add(name);
                                            LinkList.add(link);
                                            DateList.add(date);
                                            TimeList.add(time);
                                            TypeList.add(type);
                                            initRecyclerView();
                                            check=true;
                                        }
                                        if(check)
                                        {
                                            textView.setVisibility(View.GONE);
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
        history_patient_meetings_adapter =new History_Patient_Meetings_Adapter(getActivity(),NameList,TypeList,DateList,TimeList,LinkList);
        recyclerView.setAdapter(history_patient_meetings_adapter);
        history_patient_meetings_adapter.setOnItemClickListener(new History_Patient_Meetings_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(),"Button Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}