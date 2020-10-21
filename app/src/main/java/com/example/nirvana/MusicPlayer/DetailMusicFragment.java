package com.example.nirvana.MusicPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nirvana.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailMusicFragment extends Fragment{
    View view1;
    SeekBar seekBar;
    TextView marquee_music, detail_music_artist,Duration,currentDuration;
    ImageView detail_play;
    String action,phone;
    boolean ch;
    ArrayList<String> dataList,titleList,albumList,artistList;
    ArrayList<Audio> audioList;
    int music_index;
    MediaPLayerService player;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailMusicFragment newInstance(String param1, String param2) {
        DetailMusicFragment fragment = new DetailMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dataList=new ArrayList<>();
        titleList=new ArrayList<>();
        artistList=new ArrayList<>();
        albumList=new ArrayList<>();
        audioList=new ArrayList<>();
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
           action=bundle.getString("playing");
           music_index=bundle.getInt("index");
           dataList=bundle.getStringArrayList("dataList");
           titleList=bundle.getStringArrayList("titleList");
           albumList=bundle.getStringArrayList("albumList");
           artistList=bundle.getStringArrayList("artistList");
           phone=bundle.getString("phone");
            loadAudioPlayer();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1= inflater.inflate(R.layout.fragment_detail_music, container, false);
        marquee_music = view1.findViewById(R.id.detail_music);
        marquee_music.setSelected(true);
        seekBar = view1.findViewById(R.id.seekbar);
        currentDuration=view1.findViewById(R.id.current_position);
        detail_play = view1.findViewById(R.id.detail_play);
        Duration=view1.findViewById(R.id.duration);
        detail_music_artist = view1.findViewById(R.id.detail_artist);
        marquee_music.setText(audioList.get(music_index).getTitle());
        detail_music_artist.setText(audioList.get(music_index).getArtist());

        if(action.equals("yes"))
        {
            detail_play.setImageResource(R.drawable.filled_pause);
        }
        else{
            detail_play.setImageResource(R.drawable.filled_play);
        }

        change();
        return view1;
    }
    private void loadAudioPlayer() {
        int l=dataList.size();
        for(int i=0;i<l;i++)
        {
            audioList.add(new Audio(dataList.get(i),titleList.get(i),albumList.get(i),artistList.get(i)));
        }
    }
public void change()
{
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(player!= null && fromUser){
                player.Seekto(progress);
            }
        }
    });

    Handler mHandler = new Handler();
    getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if(player != null){
                seekBar.setMax((int) player.duration()/1000);
                long duration=player.duration();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                duration-=minutes*60*1000;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                String sec;
                if(seconds<10)
                    sec="0"+String.valueOf(seconds);
                else
                    sec=String.valueOf(seconds);
                String dur="0"+minutes+":"+sec;
                Duration.setText(dur);
                int mCurrentPosition = (int) (player.currentPosition()/ 1000);
                seekBar.setProgress(mCurrentPosition);
                long curr_duration=player.currentPosition();
                long curr_minutes = TimeUnit.MILLISECONDS.toMinutes(curr_duration);
                curr_duration-=curr_minutes*60*1000;
                long curr_seconds = TimeUnit.MILLISECONDS.toSeconds(curr_duration);
                String curr_sec;
                if(curr_seconds<10)
                    curr_sec="0"+String.valueOf(curr_seconds);
                else
                    curr_sec=String.valueOf(curr_seconds);
                String curr_dur="0"+curr_minutes+":"+curr_sec;
                currentDuration.setText(curr_dur);
                marquee_music.setText(audioList.get(music_index).getTitle());
                detail_music_artist.setText(audioList.get(music_index).getArtist());
            }
            mHandler.postDelayed(this, 1000);
        }
    });
}
 public void change1(MediaPLayerService player1)
 {
     player=player1;
 }
    public void SetIndex(int index)
    {
        music_index=index;

    }
}