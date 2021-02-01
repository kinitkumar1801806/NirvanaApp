package com.example.nirvana.Gestures;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.nirvana.Call.IncominCallScreenActivity;
import com.example.nirvana.GoalPlanning.StressTest;
import com.example.nirvana.GoalPlanning.Stress_Test_Main;

public class SwipeUpGesture extends GestureDetector.SimpleOnGestureListener {
    private static int MIN_SWIPE_DISTANCEY = 0;
    private static int MAX_SWAP_DISTANCEY = 1000;
    private IncominCallScreenActivity activity=null;
    private String type=null;
    public IncominCallScreenActivity getActivity()
    {
        return activity;
    }

    public void setActivity(IncominCallScreenActivity activity,String type) {
        this.activity = activity;
        this.type=type;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaY=e1.getY()-e2.getY();
        float deltaYAbs=Math.abs(deltaY);
        if(deltaYAbs>=MIN_SWIPE_DISTANCEY || deltaYAbs<=MAX_SWAP_DISTANCEY)
        {
            if(deltaY>0)
            {
               if(type.equals("answer"))
               {
                   this.activity.answerClicked();
               }
               else
               {
                   this.activity.declineClicked();
               }
            }
        }
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toast.makeText(activity,"Single Tap",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(activity,"Double Tap",Toast.LENGTH_SHORT).show();
        return true;
    }
}