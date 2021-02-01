package com.example.nirvana.GoalPlanning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.example.nirvana.Gestures.SwipeUpGesture;
import com.example.nirvana.Niri;
import com.example.nirvana.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StressTest extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_test);
        floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StressTest.this,Niri.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
                v.clearAnimation();
            }
        });

    }
    public void Start_Test(View view) {
        Intent intent=new Intent(this,Stress_Test_Main.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
    }

}