package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nirvana.Call.BaseActivity;
import com.example.nirvana.Call.SinchService;
import com.example.nirvana.Call.VideoCallScreenActivity;
import com.example.nirvana.Call.VoiceCallScreenActivity;
import com.example.nirvana.Patients.Meeting_Alresdy_fixed_step2;
import com.example.nirvana.Service.CallerName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;
import com.sinch.android.rtc.calling.Call;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StartMeetingActivity extends BaseActivity implements SinchService.StartFailedListener, PushTokenRegistrationCallback, UserRegistrationCallback {
    public String senderId,recieverId,UserName,link,Who;
    private static final String APP_KEY = "6a9ce4e2-e655-4a59-a2b0-b76c84132546";
    private static final String APP_SECRET ="2dEpHTchh0SLCsnYyv2gPw==";
    private static final String ENVIRONMENT ="clientapi.sinch.com";
    public static final String CALL_ID = "CALL_ID";
    private final int MULTIPLE_PERMISSION = 1;
    public String callType="";
    SinchClient sinchClient;
    Call call;
    private long mSigningSequence = 1;
    String[] permission={android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.MODIFY_AUDIO_SETTINGS, android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.INTERNET, android.Manifest.permission.BLUETOOTH, android.Manifest.permission.ACCESS_WIFI_STATE};
    //userName is the name of reciever
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_meeting);
        Intent intent=getIntent();
        senderId=intent.getStringExtra("SenderId");
        recieverId=intent.getStringExtra("ReceiverId");
        UserName=intent.getStringExtra("UserName");
        Who=intent.getStringExtra("Who");
        link=intent.getStringExtra("link");
    }

    public void Start_Voice_Call(View view) {
        callType = "voice";
        Check();
        SharedPreferences pref=getSharedPreferences("Sinch",MODE_PRIVATE);
        if (!pref.getBoolean("isLogin",false)) {
            if (!senderId.equals(getSinchServiceInterface().getUsername())) {
                getSinchServiceInterface().stopClient();
            }
            SharedPreferences.Editor ed=getSharedPreferences("Sinch",MODE_PRIVATE).edit();
            ed.putBoolean("isLogin",true);
            ed.apply();
            startClientAndMakeCall();
            getSinchServiceInterface().setUsername(senderId);
            UserController uc = Sinch.getUserControllerBuilder()
                    .context(getApplicationContext())
                    .applicationKey(APP_KEY)
                    .userId(senderId)
                    .environmentHost(ENVIRONMENT)
                    .build();
            uc.registerUser(this, this);
            UserController uc1 = Sinch.getUserControllerBuilder()
                    .context(getApplicationContext())
                    .applicationKey(APP_KEY)
                    .userId(senderId)
                    .environmentHost(ENVIRONMENT)
                    .build();
            uc1.registerUser(this, this);
        }
        else {
            startClientAndMakeCall();
            makeCall();
        }
    }

    public void Start_Video_Call(View view) {
        callType = "video";
        Check();
        SharedPreferences prefs=getSharedPreferences("Sinch",MODE_PRIVATE);
        if (!prefs.getBoolean("isLogin",false)) {
            if (!senderId.equals(getSinchServiceInterface().getUsername())) {
                getSinchServiceInterface().stopClient();
            }
            SharedPreferences.Editor ed=getSharedPreferences("Sinch",MODE_PRIVATE).edit();
            ed.putBoolean("isLogin",true);
            ed.apply();
            startClientAndMakeCall();
            getSinchServiceInterface().setUsername(senderId);
            UserController uc = Sinch.getUserControllerBuilder()
                    .context(getApplicationContext())
                    .applicationKey(APP_KEY)
                    .userId(senderId)
                    .environmentHost(ENVIRONMENT)
                    .build();
            uc.registerUser(this, this);

        }else{
            startClientAndMakeCall();
            makeCall();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation,R.anim.slide_in_bottom);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        SharedPreferences.Editor ed=getSharedPreferences("Sinch",MODE_PRIVATE).edit();
        ed.putBoolean("isLogin",true);
        ed.apply();
        startClientAndMakeCall();
    }
    private void startClientAndMakeCall() {
        // start Sinch Client, it'll result onStarted() callback from where the place call activity will be started
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient();
        }
    }
    @Override
    public void tokenRegistered() {
        startClientAndMakeCall();
    }

    @Override
    public void tokenRegistrationFailed(SinchError sinchError) {
        Toast.makeText(this, "Push token registration failed - incoming calls can't be received!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCredentialsRequired(ClientRegistration clientRegistration) {
        String toSign = senderId + APP_KEY + mSigningSequence + APP_SECRET;
        String signature;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(toSign.getBytes(StandardCharsets.UTF_8));
            signature = Base64.encodeToString(hash, Base64.DEFAULT).trim();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

        clientRegistration.register(signature, mSigningSequence++);
    }

    @Override
    public void onUserRegistered() {

    }

    @Override
    public void onUserRegistrationFailed(SinchError sinchError) {
        Toast.makeText(this, "Registration failed!", Toast.LENGTH_LONG).show();
    }
    public void Check()
    {
        if (ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.READ_PHONE_STATE) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.ACCESS_NETWORK_STATE) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.INTERNET) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.BLUETOOTH) + ContextCompat.checkSelfPermission(StartMeetingActivity.this,
                android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPhonePermission();
        }

    }
    private void requestPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_PHONE_STATE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.RECORD_AUDIO)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_NETWORK_STATE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.INTERNET)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.BLUETOOTH)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_WIFI_STATE)){
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(StartMeetingActivity.this,
                                    permission, MULTIPLE_PERMISSION);
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    permission,MULTIPLE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSION)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static boolean isNetworkAvailable(Context context) {

        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status", "Network is available : FALSE");
        return false;
    }
    public void SetCallerName()
    {
        CallerName callerName=new CallerName(
                senderId,
                Who
        );
        Task<Void> databaseReference= FirebaseDatabase.getInstance().getReference("CallerName").child(call.getCallId())
                .setValue(callerName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
    private void makeCall() {
        if(isNetworkAvailable(this))
        {
            String callId;
            if (callType.equals("voice")) {
                call=getSinchServiceInterface().callUser(recieverId);
                callId=call.getCallId();
                Intent intent=new Intent(this, VoiceCallScreenActivity.class);
                intent.putExtra("SenderId",senderId);
                intent.putExtra("UserName",UserName);
                intent.putExtra("Who",Who);
                intent.putExtra("link",link);
                intent.putExtra(CALL_ID, callId);
                SetCallerName();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            } else {
                call=getSinchServiceInterface().callUserVideo(recieverId);
                callId=call.getCallId();
                Intent intent=new Intent(this, VideoCallScreenActivity.class);
                intent.putExtra("SenderId",senderId);
                intent.putExtra("UserName",UserName);
                intent.putExtra("Who",Who);
                intent.putExtra("link",link);
                intent.putExtra(CALL_ID, callId);
                SetCallerName();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom,R.anim.no_animation);
            }
        }
        else
        {
            Toast.makeText(this,"No internet.Please make sure that you are connected to internet",Toast.LENGTH_SHORT).show();
        }
        }
    }