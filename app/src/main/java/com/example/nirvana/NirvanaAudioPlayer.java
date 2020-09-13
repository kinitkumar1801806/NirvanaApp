package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import android.Manifest;

import static android.provider.LiveFolders.INTENT;
import static com.example.nirvana.R.drawable.ic_media_pause;

public class NirvanaAudioPlayer extends AppCompatActivity {
    MediaPLayerService player;
    boolean serviceBound = false;
    ImageView collapsingImageView;
    ImageView music_action_image;
    TextView music_name, artist_name;
    int imageIndex = 0;
    boolean check = true;
    int id = 0,count=0;
    int music_index = 0;
    private DatabaseReference databaseReference;
    public ArrayList<Audio> audioList;
    public ArrayList<String> dataList,titleList,albumList,artistList;
    private int STORAGE_PERMISSION_CODE = 1;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.nirvana.NirvanaAudioPlayer.PlayNewAudio";

    // Change to your package name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nirvana_audio_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingImageView = (ImageView) findViewById(R.id.collapsingImageView);
        if (ContextCompat.checkSelfPermission(NirvanaAudioPlayer.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(NirvanaAudioPlayer.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }
        dataList=new ArrayList<>();
        titleList=new ArrayList<>();
        albumList=new ArrayList<>();
        artistList=new ArrayList<>();
        loadCollapsingImage(imageIndex);
        loadAudio();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
                //play the first audio in the ArrayList
//                playAudio(2);
                if (imageIndex == 4) {
                    imageIndex = 0;
                    loadCollapsingImage(imageIndex);
                } else {
                    loadCollapsingImage(++imageIndex);
                }
            }
        });
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
                music_name = findViewById(R.id.music_name);
                artist_name = findViewById(R.id.music_artist);
                music_action_image = findViewById(R.id.music_action_btn);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
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

                    }
                }));

                music_name.setText(audioList.get(0).getTitle());
                artist_name.setText(audioList.get(0).getArtist());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }

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

    public void change_icon(View view) {
        if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_pause).getConstantState()) {
            music_action_image.setImageResource(R.drawable.ic_media_play);
            player.pauseMedia();
            id = 1;
        } else {
            music_action_image.setImageResource(R.drawable.ic_media_pause);
            if (id == 0) {
                playAudio(0);
            } else if (id == 1) {
                player.resumeMedia();
            }
        }
    }

    public void show_detail_musicplayer(View view) {
        retrieve();
        if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_pause).getConstantState()) {
            Intent intent = new Intent(this, detail_music_player.class);
            intent.putExtra("index", music_index);
            intent.putStringArrayListExtra("dataList",dataList);
            intent.putStringArrayListExtra("titleList",titleList);
            intent.putStringArrayListExtra("albumList",albumList);
            intent.putStringArrayListExtra("artistList",artistList);
            startActivityForResult(intent, 1);
        } else if (music_action_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_media_play).getConstantState()) {
            Intent intent = new Intent(this, detail_music_player.class);
            intent.putExtra("index1", music_index);
            intent.putStringArrayListExtra("dataList",dataList);
            intent.putStringArrayListExtra("titleList",titleList);
            intent.putStringArrayListExtra("albumList",albumList);
            intent.putStringArrayListExtra("artistList",artistList);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int ind = data.getIntExtra("index", -1);
                if (ind != -1) {
                    music_index = ind;
                    id=1;
                    music_name.setText(audioList.get(music_index).getTitle());
                    artist_name.setText(audioList.get(music_index).getArtist());
                    music_action_image.setImageResource(R.drawable.ic_media_pause);
                    player.resumeMedia();
                } else {
                    ind = data.getIntExtra("index1", 0);
                    music_index = ind;
                    id=1;
                    music_name.setText(audioList.get(music_index).getTitle());
                    artist_name.setText(audioList.get(music_index).getArtist());
                    music_action_image.setImageResource(R.drawable.ic_media_play);
                    player.stopMedia();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }
 public void retrieve()
 {
     int l=audioList.size();
     for(int i=0;i<l;i++)
     {
         dataList.add(audioList.get(i).getData());
         titleList.add(audioList.get(i).getTitle());
         albumList.add(audioList.get(i).getAlbum());
         artistList.add(audioList.get(i).getArtist());
     }
 }
}