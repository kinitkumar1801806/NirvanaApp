package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.nirvana.NirvanaAudioPlayer.Broadcast_PLAY_NEW_AUDIO;

public class detail_music_player extends AppCompatActivity {
    TextView marquee_music, detail_music_artist;
    ImageView detail_play;
    int index;
    SeekBar seekBar;
    boolean serviceBound = false;
    ArrayList<Audio> audioList;
    MediaPLayerService player;
    DatabaseReference databaseReference;
    MediaPlayer mediaPlayer;
    public ArrayList<String> dataList,titleList,albumList,artistList;
    NirvanaAudioPlayer nirvanaAudioPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music_player);
        marquee_music = findViewById(R.id.detail_music);
        marquee_music.setSelected(true);
        seekBar = findViewById(R.id.seekbar);
        dataList=new ArrayList<>();
        titleList=new ArrayList<>();
        albumList=new ArrayList<>();
        artistList=new ArrayList<>();
        audioList=new ArrayList<>();
        detail_play = findViewById(R.id.detail_play);
        detail_music_artist = findViewById(R.id.detail_artist);
        Intent intent = getIntent();
        dataList=intent.getStringArrayListExtra("dataList");
        titleList=intent.getStringArrayListExtra("titleList");
        albumList=intent.getStringArrayListExtra("albumList");
        artistList=intent.getStringArrayListExtra("artistList");
        loadAudioPlayer();
        index = intent.getIntExtra("index", -1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int x = (int) Math.ceil(progress / 1000f);
                if(x==0&&player!=null&&!player.check())
                {
                    player.clearMediaPlayer();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
        if(index!=-1)
        {
            marquee_music.setText(audioList.get(index).getTitle());
            detail_music_artist.setText(audioList.get(index).getArtist());
            detail_play.setImageResource(R.drawable.filled_pause);
            playAudio(index);

        }
        else
        {
            index = intent.getIntExtra("index1", 0);
            marquee_music.setText(audioList.get(index).getTitle());
            detail_music_artist.setText(audioList.get(index).getArtist());
            detail_play.setImageResource(R.drawable.filled_play);
        }


    }

    //Binding this Client to the AudioPlayer Service
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPLayerService.LocalBinder binder = (MediaPLayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    public void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPLayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }


    private void loadAudioPlayer() {
        int l=dataList.size();
        for(int i=0;i<l;i++)
        {
           audioList.add(new Audio(dataList.get(i),titleList.get(i),albumList.get(i),artistList.get(i)));
        }
    }

    public void play_music(View view) {

        if (detail_play.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.filled_play).getConstantState()) {
            detail_play.setImageResource(R.drawable.filled_pause);
            try {
                player.resumeMedia();
            }
            catch(Exception e)
            {
                player.pauseMedia();
            }

        } else {
            detail_play.setImageResource(R.drawable.filled_play);
            player.pauseMedia();
        }
    }

    public void play_previous(View view) {
        if (index != 0) {
            player.skipToPrevious();
            index--;
            marquee_music.setText(audioList.get(index).getTitle());
            detail_music_artist.setText(audioList.get(index).getArtist());
            playAudio(index);

        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    public void play_next(View view) {
        if (audioList.size() != index+1) {
            player.skipToNext();
            index++;
            marquee_music.setText(audioList.get(index).getTitle());
            detail_music_artist.setText(audioList.get(index).getArtist());
            playAudio(index);
            seekBar.setProgress(0);
        } else {
            player.skipToNext();
            index=0;
            marquee_music.setText(audioList.get(index).getTitle());
            detail_music_artist.setText(audioList.get(index).getArtist());
            playAudio(index);
            seekBar.setProgress(0);
        }
    }
    @Override
    public void onBackPressed() {
        if (detail_play.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.filled_pause).getConstantState()) {
            Intent intent1 = new Intent();
            intent1.putExtra("index", index);
            setResult(Activity.RESULT_OK, intent1);
            finish();
        } else {
            Intent intent1 = new Intent();
            intent1.putExtra("index1", index);
            setResult(Activity.RESULT_OK, intent1);
            finish();
        }

    }
}
