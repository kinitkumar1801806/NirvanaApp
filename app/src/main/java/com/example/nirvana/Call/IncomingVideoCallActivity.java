package com.example.nirvana.Call;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nirvana.MusicPlayer.AudioPLayer;
import com.example.nirvana.R;
import com.google.firebase.database.DatabaseReference;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

public class IncomingVideoCallActivity extends BaseActivity {

    static final String TAG = IncominCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPLayer mAudioPlayer;
    private boolean mAcceptVideo = true;

    public static final String ACTION_ANSWER = "answer";
    public static final String ACTION_IGNORE = "ignore";
    public static final String EXTRA_ID = "id";
    public static int MESSAGE_ID = 14;
    private String mAction;
    Animation answer_bounce,hangup_bounce;
    ImageView answerbtn,hangupbtn;
    DatabaseReference userRef;
    TextView remoteUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomin_call_screen);

        TextView callState=findViewById(R.id.callState);
         answerbtn =  findViewById(R.id.accept_swipe_btn);
         hangupbtn =  findViewById(R.id.reject_swipe_btn);
        remoteUser = findViewById(R.id.remoteUser);
        callState.setText("Incoming Video Call");

        mAudioPlayer = new AudioPLayer(this);
        mAudioPlayer.playRingtone();

        Intent intent = getIntent();
        mCallId = intent.getStringExtra(SinchService.CALL_ID);
        mAction = "";
        answer_bounce= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        answerbtn.startAnimation(answer_bounce);
        answer_bounce.setRepeatCount(Animation.INFINITE);
        hangup_bounce=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        hangupbtn.startAnimation(hangup_bounce);
        hangup_bounce.setRepeatCount(Animation.INFINITE);
        answerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerClicked();
            }
        });
       hangupbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               declineClicked();
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra(SinchService.CALL_ID) != null) {
                mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
            }
            final int id = intent.getIntExtra(EXTRA_ID, -1);
            if (id > 0) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, VideoCallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // No need to implement for managed push
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
            // and pass it to the CallScreenActivity via Intent and mAcceptVideo
            mAcceptVideo = true;
        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }
    }
}
