package com.example.nirvana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailMusicFragment extends Fragment{
    View view1;
    SeekBar seekBar;
    TextView marquee_music, detail_music_artist;
    ImageView detail_play;
    String action;
    ArrayList<String> dataList,titleList,albumList,artistList;
    ArrayList<Audio> audioList;
    int music_index;
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
        detail_play = view1.findViewById(R.id.detail_play);
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
        return view1;
    }
    private void loadAudioPlayer() {
        int l=dataList.size();
        for(int i=0;i<l;i++)
        {
            audioList.add(new Audio(dataList.get(i),titleList.get(i),albumList.get(i),artistList.get(i)));
        }
    }

}