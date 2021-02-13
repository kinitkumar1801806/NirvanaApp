package com.example.nirvana.Call;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.nirvana.Model.Rating_Model;
import com.example.nirvana.MusicPlayer.AudioPLayer;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class VideoCallScreenActivity extends BaseActivity {

    static final String TAG = VideoCallScreenActivity.class.getSimpleName();
    static final String ADDED_LISTENER = "addedListener";
    static final String VIEWS_TOGGLED = "viewsToggled";
    MediaRecorder recorder;
    private AudioPLayer mAudioPlayer;
    public Integer rating=0;
    private String mCallId,recieverImage,recieverUsername,name,Who,senderId,review,receiverId,patientName,key,Calling;
    private boolean mAddedListener = false;
    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    ImageView micbutton,videobutton,endCallButton,CameraSwap,profileImage;
    Call call1;
    File direct;
    DatabaseReference firebaseDatabase1;
    int totalrating,ratedby;
    TextView mCallState,RecieverUserName;
    boolean mToggleVideoViewPositions = true;
    RelativeLayout remoteVideo,localVideo;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
        savedInstanceState.putBoolean(VIEWS_TOGGLED, mToggleVideoViewPositions);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
        mToggleVideoViewPositions = savedInstanceState.getBoolean(VIEWS_TOGGLED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_call_screen);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        senderId=intent.getStringExtra("SenderId");
        Who=intent.getStringExtra("Who");
        recieverImage=intent.getStringExtra("link");
        recieverUsername=intent.getStringExtra("UserName");
        review=intent.getStringExtra("review");
        receiverId=intent.getStringExtra("receiverId");
        key=intent.getStringExtra("key");
        Calling=intent.getStringExtra("calling");
        mAudioPlayer = new AudioPLayer(this);
        endCallButton=findViewById(R.id.hangupButton);
        micbutton=findViewById(R.id.micbutton);
        videobutton=findViewById(R.id.video_off);
        CameraSwap=findViewById(R.id.camera_swap);
        profileImage=findViewById(R.id.profile_image_calling);
        RecieverUserName=findViewById(R.id.receiver_name);
        mCallState=findViewById(R.id.callState);
        StartRecording();
        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                if(micbutton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.baseline_mic_white_18dp).getConstantState())
                {
                    micbutton.setImageResource(R.drawable.baseline_mic_off_white_18dp);
                    audioManager.setMicrophoneMute(true);
                }
                else{
                    micbutton.setImageResource(R.drawable.baseline_mic_white_18dp);
                    audioManager.setMicrophoneMute(false);
                }
            }
        });
        videobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videobutton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_videocam_white_24px).getConstantState())
                {
                    videobutton.setImageResource(R.drawable.ic_baseline_videocam_off_24);
                    call1.pauseVideo();

                }
                else{
                    videobutton.setImageResource(R.drawable.ic_videocam_white_24px);
                    call1.resumeVideo();
                }
            }
        });
        CameraSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             VideoController vc=getSinchServiceInterface().getVideoController();
             vc.toggleCaptureDevicePosition();
            }
        });
        videobutton.setEnabled(false);
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        RecieverUserName.setText(recieverUsername);
        if(recieverImage.equals("None"))
        {
            Glide.with(this).load(R.drawable.green_person_logo).into(profileImage);
        }
        else
        {
            Glide.with(this).load(recieverImage).into(profileImage);
        }
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
        updateUI();
    }

    private void updateUI() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (call.getDetails().isVideoOffered()) {
                if (call.getState() == CallState.ESTABLISHED) {
                    videobutton.setVisibility(View.VISIBLE);
                    setVideoViewsVisibility(true, true);
                } else {
                    setVideoViewsVisibility(true, false);
                }
            }
        } else {
            setVideoViewsVisibility(false, false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        removeVideoViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        Complete();
        if(Who.equals("Patient"))
        {
            Review();
        }
        else
        {
            hangup();
        }
    }
    public void hangup()
    {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
    }

    private ViewGroup getVideoView(boolean localView) {
        if (mToggleVideoViewPositions) {
            localView = !localView;
        }
        return (ViewGroup) (localView ? findViewById(R.id.localVideo) : findViewById(R.id.remoteVideo));
    }

    private void addLocalView() {
        if (mLocalVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc =getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ViewGroup localView = getVideoView(true);
                localView.addView(vc.getLocalView());
                localView.setOnClickListener((View v) -> {
                    Call call = getSinchServiceInterface().getCall(mCallId);
                    if(call.getState()==CallState.ESTABLISHED) {
                        vc.toggleCaptureDevicePosition();
                    }
                });
                mLocalVideoViewAdded = true;
                vc.setLocalVideoZOrder(!mToggleVideoViewPositions);
            });
        }
    }
    private void addRemoteView() {
        if (mRemoteVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ViewGroup remoteView = getVideoView(false);
                remoteView.addView(vc.getRemoteView());
                remoteView.setOnClickListener((View v) -> {
                    Call call = getSinchServiceInterface().getCall(mCallId);
                    if(call.getState()==CallState.ESTABLISHED)
                    {
                        removeVideoViews();
                        mToggleVideoViewPositions = !mToggleVideoViewPositions;
                        addRemoteView();
                        addLocalView();
                    }
                });
                mRemoteVideoViewAdded = true;
                vc.setLocalVideoZOrder(!mToggleVideoViewPositions);
            });
        }
    }


    private void removeVideoViews() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ((ViewGroup)(vc.getRemoteView().getParent())).removeView(vc.getRemoteView());
                ((ViewGroup)(vc.getLocalView().getParent())).removeView(vc.getLocalView());
                mLocalVideoViewAdded = false;
                mRemoteVideoViewAdded = false;
            });
        }
    }


    private void setVideoViewsVisibility(final boolean localVideoVisibile, final boolean remoteVideoVisible) {
        if (getSinchServiceInterface() == null)
            return;
        if (!mRemoteVideoViewAdded) {
            addRemoteView();
        }
        if (!mLocalVideoViewAdded) {
            addLocalView();
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                vc.getLocalView().setVisibility(localVideoVisibile ? View.VISIBLE : View.GONE);
                vc.getRemoteView().setVisibility(remoteVideoVisible ? View.VISIBLE : View.GONE);
            });
        }
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended";
            recorder.release();
            recorder=null;
            Toast.makeText(VideoCallScreenActivity.this,"Recording is  saved to"+direct+name,Toast.LENGTH_SHORT).show();
            Complete();
            if(Who.equals("Patient"))
             {
                 Review();
             }
            else
            {
                hangup();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            videobutton.setEnabled(true);
            profileImage.setVisibility(View.GONE);
            mCallState.setVisibility(View.GONE);
            RecieverUserName.setVisibility(View.GONE);
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.enableSpeaker();
            if (call.getDetails().isVideoOffered()) {
                setVideoViewsVisibility(true, true);
                try {
                    recorder.prepare();
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                micbutton.setVisibility(View.VISIBLE);
                videobutton.setVisibility(View.VISIBLE);
                call1=call;
            }
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            mCallState.setText("Ringing..");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // No need to implement if you use managed push
        }

        @Override
        public void onVideoTrackAdded(Call call) {

        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }
    public void Review()
    {
        final AlertDialog alertDialog=new AlertDialog.Builder(VideoCallScreenActivity.this).create();
        LayoutInflater layoutInflater=VideoCallScreenActivity.this.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.review_layout,null);
        Button Save,Cancel;
        EditText Review;
        ImageView star1,star2,star3,star4,star5;
        Save=view.findViewById(R.id.save);
        Cancel=view.findViewById(R.id.cancel);
        Review=view.findViewById(R.id.editText);
        star1=view.findViewById(R.id.star1);
        star2=view.findViewById(R.id.star2);
        star3=view.findViewById(R.id.star3);
        star4=view.findViewById(R.id.star4);
        star5=view.findViewById(R.id.star5);
        alertDialog.setView(view);
        alertDialog.show();
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                hangup();
            }
        });
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.ic_baseline_star_24);
                star2.setImageResource(R.drawable.ic_star_black_24dp);
                star3.setImageResource(R.drawable.ic_star_black_24dp);
                star4.setImageResource(R.drawable.ic_star_black_24dp);
                star5.setImageResource(R.drawable.ic_star_black_24dp);
                rating=1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.ic_baseline_star_24);
                star2.setImageResource(R.drawable.ic_baseline_star_24);
                star3.setImageResource(R.drawable.ic_star_black_24dp);
                star4.setImageResource(R.drawable.ic_star_black_24dp);
                star5.setImageResource(R.drawable.ic_star_black_24dp);
                rating=2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.ic_baseline_star_24);
                star2.setImageResource(R.drawable.ic_baseline_star_24);
                star3.setImageResource(R.drawable.ic_baseline_star_24);
                star4.setImageResource(R.drawable.ic_star_black_24dp);
                star5.setImageResource(R.drawable.ic_star_black_24dp);
                rating=3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.ic_baseline_star_24);
                star2.setImageResource(R.drawable.ic_baseline_star_24);
                star3.setImageResource(R.drawable.ic_baseline_star_24);
                star4.setImageResource(R.drawable.ic_baseline_star_24);
                star5.setImageResource(R.drawable.ic_star_black_24dp);
                rating=4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.ic_baseline_star_24);
                star2.setImageResource(R.drawable.ic_baseline_star_24);
                star3.setImageResource(R.drawable.ic_baseline_star_24);
                star4.setImageResource(R.drawable.ic_baseline_star_24);
                star5.setImageResource(R.drawable.ic_baseline_star_24);
                rating=5;
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rating==0)
                {
                    Toast.makeText(VideoCallScreenActivity.this,"Please select the rating",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Review.getText()))
                {
                    Review.setError("Please give your reviews");
                }
                else
                {
                    String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);
                    if(Calling!=null)
                    {
                        if(Calling.equals("Doctors"))
                        {
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(receiverId);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                    String fname=hashMap.get("fname").toString();
                                    String lname=hashMap.get("lname").toString();
                                    patientName=fname+" "+lname;
                                    Rating_Model rating_model=new Rating_Model(
                                            patientName,
                                            String.valueOf(rating),
                                            Review.getText().toString(),
                                            thisDate,
                                            currentTime
                                    );
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(senderId).child(receiverId);
                                    databaseReference1.setValue(rating_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            hangup();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else
                        {
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(senderId);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                    String fname=hashMap.get("fname").toString();
                                    String lname=hashMap.get("lname").toString();
                                    patientName=fname+" "+lname;
                                    Rating_Model rating_model=new Rating_Model(
                                            patientName,
                                            String.valueOf(rating),
                                            Review.getText().toString(),
                                            thisDate,
                                            currentTime
                                    );
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(receiverId).child(senderId);
                                    databaseReference1.setValue(rating_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            hangup();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                    else
                    {
                        if(Who.equals("Doctors"))
                        {
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(receiverId);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                    String fname=hashMap.get("fname").toString();
                                    String lname=hashMap.get("lname").toString();
                                    patientName=fname+" "+lname;
                                    Rating_Model rating_model=new Rating_Model(
                                            patientName,
                                            String.valueOf(rating),
                                            Review.getText().toString(),
                                            thisDate,
                                            currentTime
                                    );
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(senderId).child(receiverId);
                                    databaseReference1.setValue(rating_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            hangup();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else
                        {
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Patient").child(senderId);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                                    String fname=hashMap.get("fname").toString();
                                    String lname=hashMap.get("lname").toString();
                                    patientName=fname+" "+lname;
                                    Rating_Model rating_model=new Rating_Model(
                                            patientName,
                                            String.valueOf(rating),
                                            Review.getText().toString(),
                                            thisDate,
                                            currentTime
                                    );
                                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(receiverId).child(senderId);
                                    databaseReference1.setValue(rating_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(Calling==null)
                                            {
                                                if(Who.equals("Doctors"))
                                                {
                                                    firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(senderId);
                                                }
                                                else
                                                {
                                                    firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(receiverId);
                                                }
                                            }
                                            else
                                            {
                                                if(Calling.equals("Doctors"))
                                                {
                                                    firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(senderId);
                                                }
                                                else
                                                {
                                                    firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(receiverId);
                                                }
                                            }
                                            firebaseDatabase1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    HashMap<String,Object> hashMap1= (HashMap<String, Object>) snapshot.getValue();
                                                    totalrating=Integer.parseInt(hashMap1.get("total_rating").toString());
                                                    ratedby=Integer.parseInt(hashMap1.get("rated_by").toString());
                                                    totalrating+=rating;
                                                    ratedby++;
                                                    firebaseDatabase1.child("total_rating").setValue(String.valueOf(totalrating));
                                                    firebaseDatabase1.child("rated_by").setValue(String.valueOf(ratedby));
                                                    hangup();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
        });
    }
    public void Complete()
    {
       if(Calling==null)
       {
           if(Who.equals("Doctors"))
           {
               Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child(senderId).child(receiverId)
                       .child(key).child("complete").setValue("1");
               Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(receiverId).child(senderId)
                       .child(key).child("complete").setValue("1");
           }
           else
           {
               Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child(receiverId).child(senderId)
                       .child(key).child("complete").setValue("1");
               Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(senderId).child(receiverId)
                       .child(key).child("complete").setValue("1");
           }
       }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void StartRecording()
    {
        if(Calling!=null)
        {
            if(Calling.equals("Doctors"))
            {
                direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+receiverId+"/");
            }
            else
            {
               direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+senderId+"/");
            }
        }
        else
        {
            if(Who.equals("Doctors"))
            {
                direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+receiverId+"/");
            }
            else
            {
               direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+senderId+"/");
            }
        }
        if(!direct.exists()) {
            direct.mkdirs(); //directory is created;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date todaydate = new Date();
        String uuid = UUID.randomUUID().toString();
        String thisDate = simpleDateFormat.format(todaydate);
        name="/"+uuid+thisDate+".mp3";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                File filePath = new File(direct,name);
                if (!filePath.exists()) {
                    if (!filePath.createNewFile()) {
                        Toast.makeText(VideoCallScreenActivity.this, "Can't create file", Toast.LENGTH_LONG).show();
                    }
                }
                recorder.setOutputFile(filePath);
                Toast.makeText(VideoCallScreenActivity.this,"Recording Started",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
