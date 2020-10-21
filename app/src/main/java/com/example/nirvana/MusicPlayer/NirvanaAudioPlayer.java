package com.example.nirvana.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirvana.Adapter.RecyclerView_Adapter;
import com.example.nirvana.CustomTouchListener;
import com.example.nirvana.R;
import com.example.nirvana.onItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import android.Manifest;
import static com.example.nirvana.R.drawable.ic_media_pause;

public class NirvanaAudioPlayer extends AppCompatActivity {
    MediaPLayerService player;
    RelativeLayout relativeLayout;
    boolean serviceBound = false;
    ImageView collapsingImageView;
    ImageView music_action_image;
    TextView marquee_music, detail_music_artist,currentDuration;
    ImageView detail_play;
    SeekBar seekBar;
    TextView music_name, artist_name;
    int imageIndex = 0;
    boolean check = true;
    int id = 0,count=0,track=0,tt=0;
    int music_index = 0;
    public ProgressBar progressBar;
    ArrayList<String> dataList,titleList,albumList,artistList;
    private DatabaseReference databaseReference;
    public ArrayList<Audio> audioList;
    private int STORAGE_PERMISSION_CODE = 1;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.nirvana.MusicPlayer.NirvanaAudioPlayer.PlayNewAudio";
    public String phone;
    // Change to your package name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nirvana_audio_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle=new Bundle();
        bundle.putInt("track",track);
        HomeMusicPlayer homeMusicPlayer=new HomeMusicPlayer();
        homeMusicPlayer.setArguments(bundle);
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.music_frame,homeMusicPlayer,"homeplayer");
        fragmentTransaction.commit();
        dataList=new ArrayList<>();
        titleList=new ArrayList<>();
        artistList=new ArrayList<>();
        albumList=new ArrayList<>();
        phone=getIntent().getStringExtra("phone");
        System.out.println(Integer.valueOf(Build.VERSION.SDK_INT));
        if (ContextCompat.checkSelfPermission(NirvanaAudioPlayer.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(NirvanaAudioPlayer.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }
        LoadIndex();
        loadAudio();
  }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed ")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NirvanaAudioPlayer.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initRecyclerView() {
        try {
            if (audioList.size() > 0) {
                ProgressBar progressBar;
                music_name = findViewById(R.id.music_name);
                artist_name = findViewById(R.id.music_artist);
                music_action_image = findViewById(R.id.music_action_btn);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar=findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                RecyclerView_Adapter adapter = new RecyclerView_Adapter(audioList, getApplication());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
                    @Override
                    public void onClick(View view, int index) {
                        playAudio(index);
                        music_index = index;
                        music_name.setText(audioList.get(index).getTitle());
                        artist_name.setText(audioList.get(index).getArtist());
                        music_action_image.setImageResource(ic_media_pause);
                        id=1;
                    }
                }));
                if(music_index<audioList.size())
                {
                    music_name.setText(audioList.get(music_index).getTitle());
                    artist_name.setText(audioList.get(music_index).getArtist());
                }
            }
        } catch (NullPointerException e) {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
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


    public void loadAudio() {
        audioList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Musics").child("musics");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                    for(String key:hashMap.keySet())
                    {
                        Object data1=hashMap.get(key);
                        HashMap<String,Object> userdata= (HashMap<String, Object>) data1;
                        String title=(String)userdata.get("title");
                        String album=(String)userdata.get("album");
                        String artist=(String)userdata.get("artist");
                        String data=(String)userdata.get("url");
                        audioList.add(new Audio(data, title, album, artist));
                        count++;
                        initRecyclerView();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    public void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPLayerService.class);
            playerIntent.putExtra("music_index",music_index);
            playerIntent.putExtra("phone",phone);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("music_index",music_index);
            broadcastIntent.putExtra("phone",phone);
            sendBroadcast(broadcastIntent);

        }

    }

    public void change_icon(View view) {
        if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_pause).getConstantState()) {
            music_action_image.setImageResource(R.drawable.ic_media_play);
            player.pauseMedia();
            tt=0;
            id = 1;
        } else {
            music_action_image.setImageResource(R.drawable.ic_media_pause);
            tt=1;
            if (id == 0) {
                playAudio(music_index);
                id=1;
            } else if (id == 1) {
                player.resumeMedia();
            }
        }
    }

    public void show_detail_musicplayer(View view) {
        if(player.check()&&player!=null)
        {
            retrieve();
            if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_pause).getConstantState()) {
                Bundle bundle=new Bundle();
                bundle.putString("playing","yes");
                bundle.putInt("index",music_index);
                bundle.putStringArrayList("dataList",dataList);
                bundle.putStringArrayList("titleList",titleList);
                bundle.putStringArrayList("albumList",albumList);
                bundle.putStringArrayList("artistList",artistList);
                bundle.putString("phone",phone);
                DetailMusicFragment detailMusicFragment=new DetailMusicFragment();
                detailMusicFragment.setArguments(bundle);
                track=1;
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.music_frame,detailMusicFragment,"detailplayer");
                fragmentTransaction.commit();
                detailMusicFragment.change1(player);
            } else if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_play).getConstantState()) {
                Bundle bundle=new Bundle();
                bundle.putString("playing","no");
                bundle.putInt("index",music_index);
                bundle.putStringArrayList("dataList",dataList);
                bundle.putStringArrayList("titleList",titleList);
                bundle.putStringArrayList("albumList",albumList);
                bundle.putStringArrayList("artistList",artistList);
                bundle.putString("phone",phone);
                DetailMusicFragment detailMusicFragment=new DetailMusicFragment();
                detailMusicFragment.setArguments(bundle);
                track=1;
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.music_frame,detailMusicFragment,"detailplayer");
                fragmentTransaction.commit();
                detailMusicFragment.change1(player);

            }

        }
    }


    public void play_previous(View view) {
        marquee_music = findViewById(R.id.detail_music);
        marquee_music.setSelected(true);
        detail_music_artist = findViewById(R.id.detail_artist);
        if (music_index != 0) {
            player.skipToPrevious();
            music_index--;

            marquee_music.setText(audioList.get(music_index).getTitle());
            detail_music_artist.setText(audioList.get(music_index).getArtist());
            playAudio(music_index);

        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void play_next(View view) {
        marquee_music = findViewById(R.id.detail_music);
        marquee_music.setSelected(true);
        detail_music_artist = findViewById(R.id.detail_artist);
        seekBar=findViewById(R.id.seekbar);
        if (audioList.size() != music_index+1) {
            player.skipToNext();
            music_index++;
            marquee_music.setText(audioList.get(music_index).getTitle());
            detail_music_artist.setText(audioList.get(music_index).getArtist());
            playAudio(music_index);
            seekBar.setProgress(0);
        } else {
            player.skipToNext();
            music_index=0;
            marquee_music.setText(audioList.get(music_index).getTitle());
            detail_music_artist.setText(audioList.get(music_index).getArtist());
            playAudio(music_index);
            seekBar.setProgress(0);
        }
    }
    public void play_music(View view) {
        detail_play=findViewById(R.id.detail_play);
        if (detail_play.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.filled_play).getConstantState()) {
            detail_play.setImageResource(R.drawable.filled_pause);
            if(id==1)
                player.resumeMedia();
            else
            {
                playAudio(music_index);
                id=1;
            }
        } else {
            detail_play.setImageResource(R.drawable.filled_play);
            player.pauseMedia();
        }
    }
    public void change_theme(View view) {
        relativeLayout=findViewById(R.id.relativeLayout);
        if(relativeLayout.getBackground().getConstantState()==getResources().getDrawable(R.drawable.photo).getConstantState())
        {
            relativeLayout.setBackgroundResource(R.drawable.ocean);
        }
        else if(relativeLayout.getBackground().getConstantState()==getResources().getDrawable(R.drawable.ocean).getConstantState())
        {
            relativeLayout.setBackgroundResource(R.drawable.sunrise);
        }
        else if(relativeLayout.getBackground().getConstantState()==getResources().getDrawable(R.drawable.sunrise).getConstantState())
        {
            relativeLayout.setBackgroundResource(R.drawable.photo);
        }
    }
    public void retrieve()
    {
        int l=audioList.size();
        for (int i=0;i<l;i++)
        {
            dataList.add(audioList.get(i).getData());
            artistList.add(audioList.get(i).getArtist());
            albumList.add(audioList.get(i).getAlbum());
            titleList.add(audioList.get(i).getTitle());

        }
    }

    public void change_image(View view) {
        collapsingImageView = (ImageView) findViewById(R.id.collapsingImageView);
        if (imageIndex == 4) {
            imageIndex = 0;
            loadCollapsingImage(imageIndex);
        } else {
            loadCollapsingImage(++imageIndex);
        }
    }
    public void LoadIndex()
    {
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Music_Index").child(phone);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    HashMap<String,Object> hashMap= (HashMap<String, Object>) snapshot.getValue();
                    music_index=Integer.parseInt((String) hashMap.get("music_index"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}