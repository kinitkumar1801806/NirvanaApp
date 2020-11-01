package com.example.nirvana.GoalPlanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nirvana.R;

public class GoalPlanning extends AppCompatActivity {
    EditText title;
    EditText time;
    EditText info;
    Button addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_planning);

        title= findViewById(R.id.etTitle);
        time= findViewById(R.id.etTime);
        info= findViewById(R.id.etDescription);
        addEvent= findViewById(R.id.btnAdd);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !time.getText().toString().isEmpty() && !info
                        .getText().toString().isEmpty()) {

                    Intent intent= new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, info.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, time.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY,"true");
                    intent.putExtra(Intent.EXTRA_EMAIL,"shreyashikumari2019@gmail.com, priyanshkharesln@gmail.com");
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    }else{
                        Toast.makeText(GoalPlanning.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(GoalPlanning.this, "Please fill all the fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}