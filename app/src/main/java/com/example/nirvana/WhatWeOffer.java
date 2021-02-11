package com.example.nirvana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhatWeOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatWeOffer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView text;
    View view;
    String content;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WhatWeOffer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatWeOffer.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatWeOffer newInstance(String param1, String param2) {
        WhatWeOffer fragment = new WhatWeOffer();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_what_we_offer, container, false);
        content= "NIRI\n" +
                "NIRI is your personal Assistant cum friend at NIRVANA.From taking the test to analysing the results, from setting goals to making them happen, from booking appointments to attending them, from accessing various features to making payments, NIRI serves as a medium for all your work at Nirvana, making it easier..\n" +
                "\n"+
                "E-MANTRANA\n" +
                "\n"+
                "It is your place to find counsellors/ therapists, book appointments and meet them at your convenience at your preferred time, remotely and anonymously, if you want. This lets you escape from all your travelling/ leave tensions, making the meeting easier. .\n" +
                "\n"+
                "The room provides you the space to consult with the best counsellor by searching and comparing various profiles, book appointments based on your comfortable time, meeting room with chat/ voice/ video call, file transfer and record meeting facility.  All the meetings shall remain secured and shall not be revealed to any third party..\n" +
                "\n"+
                "CHETNA\n" +
                "\n"+
                "This helps you connect your roots by bringing you back to the spirits of Yoga and Meditation by its beautiful video, speech, text tutorials synced with your Saarini and thus helping you attain mindfulness and a happy body.\n" +
                "\n"+
                "SAARINI\n" +
                "\n"+
                "Saarini helps you track your goals and involve in good practice for a healthy lifestyle. Sync it with your calendar and stay focussed on your goal.\n" +
                "\n"+
                "SOUL\n" +
                "\n"+
                "We get too tired and irritated from the noises around our day to day chores. It is often desirable to take an escape from all of it to the sounds of nature, to relax. Soul presents you relaxing music on Natural and Spiritual themes and podcasts for motivation..\n" +
                "MEDICPEN\n" +
                "It’s good to always stay updated on health and precautions for prevention from various diseases. It’s also important to have a place dedicated to find about various kinds of mental disorders and treatment procedures. Medicpen comes up with all of these blogs for the readers and various health alerts..\n" +
                "Telemedicine should complement, not replace, traditional care delivery\n" +
                "\n"+
                "DHARMA\n" +
                "\n"+
                "Humans have been constantly evolving and the quest to the purpose of life still remains unknown. With this evolution we also have inclined towards forgetfulness of the spiritual powers and the essence of life. Dharma is a small step by Nirvana to cater to the spirits of life.\n" +
                "\n"+
                "NIRVAAN\n" +
                "\n"+
                "Nirvaan is an ongoing effort to help connect small local mental health based businesses or products with our users inclined towards Vocal for Local through Nirvana.\n" +
                "\n"+
                "NIRI TEST\n" +
                "\n"+
                "The NIRI Test is a short structured diagnostic test for the determination of depression level and identification of the presence of mental disorders, if any. \n" +
                "\n"+
                "The Stress/NIRI Test:\n" +
                "\n"+
                "1) Quantifies depression/ stress/ anxiety\n" +
                "\n"+
                "2) Suggests ways to combat\n" +
                "\n"+
                "3) Screens for mental disorders\n" +
                "\n"+
                "Ideally, the test shall be attempted twice with a gap of two weeks. The test taker can remain anonymous while appearing and follow the results of the NIRI Test to lead a path for stronger mental health.\n" +
                "\n"+
                "The NIRI Test is;\n" +
                "\n"+
                "1) Short and Secure\n" +
                "\n"+
                "2) Simple and Secure\n" +
                "\n"+
                "3) Highly Sensitive\n" +
                "4) Highly Specific\n" +
                "\n"+
                "5) Compatible with all kinds of disorders\n" +
                "\n";
        text=view.findViewById(R.id.whatweoffer_text);
        text.setText(content);
        return view;
    }
}
