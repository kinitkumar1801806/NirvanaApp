package com.example.nirvana.Patients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Adapter.ContinueMeeting;
import com.example.nirvana.Doctors.Doctor_Meeting;
import com.example.nirvana.Doctors.Doctor_Welcome_Activity;
import com.example.nirvana.Model.CountryCode;
import com.example.nirvana.Payments.MainPaymentActivity;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Fix_Meeting_step3 extends AppCompatActivity {
    ArrayList<String>arr,Patient_Detail,Doctor_Detail;
    private String doctor_name,doctor_phone,Did,Pid,Pname,Problem,child_date;
    RecyclerView recyclerView;
    private int mYear, mMonth, mDay;
    Boolean check=true;
    String fromTime,toTime,lastDateChange,slot_details,slots_no,final_date,final_slot,name,problem;
    Date mindate,maxdate;
    JSONObject json;
    TextView ContinueAs;
    private boolean Flag=false;
    ContinueMeeting continueMeeting;
    ArrayList<String> Avaiable_Slots,Patient_Name,Patient_Problem;
    HashMap<String,String> Slots_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix__meeting_step3);
        Intent intent=getIntent();
        Pid=intent.getStringExtra("Id");
        arr=intent.getStringArrayListExtra("arr");
        doctor_name=arr.get(0);
        doctor_phone=arr.get(1);
        Did=arr.get(5);
        Avaiable_Slots=new ArrayList<>();
        Patient_Detail=new ArrayList<>();
        Doctor_Detail=new ArrayList<>();
        Patient_Name=new ArrayList<>();
        Patient_Problem=new ArrayList<>();
        Slots_map=new HashMap<>();
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ContinueAs=findViewById(R.id.continueAs);
        ContinueAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Flag)
               {
                   recyclerView.setVisibility(View.GONE);
                   ContinueAs.setText("CONTINUE AS ▼");
                   Flag=false;
               }
               else
               {
                   recyclerView.setVisibility(View.VISIBLE);
                   ContinueAs.setText("CONTINUE AS ▲");
                   Flag=true;
               }
            }
        });
        retrieveData();
        retrievePreviousDetails();
    }

    private void retrievePreviousDetails() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(Pid).child(Did);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                   for(String key:hashMap.keySet())
                   {
                       Object data=hashMap.get(key);
                       HashMap<String,Object> userdata= (HashMap<String, Object>) data;
                       name=userdata.get("p_name").toString();
                       problem=userdata.get("p_problem").toString();
                       check(name,problem);
                       initRecyclerView();
                   }
               }
               else
               {
                   TextView textView;
                   textView=findViewById(R.id.no_meetings);
                   textView.setVisibility(View.VISIBLE);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void check(String name, String problem) {
        int m=Patient_Problem.size();
        boolean flag=true;
        for(int i=0;i<m;i++)
        {
            if(name.equals(Patient_Name.get(i))&&problem.equals(Patient_Problem.get(i)))
            {
                flag=false;
            }
        }
        if(flag)
        {
            Patient_Name.add(name);
            Patient_Problem.add(problem);
        }
    }

    private void initRecyclerView() {
        continueMeeting=new ContinueMeeting(Fix_Meeting_step3.this,Patient_Name,Patient_Problem);
        recyclerView.setAdapter(continueMeeting);
        continueMeeting.setOnItemClickListener(new ContinueMeeting.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Pname=Patient_Name.get(position);
                Problem=Patient_Problem.get(position);
                DoAlter();
            }
        });
    }

    private void retrieveData() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Doctors_Meeting_Time").child(Did);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                   fromTime=hashMap.get("fromTime").toString();
                   toTime=hashMap.get("toTime").toString();
                   lastDateChange=hashMap.get("lastChangeDate").toString();
                   slot_details=hashMap.get("slot_details").toString();
                   slots_no=hashMap.get("no_of_slots").toString();
                   try {
                       json=new JSONObject(slot_details);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               else
               {
                   check=false;
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

    public void NextStep(View view) {
      TextView Pname1,Problem1;
      Pname1=findViewById(R.id.pname);
      Problem1=findViewById(R.id.problem);
      if(TextUtils.isEmpty(Pname1.getText()))
      {
          Pname1.setError("Please enter your name");
      }
      else if(TextUtils.isEmpty(Problem1.getText()))
      {
          Problem1.setError("Please enter yoour problem");
      }
      else
      {
           Pname=Pname1.getText().toString();
           Problem=Problem1.getText().toString();
           if(check)
               DoAlter();
           else
               Toast.makeText(Fix_Meeting_step3.this,"Doctor Slots Details are not available",Toast.LENGTH_SHORT).show();
      }
    }

    private void DoAlter() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(Fix_Meeting_step3.this).create();
        LayoutInflater inflater = Fix_Meeting_step3.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.select_date_time, null);
        TextView Date;
        ImageView calendar=dialogView.findViewById(R.id.calendar);
        Spinner spinner;
        Date=dialogView.findViewById(R.id.date);
        spinner=dialogView.findViewById(R.id.slots);
        spinner.setAdapter(new ArrayAdapter<String>(Fix_Meeting_step3.this, android.R.layout.simple_spinner_dropdown_item,Avaiable_Slots));
        Button Save=dialogView.findViewById(R.id.save);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Fix_Meeting_step3.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               monthOfYear+=1;
                               String mt;
                               if(monthOfYear<10)
                               {
                                  mt="0"+String.valueOf(monthOfYear);
                               }
                               else
                               {
                                   mt=String.valueOf(monthOfYear);
                               }
                               if(dayOfMonth<10) {
                                   String str = "0" + String.valueOf(dayOfMonth);
                                   final_date = str + "/" + mt + "/" + String.valueOf(year);
                                   child_date = str + "-" + mt + "-" + String.valueOf(year);
                               }
                               else
                               {
                                   final_date = String.valueOf(dayOfMonth) + "/" + mt + "/" + String.valueOf(year);
                                   child_date = String.valueOf(dayOfMonth) + "-" + mt + "-" + String.valueOf(year);
                               }
                               Date.setText(final_date);
                                try {
                                    Avaiable_Slots.clear();
                                    Avaiable_Slots.add("Select a slot");
                                    String sp=json.getString(final_date);
                                    JSONObject json1=new JSONObject(sp);
                                    int n=Integer.parseInt(slots_no);
                                    for(int i=0;i<n;i++)
                                    {
                                        String slot="slot"+String.valueOf(i+1);
                                        if(json1.getString(slot).equals("false"))
                                        {
                                            String start=String.valueOf(Integer.parseInt(fromTime.substring(0,2))+i)+fromTime.substring(2,5);
                                            String end=String.valueOf(Integer.parseInt(fromTime.substring(0,2))+i+1)+fromTime.substring(2,5);
                                            String time=start+" - "+end;
                                            Avaiable_Slots.add(time);
                                            Slots_map.put(time,slot);
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(Avaiable_Slots.size()==1)
                                {
                                    Avaiable_Slots.clear();
                                    Avaiable_Slots.add(0,"No slots available");
                                }
                                spinner.setAdapter(new ArrayAdapter<String>(Fix_Meeting_step3.this, android.R.layout.simple_spinner_dropdown_item,Avaiable_Slots));
                            }
                        }, mYear, mMonth, mDay);
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    mindate=f.parse(lastDateChange);
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(mindate);
                    calendar.add(Calendar.DAY_OF_YEAR,6);
                    maxdate=f.parse(f.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date todayDate = new Date();
                datePickerDialog.getDatePicker().setMinDate(todayDate.getTime());
                datePickerDialog.getDatePicker().setMaxDate(maxdate.getTime());
                datePickerDialog.show();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  int index=spinner.getSelectedItemPosition();
                  final_slot=Avaiable_Slots.get(index);
                  if(final_slot.equals("Select a slot"))
                  {
                      Toast.makeText(Fix_Meeting_step3.this,"Please select a slot",Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                      String slot1=Slots_map.get(final_slot);
                      try {
                          String sp=json.getString(final_date);
                          JSONObject json1=new JSONObject(sp);
                          json1.remove(slot1);
                          json1.put(slot1,"true");
                          json.remove(json1.toString());
                          json.put(final_date,json1.toString());
                          Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctors_Meeting_Time").child(Did).child("slot_details").setValue(json.toString());
                          dialogBuilder.dismiss();
                          Fix_the_Meeting();

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              }
              catch (Exception e)
              {
                  Toast.makeText(Fix_Meeting_step3.this,"No Operation Performed",Toast.LENGTH_SHORT).show();
                  dialogBuilder.dismiss();
              }

            }
        });
    }
    public void Fix_the_Meeting() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        Patient_Detail.add(Pid);
        Patient_Detail.add(Pname);
        Patient_Detail.add(Problem);
        Patient_Detail.add(doctor_name);
        Patient_Detail.add(final_slot);
        Patient_Detail.add(final_date);
        Patient_Detail.add(thisDate);
        Patient_Detail.add(currentTime);
        Patient_Detail.add(child_date);
        Doctor_Detail.add(arr.get(4));
        Doctor_Detail.add(arr.get(3));
        Doctor_Detail.add(Did);
        Doctor_Detail.add(arr.get(6));
        Intent intent=new Intent(this, MainPaymentActivity.class);
        intent.putStringArrayListExtra("Patient_Detail",Patient_Detail);
        intent.putStringArrayListExtra("Doctor_Detail",Doctor_Detail);
        startActivity(intent);
    }
}