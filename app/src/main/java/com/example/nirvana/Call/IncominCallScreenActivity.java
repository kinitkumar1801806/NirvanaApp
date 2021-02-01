package com.example.nirvana.Call;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.bumptech.glide.Glide;
import com.example.nirvana.Gestures.SwipeUpGesture;
import com.example.nirvana.MusicPlayer.AudioPLayer;
import com.example.nirvana.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import java.util.HashMap;
import java.util.List;

import static com.example.nirvana.Call.SinchService.CALL_ID;

public class IncominCallScreenActivity extends BaseActivity {

    static final String TAG = IncominCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPLayer mAudioPlayer;
    private GestureDetectorCompat gestureDetectorCompat=null;
    public static final String ACTION_ANSWER = "answer";
    public static final String ACTION_IGNORE = "ignore";
    public static final String EXTRA_ID = "id";
    public static int MESSAGE_ID = 14;
    private String mAction,Id,name,link,Who;
    ImageView profile_image;
    TextView textViewRemoteUser;
    ImageView answerbtn,hangupbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomin_call_screen);
        TextView callState=findViewById(R.id.callState);
        answerbtn = findViewById(R.id.accept_swipe_btn);
        hangupbtn = findViewById(R.id.reject_swipe_btn);
        profile_image=findViewById(R.id.incoming_profile_image);
        textViewRemoteUser=findViewById(R.id.remoteUser);
        callState.setText("Incoming Voice Call");
        mAudioPlayer = new AudioPLayer(this);
        mAudioPlayer.playRingtone();

        Intent intent = getIntent();
        mCallId = intent.getStringExtra(CALL_ID);
        mAction = "";
        Animation Bounce=  new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0.0f,
                TranslateAnimation.ABSOLUTE, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -1.5f);
        Bounce.setDuration(800);
        Bounce.setRepeatCount(-1);
        Bounce.setRepeatMode(Animation.REVERSE);
        answerbtn.setAnimation(Bounce);
        hangupbtn.setAnimation(Bounce);
        getCallerName();
        answerbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SwipeUpGesture swipeUpGesture=new SwipeUpGesture();
                swipeUpGesture.setActivity(IncominCallScreenActivity.this,"answer");
                gestureDetectorCompat=new GestureDetectorCompat(IncominCallScreenActivity.this,swipeUpGesture);
                gestureDetectorCompat.onTouchEvent(event);
                return true;
            }
        });
        hangupbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SwipeUpGesture swipeUpGesture=new SwipeUpGesture();
                swipeUpGesture.setActivity(IncominCallScreenActivity.this,"hangup");
                gestureDetectorCompat=new GestureDetectorCompat(IncominCallScreenActivity.this,swipeUpGesture);
                gestureDetectorCompat.onTouchEvent(event);
                return true;
            }
        });
    }
public void getCallerName()
{
    DatabaseReference databaseReference= (DatabaseReference) FirebaseDatabase.getInstance().getReference("CallerName").child(mCallId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String,Object> hashMap=(HashMap)snapshot.getValue();
                    Id=(String)hashMap.get("Id");
                    Who=(String)hashMap.get("Who");
                    getCallerDetails();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}

    private void getCallerDetails() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(Who).child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                   name=hashMap.get("fname").toString()+" "+hashMap.get("lname").toString();
                   link=hashMap.get("link").toString();
                   textViewRemoteUser.setText(name);
                   if(link.equals("None"))
                   {
                       Glide.with(IncominCallScreenActivity.this).load(R.drawable.green_person_logo).into(profile_image);
                   }
                   else
                   {
                       Glide.with(IncominCallScreenActivity.this).load(link).into(profile_image);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra(CALL_ID) != null) {
                mCallId = getIntent().getStringExtra(CALL_ID);
            }
            final int id = intent.getIntExtra(EXTRA_ID, -1);
            if (id > 0) {
                NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);
            }
            mAction = intent.getAction();
        }
    }


    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());

            if (ACTION_ANSWER.equals(mAction)) {
                mAction = "";
                answerClicked();
            } else if (ACTION_IGNORE.equals(mAction)) {
                mAction = "";
                declineClicked();
            }

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    public void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            Log.d(TAG, "Answering call");
            call.answer();
            Intent intent=new Intent(this, VoiceCallScreenActivity.class);
            intent.putExtra("SenderId",Id);
            intent.putExtra("UserName",name);
            if(Who.equals("Doctors"))
            {
                Who="Patient";
            }
            else
            {
                Who="Doctors";
            }
            intent.putExtra("Who",Who);
            intent.putExtra("link",link);
            intent.putExtra(CALL_ID, mCallId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
        } else {
            finish();
        }
    }

    public void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Toast.makeText(IncominCallScreenActivity.this,"Call Ended : "+cause.toString(),Toast.LENGTH_SHORT).show();
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {

            Toast.makeText(IncominCallScreenActivity.this,"Call Established",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(IncominCallScreenActivity.this,"Call Processing",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // no need to implement for managed push
        }

    }
}
