package com.example.nirvana.MusicPlayer;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nirvana.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeMusicPlayer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMusicPlayer extends Fragment {
    View view1;
    ImageView collapsingImageView;
    int imageIndex=0;
    int track=0;
    ImageView music_action_image;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeMusicPlayer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMusicPlayer.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMusicPlayer newInstance(String param1, String param2) {
        HomeMusicPlayer fragment = new HomeMusicPlayer();
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
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            track=bundle.getInt("track");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1= inflater.inflate(R.layout.fragment_home_music_player, container, false);
        collapsingImageView = view1.findViewById(R.id.collapsingImageView);
        music_action_image = view1.findViewById(R.id.music_action_btn);
        if(track==1)
           music_action_image.setImageResource(R.drawable.ic_media_pause);
        loadCollapsingImage(imageIndex);
        return view1;
    }
    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
    }

}