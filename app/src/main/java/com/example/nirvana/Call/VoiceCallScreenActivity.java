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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.nirvana.Model.Rating_Model;
import com.example.nirvana.MusicPlayer.AudioPLayer;
import com.example.nirvana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class VoiceCallScreenActivity extends BaseActivity {

    ImageView rejectSwipeBtn;
    private TextView mCallState,mcallDuration;
    private TextView mCallerName;
    ImageView profileImageCalling;
    DatabaseReference firebaseDatabase1;
    ImageView micbutton,speakerbutton,Recorder;
    static final String TAG = VoiceCallScreenActivity.class.getSimpleName();
    public Integer rating=0;
    private AudioPLayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
    String receiverUserId="",receiverUserImage="",receiverUserName="",senderUserId="";
    private String mCallId,Who,review,receiverId,patientName,key,Calling,name;
    MediaRecorder recorder;
    File direct;
    Intent intent;
    int totalrating,ratedby;

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            VoiceCallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        micbutton=findViewById(R.id.mute);
        speakerbutton=findViewById(R.id.speakerbutton);
        mAudioPlayer = new AudioPLayer(this);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        mcallDuration=findViewById(R.id.callDuration);
        Recorder=findViewById(R.id.video_on);
        rejectSwipeBtn=findViewById(R.id.hangupButton);
        profileImageCalling=findViewById(R.id.profile_image_calling);
        intent=getIntent();
        senderUserId= intent.getStringExtra("SenderId");
        Who=intent.getStringExtra("Who");
        review=intent.getStringExtra("review");
        mCallId = intent.getStringExtra(SinchService.CALL_ID);
        receiverUserName=intent.getStringExtra("UserName");
        receiverUserImage=intent.getStringExtra("link");
        receiverId=intent.getStringExtra("receiverId");
        key=intent.getStringExtra("key");
        Calling=intent.getStringExtra("calling");
        rejectSwipeBtn.setOnClickListener(new View.OnClickListener() {
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
                if(micbutton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.baseline_mic_off_white_18dp).getConstantState())
                {
                    micbutton.setImageResource(R.drawable.baseline_mic_white_18dp);
                    audioManager.setMicrophoneMute(true);
                }
                else{
                    micbutton.setImageResource(R.drawable.baseline_mic_off_white_18dp);
                    audioManager.setMicrophoneMute(false);
                }
            }
        });
        speakerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                if(speakerbutton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_baseline_phone_in_talk_24).getConstantState())
                {
                    Recorder.setImageResource(R.drawable.ic_baseline_phone_24);
                    audioManager.setSpeakerphoneOn(true);

                }
                else{
                    Recorder.setImageResource(R.drawable.ic_baseline_phone_in_talk_24);
                    audioManager.setSpeakerphoneOn(false);
                }
            }
        });
        Recorder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(Calling!=null)
                {
                    if(Calling.equals("Doctors"))
                    {
                       direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+receiverId+"/");
                    }
                    else
                    {
                       direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+senderUserId+"/");
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
                        File direct = new File(Environment.getExternalStorageDirectory() + "/Nirvana/Recordings/Voice Recordings/"+senderUserId+"/");
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
                if(speakerbutton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.ic_baseline_videocam_white).getConstantState())
                {
                    speakerbutton.setImageResource(R.drawable.ic_baseline_phone_24);
                    try {
                        File filePath = new File(direct,name);
                        if (!filePath.exists()) {
                            if (!filePath.createNewFile()) {
                                Toast.makeText(VoiceCallScreenActivity.this, "Can't create file", Toast.LENGTH_LONG).show();
                            }
                        }
                        recorder.setOutputFile(filePath);
                        recorder.prepare();
                        recorder.start();
                        Toast.makeText(VoiceCallScreenActivity.this,"Recording Started",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    speakerbutton.setImageResource(R.drawable.ic_baseline_videocam_white);
                    if(recorder!=null)
                    {
                        recorder.release();
                        recorder=null;
                        Toast.makeText(VoiceCallScreenActivity.this,"Recording is  saved to"+direct+name,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Recorder.setEnabled(false);
        micbutton.setEnabled(false);
        speakerbutton.setEnabled(false);
        getAndSetUserProfileInfo();

    }

    private void getAndSetUserProfileInfo() {
            mCallerName.setText(receiverUserName);
            if(receiverUserImage.equals("None"))
            {
                Glide.with(this).load(R.drawable.green_person_logo).into(profileImageCalling);
            }
            else
            {
                Glide.with(this).load(receiverUserImage).into(profileImageCalling);
            }
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            mCallState.setText(call.getState().toString());
        } else {
           Toast.makeText(VoiceCallScreenActivity.this,"Started with invalid callId, aborting.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
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
    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mcallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            if(recorder!=null)
            {
                recorder.release();
                recorder=null;
                Toast.makeText(VoiceCallScreenActivity.this,"Recording is  saved to"+direct+name,Toast.LENGTH_SHORT).show();
            }
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

        @Override
        public void onCallEstablished(Call call) {
            if(Who.equals("Doctors"))
            {
                Recorder.setEnabled(true);
            }
            mAudioPlayer.stopProgressTone();
            mCallerName.setText(receiverUserName);
            micbutton.setEnabled(true);
            speakerbutton.setEnabled(true);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.disableSpeaker();
            mCallState.setVisibility(View.GONE);
            mcallDuration.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCallProgressing(Call call) {
            mAudioPlayer.playProgressTone();
            mCallState.setText("Ringing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // no need to implement if you use managed push

        }

    }
    public void Review()
    {
        final AlertDialog alertDialog=new AlertDialog.Builder(VoiceCallScreenActivity.this).create();
        LayoutInflater layoutInflater=VoiceCallScreenActivity.this.getLayoutInflater();
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
                    Toast.makeText(VoiceCallScreenActivity.this,"Please select the rating",Toast.LENGTH_SHORT).show();
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
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Patient").child(receiverId);
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
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(senderUserId).child(receiverId);
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
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Patient").child(senderUserId);
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
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(receiverId).child(senderUserId);
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
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Patient").child(receiverId);
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
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(senderUserId).child(receiverId);
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
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Patient").child(senderUserId);
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
                                    DatabaseReference databaseReference1=firebaseDatabase.getReference().child("Doctors_Reviews").child(receiverId).child(senderUserId);
                                    databaseReference1.setValue(rating_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(Calling==null)
                                            {
                                               if(Who.equals("Doctors"))
                                               {
                                                   firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(senderUserId);
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
                                                    firebaseDatabase1=FirebaseDatabase.getInstance().getReference("Doctors").child(senderUserId);
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
                Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child(senderUserId).child(receiverId)
                        .child(key).child("complete").setValue("1");
                Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(receiverId).child(senderUserId)
                        .child(key).child("complete").setValue("1");
            }
            else
            {
                Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child(receiverId).child(senderUserId)
                        .child(key).child("complete").setValue("1");
                Task<Void> databaseReference1=FirebaseDatabase.getInstance().getReference("Patient_Meetings").child(senderUserId).child(receiverId)
                        .child(key).child("complete").setValue("1");
            }
        }
    }
}
