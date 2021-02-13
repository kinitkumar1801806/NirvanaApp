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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.R;

public class DepressiveModule extends AppCompatActivity {
    RadioGroup a1_q1_radioGroup,a1_q2_radioGroup,a2_q1_radioGroup,a2_q2_radioGroup,a3_q1_radioGroup,a3_q2_radioGroup,
            a3_q3_radioGroup,a3_q4_radioGroup,a3_q5_radioGroup,a3_q6_radioGroup,a3_q7_radioGroup,a3_q8_radioGroup,
            a3_q9_radioGroup,a3_q10_radioGroup,a3_q11_radioGroup,a4_q1_radioGroup,a4_q2_radioGroup;
    RelativeLayout a1_q1_layout,a1_q2_layout,a2_q1_layout,a2_q2_layout,a4_q1_layout,a4_q2_layout;
    int a1=0,a2=0,a4=0;
    SharedPreferences sharedPreferences;
    String Result,LastTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depressive_module);
        a1_q1_radioGroup=findViewById(R.id.a1_q1_radioGroup);
        a1_q2_radioGroup=findViewById(R.id.a1_q2_radioGroup);
        a2_q1_radioGroup=findViewById(R.id.a2_q1_radioGroup);
        a2_q2_radioGroup=findViewById(R.id.a2_q2_radioGroup);
        a3_q1_radioGroup=findViewById(R.id.a3_q1_radioGroup);
        a3_q2_radioGroup=findViewById(R.id.a3_q2_radioGroup);
        a3_q3_radioGroup=findViewById(R.id.a3_q3_radioGroup);
        a3_q4_radioGroup=findViewById(R.id.a3_q4_radioGroup);
        a3_q5_radioGroup=findViewById(R.id.a3_q5_radioGroup);
        a3_q6_radioGroup=findViewById(R.id.a3_q6_radioGroup);
        a3_q7_radioGroup=findViewById(R.id.a3_q7_radioGroup);
        a3_q8_radioGroup=findViewById(R.id.a3_q8_radioGroup);
        a3_q9_radioGroup=findViewById(R.id.a3_q9_radioGroup);
        a3_q10_radioGroup=findViewById(R.id.a3_q10_radioGroup);
        a3_q11_radioGroup=findViewById(R.id.a3_q11_radioGroup);
        a4_q1_radioGroup=findViewById(R.id.a4_q1_radioGroup);
        a4_q2_radioGroup=findViewById(R.id.a4_q2_radioGroup);
        a1_q1_layout=findViewById(R.id.a1_q1_layout);
        a1_q2_layout=findViewById(R.id.a1_q2_layout);
        a2_q1_layout=findViewById(R.id.a2_q1_layout);
        a2_q2_layout=findViewById(R.id.a2_q2_layout);
        a4_q1_layout=findViewById(R.id.a4_q1_layout);
        a4_q2_layout=findViewById(R.id.a4_q2_layout);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        LastTest=sharedPreferences.getString("LastDepressiveTest",null);
        CheckWhichTestIsVisibile();
    }

    private void CheckWhichTestIsVisibile() {
        a1_q1_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.a1_q1_radioGroup_Yes)
                {
                    a1_q1_layout.animate().alpha(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            a1_q1_layout.setVisibility(View.GONE);
                            a1_q2_layout.setVisibility(View.VISIBLE);
                            a1_q2_layout.animate().alpha(1.0f).setDuration(1000);
                        }
                    });
                    a1=1;
                }
            }
        });
        a2_q1_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.a2_q1_radioGroup_Yes)
                {
                    a2_q1_layout.animate().alpha(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            a2_q1_layout.setVisibility(View.GONE);
                            a2_q2_layout.setVisibility(View.VISIBLE);
                            a2_q2_layout.animate().alpha(1.0f).setDuration(1000);
                        }
                    });
                    a2=1;
                }
            }
        });
        a4_q1_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.a4_q1_radioGroup_Yes)
                {
                    a4_q1_layout.animate().alpha(0.0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            a4_q1_layout.setVisibility(View.GONE);
                            a4_q2_layout.setVisibility(View.VISIBLE);
                            a4_q2_layout.animate().alpha(1.0f).setDuration(1000);
                        }
                    });
                    a4=1;
                }
            }
        });
    }

    public void CheckTest(View view) {
        int count=0,selectedId;
        boolean a4_q2_Yes=false;
        if(a1==1)
        {
            selectedId=a1_q2_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a1_q2_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Yes"))
                {
                    count++;
                }
            }
        }
        else
        {
            selectedId=a1_q1_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(a2==1)
        {
            selectedId=a2_q2_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a2_q2_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Yes"))
                {
                    count++;
                }
            }
        }
        else
        {
            selectedId=a2_q1_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        selectedId=a3_q1_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q1_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q2_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q2_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q3_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q3_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q4_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q4_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q5_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q5_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q6_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q6_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q7_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q7_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q8_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q8_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q9_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q9_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q10_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q10_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        selectedId=a3_q11_radioGroup.getCheckedRadioButtonId();
        if(selectedId==-1)
        {
            Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            RadioButton radioButton;
            radioButton=a3_q11_radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Yes"))
            {
                count++;
            }
        }
        if(a4==1)
        {
            selectedId=a4_q2_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                RadioButton radioButton;
                radioButton=a4_q2_radioGroup.findViewById(selectedId);
                if(radioButton.getText().toString().equals("Yes"))
                {
                    a4_q2_Yes=true;
                }
            }
        }
        else
        {
            selectedId=a4_q1_radioGroup.getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(DepressiveModule.this,"Please answer all question",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(count>=5)
        {

            Result="Current Major Depressive Episode";
        }
        if(a4_q2_Yes)
        {
            Result="Recurring Major Depressive Episode";
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
        editor.putString("LastDepressiveTest",Result);
        editor.apply();
        TextView currentReport=view.findViewById(R.id.currentTestReport);
        currentReport.setText(Result.toUpperCase());
        TextView currentModule=view.findViewById(R.id.currentModule);
        currentModule.setText("MAJOR DEPRESSIVE EPISODE");
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
                /*Intent intent=new Intent(DepressiveModule.this,SuicidalityModule.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);*/
                Toast.makeText(DepressiveModule.this,"No next mocule available at this time",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }
}