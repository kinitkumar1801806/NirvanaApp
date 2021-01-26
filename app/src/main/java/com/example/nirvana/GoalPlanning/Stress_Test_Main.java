package com.example.nirvana.GoalPlanning;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.R;

public class Stress_Test_Main extends AppCompatActivity {
    RadioGroup a1_q1_radioGroup,a1_q2_radioGroup,a1_q3_radioGroup,a1_q4_radioGroup,a1_q5_radioGroup,
            a1_q6_radioGroup,a1_q7_radioGroup,a1_q8_radioGroup, a1_q9_radioGroup;
    public  String Result,LastTest;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_test_main);
        a1_q1_radioGroup=findViewById(R.id.a1_q1_radioGroup);
        a1_q2_radioGroup=findViewById(R.id.a1_q2_radioGroup);
        a1_q3_radioGroup=findViewById(R.id.a1_q3_radioGroup);
        a1_q4_radioGroup=findViewById(R.id.a1_q4_radioGroup);
        a1_q5_radioGroup=findViewById(R.id.a1_q5_radioGroup);
        a1_q6_radioGroup=findViewById(R.id.a1_q6_radioGroup);
        a1_q7_radioGroup=findViewById(R.id.a1_q7_radioGroup);
        a1_q8_radioGroup=findViewById(R.id.a1_q8_radioGroup);
        a1_q9_radioGroup=findViewById(R.id.a1_q9_radioGroup);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        LastTest=sharedPreferences.getString("LastIntroductionTest",null);
    }

    public void CheckTest(View view) {
                int count=0,selectedId;
                selectedId=a1_q1_radioGroup.getCheckedRadioButtonId();
                if(selectedId==-1)
                {
                    Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    RadioButton radioButton;
                    radioButton=a1_q1_radioGroup.findViewById(selectedId);
                    if(radioButton.getText().toString().equals("Not at all"))
                    {
                        count+=0;
                    }
                    else if(radioButton.getText().toString().equals("Several days"))
                    {
                        count+=1;
                    }
                    else if(radioButton.getText().toString().equals("More than half the number of days"))
                    {
                        count+=2;
                    }
                    else
                    {
                       count+=3;
                    }
                }
                selectedId=a1_q2_radioGroup.getCheckedRadioButtonId();
                if(selectedId==-1)
                {
                    Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    RadioButton radioButton;
                    radioButton=a1_q2_radioGroup.findViewById(selectedId);
                    if(radioButton.getText().toString().equals("Not at all"))
                    {
                        count+=0;
                    }
                    else if(radioButton.getText().toString().equals("Several days"))
                    {
                        count+=1;
                    }
                    else if(radioButton.getText().toString().equals("More than half the number of days"))
                    {
                        count+=2;
                    }
                    else
                    {
                        count+=3;
                    }
                }
            selectedId=a1_q3_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q3_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q4_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q4_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q5_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q5_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q6_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q6_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q7_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q7_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q8_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q8_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            selectedId=a1_q9_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(Stress_Test_Main.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q9_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Not at all"))
                {
                    count+=0;
                }
                else if(radioButton.getText().toString().equals("Several days"))
                {
                    count+=1;
                }
                else if(radioButton.getText().toString().equals("More than half the number of days"))
                {
                    count+=2;
                }
                else
                {
                    count+=3;
                }
            }
            if(count>=0&&count<=4)
            {
               Result="None";
            }
            else if(count>=5&&count<=9)
            {
                 Result="Mild";
            }
            else if(count>=10&&count<=14)
            {
                Result="Moderate";
            }
            else if(count>=15&&count<=19)
            {
                Result="Moderately Severe";
            }
            else
            {
                Result="Severe";
            }
            DoAlter();
        }

    private void DoAlter() {
        final AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater=this.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.stress_test_dialoguebox,null);
        TextView lastReport=view.findViewById(R.id.lastTestReport);
        if(LastTest==null)
        {
            lastReport.setText("No Past Record");
        }
        else
        {
            lastReport.setText(LastTest);
        }
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("LastIntroductionTest",Result);
        editor.apply();
        TextView currentReport=view.findViewById(R.id.currentTestReport);
        currentReport.setText(Result.toUpperCase());
        TextView currentModule=view.findViewById(R.id.currentModule);
        currentModule.setText("Introductory Module Report");
        Button FinishTest=view.findViewById(R.id.finishTest);
        Button NextModule=view.findViewById(R.id.nextModule);
        FinishTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
               overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
            }
        });
        NextModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Stress_Test_Main.this,DepressiveModule.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }
}