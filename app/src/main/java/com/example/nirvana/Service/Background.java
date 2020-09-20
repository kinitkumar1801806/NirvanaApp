package com.example.nirvana.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sinch.android.rtc.SinchClient;

public class Background extends Service {
    String phone;
    private SinchClient mSinchClient;
    public Background() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        phone=intent.getStringExtra("phone");
        Toast.makeText(getApplicationContext(),phone,Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        restartServiceIntent.putExtra("phone",phone);
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
}
