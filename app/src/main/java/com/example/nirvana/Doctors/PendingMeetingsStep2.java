package com.example.nirvana.Doctors;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Call.BaseActivity;
import com.example.nirvana.Call.SinchService;
import com.example.nirvana.Call.VideoCallScreenActivity;
import com.example.nirvana.Call.VoiceCallScreenActivity;
import com.example.nirvana.Call.MessageActivity;
import com.example.nirvana.R;
import com.example.nirvana.Service.CallerName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;
import com.sinch.android.rtc.calling.Call;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
public class PendingMeetingsStep2 extends BaseActivity implements SinchService.StartFailedListener, PushTokenRegistrationCallback, UserRegistrationCallback {
    private TextView nameView, ageView, genderView, problemView, dateView, timeView;
    private String phone, p_phone, date, date_period, time, name, patient_phone, time_period, link;
    ProgressDialog progressDialog;
    private static final String APP_KEY = "6a9ce4e2-e655-4a59-a2b0-b76c84132546";
    private static final String APP_SECRET ="2dEpHTchh0SLCsnYyv2gPw==";
    private static final String ENVIRONMENT ="clientapi.sinch.com";
    TextView patientName;
    static final String TAG = PendingMeetingsStep2.class.getSimpleName();
    private static String callType = "";
    private long mSigningSequence = 1;
    public static final String CALL_ID = "CALL_ID";
    private int MULTIPLE_PERMISSION = 1;
    Call call;
    MediaPlayer mediaPlayer;
    String[] permission = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.MODIFY_AUDIO_SETTINGS, android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.INTERNET, android.Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_WIFI_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_meetings_step2);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        patient_phone = intent.getStringExtra("phone1");
        Pending_Meeting_Step2_Fragment pending_meeting_step2_fragment = new Pending_Meeting_Step2_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, pending_meeting_step2_fragment);
        fragmentTransaction.commit();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

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
        makeCall();
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
        String toSign = phone + APP_KEY + mSigningSequence + APP_SECRET;
        String signature;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(toSign.getBytes("UTF-8"));
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doctor_Meetings").child("Fixed_Meetings")
                    .child(phone).child(patient_phone);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    name = (String) hashMap.get("p_name");
                    String age = (String) hashMap.get("p_age");
                    String gender = (String) hashMap.get("p_gender");
                    String problem = (String) hashMap.get("p_problem");
                    date = (String) hashMap.get("date");
                    time = (String) hashMap.get("time");
                    p_phone = (String) hashMap.get("p_phone");
                    nameView = findViewById(R.id.patient_name);
                    ageView = findViewById(R.id.patient_age);
                    genderView = findViewById(R.id.patient_gender);
                    problemView = findViewById(R.id.patient_problem);
                    patientName = findViewById(R.id.patient_name);
                    dateView = findViewById(R.id.date);
                    timeView = findViewById(R.id.time);
                    nameView.setText(name);
                    ageView.setText(age);
                    genderView.setText(gender);
                    problemView.setText(problem);
                    dateView.setText(date);
                    timeView.setText(time);
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                    try {
                        Date date1 = simpleDateFormat.parse(date);
                        Date date2 = simpleDateFormat.parse(thisDate);
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);
                        date_period = Long.toString(differenceDates);
                        Toast.makeText(PendingMeetingsStep2.this, date_period, Toast.LENGTH_SHORT).show();
                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                        Date date22 = format.parse(currentTime);
                        Date date11 = format.parse(time);
                        long difference1 = date22.getTime() - date11.getTime();
                        time_period = Long.toString(difference1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Patient").child(patient_phone);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    link = snapshot.child("link").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return "Finish";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PendingMeetingsStep2.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.video_call:
                callType = "video";
                VoiceCall();
                SharedPreferences prefs=getSharedPreferences("Sinch",MODE_PRIVATE);
                if (!prefs.getBoolean("isLogin",false)) {
                    if (!phone.equals(getSinchServiceInterface().getUsername())) {
                        getSinchServiceInterface().stopClient();
                    }
                    SharedPreferences.Editor ed=getSharedPreferences("Sinch",MODE_PRIVATE).edit();
                    ed.putBoolean("isLogin",true);
                    ed.apply();
                    startClientAndMakeCall();
                    getSinchServiceInterface().setUsername(phone);
                    UserController uc = Sinch.getUserControllerBuilder()
                            .context(getApplicationContext())
                            .applicationKey(APP_KEY)
                            .userId(phone)
                            .environmentHost(ENVIRONMENT)
                            .build();
                    uc.registerUser(this, this);
                    UserController uc1 = Sinch.getUserControllerBuilder()
                            .context(getApplicationContext())
                            .applicationKey(APP_KEY)
                            .userId(patient_phone)
                            .environmentHost(ENVIRONMENT)
                            .build();
                    uc1.registerUser(this, this);
                }
                   else {
                    startClientAndMakeCall();
                    makeCall();
                }
                break;
            case R.id.chat:
                Intent intent=new Intent(PendingMeetingsStep2.this, MessageActivity.class);
                intent.putExtra("d_phone",phone);
                intent.putExtra("p_phone",patient_phone);
                intent.putExtra("link",link);
                intent.putExtra("who","doctor");
                intent.putExtra("name",name);
                startActivity(intent);
                break;
            case R.id.voice_call:
                callType = "voice";
                VoiceCall();
                SharedPreferences pref=getSharedPreferences("Sinch",MODE_PRIVATE);
                if (!pref.getBoolean("isLogin",false)) {
                    if (!phone.equals(getSinchServiceInterface().getUsername())) {
                        getSinchServiceInterface().stopClient();
                    }
                    SharedPreferences.Editor ed=getSharedPreferences("Sinch",MODE_PRIVATE).edit();
                    ed.putBoolean("isLogin",true);
                    ed.apply();
                    startClientAndMakeCall();
                    getSinchServiceInterface().setUsername(phone);
                    UserController uc = Sinch.getUserControllerBuilder()
                            .context(getApplicationContext())
                            .applicationKey(APP_KEY)
                            .userId(phone)
                            .environmentHost(ENVIRONMENT)
                            .build();
                    uc.registerUser(this, this);
                }
                else
                {
                    startClientAndMakeCall();
                    makeCall();
                }
                break;
        }
        return true;

    }

    public void VoiceCall() {
        if (ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.READ_PHONE_STATE) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.ACCESS_NETWORK_STATE) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.INTERNET) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.BLUETOOTH) + ContextCompat.checkSelfPermission(PendingMeetingsStep2.this,
                Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPhonePermission();
        }

    }

    private void requestPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_NETWORK_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BLUETOOTH) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_WIFI_STATE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PendingMeetingsStep2.this,
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
                    permission, MULTIPLE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSinchServiceInterface().retryStartAfterPermissionGranted();

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

    private void makeCall() {
        String callId;
        if (callType.equals("voice")) {
            call=getSinchServiceInterface().callUser(patient_phone);
            callId=call.getCallId();
            Intent intent = new Intent(PendingMeetingsStep2.this, VoiceCallScreenActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("phone1", patient_phone);
            intent.putExtra("name", name);
            intent.putExtra("link", link);
            intent.putExtra(CALL_ID, callId);
            SetCallerName();
            startActivity(intent);
        } else {
            call=getSinchServiceInterface().callUserVideo(patient_phone);
            callId=call.getCallId();
            Intent intent = new Intent(PendingMeetingsStep2.this, VideoCallScreenActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("phone1", patient_phone);
            intent.putExtra("name", name);
            intent.putExtra("link", link);
            intent.putExtra(CALL_ID, callId);
            SetCallerName();
            startActivity(intent);
        }
    }
public void SetCallerName()
{
    CallerName callerName=new CallerName(
            name,
            link
    );
    Task<Void> databaseReference=FirebaseDatabase.getInstance().getReference("CallerName").child(call.getCallId())
            .setValue(callerName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
}
}

