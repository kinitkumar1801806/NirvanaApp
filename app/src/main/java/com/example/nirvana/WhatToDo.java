package com.example.nirvana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhatToDo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatToDo extends Fragment {
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

    public WhatToDo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatToDo.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatToDo newInstance(String param1, String param2) {
        WhatToDo fragment = new WhatToDo();
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
        view= inflater.inflate(R.layout.fragment_what_to_do, container, false);
        content= "Nirvana – The simple, easy and secure mental health solution.\n" +
                "Mental health and well-being addressed across; To help you live with the world, there for you forever..\n" +
                "\n"+
                "Nirvana is a team of enthusiasts exploring the untouched spheres of the mental health. With a mission to quantify the burden of those suffering from depression, mental, select neurological or substance problems by creating awareness and developing mental health system we carry our work ahead by working on our idea to provide a truly personalised user experience to combat mental health issues by bridging the gap and hence calm the oscillation of mind towards stability..\n" +
                "\n"+
                "We aim to reach out to everyone to help them relax by the practice of the NIRI Test and NIRI tips based on extensive research and expertise suggestions. Nirvana helps making identification and treatment easy and secure by its encrypted E- counsel and wellness features for all groups of people..\n" +
                "\n"+
                "Nirvana is your ‘Mental Wellness Spa’. .\n" +
                "\n"+
                "It is simple and easy to use all-in-one development package to bounce you back from your disappointment and psychotic pains and at the same time help you discover ways to tap into your own resilience. Nirvana works in very unique and systematic way encompassing attributes like Yoga, Meditation, Therapy, Health Quantifier, Health blogs, Relaxing music and podcasts, Goal tracker and above all, the intelligent AI based chat bot NIRI.\n" +
                "\n"+
                "Nirvana helps you attain mindfulness in every possible way as you unzip;\n" +
                "\n"+
                "1) NIRI : Your friend at Nirvana\n" +
                "\n"+
                "2) E-Mantrana : Your room to therapy\n" +
                "\n"+
                "3) Chetna : Your guide to Yog and meditation\n" +
                "\n"+
                "4) Medicpen : Your place to read\n" +
                "\n"+
                "5) Soul : Your escape into chords\n" +
                "\n"+
                "6) Saarini : Your chores synchronised\n" +
                "\n"+
                "7) Dharma : Your purpose to life\n" +
                "\n"+
                "8) Nirvaan : Your store to happiness\n" +
                "\n"+
                "9) NIRI Test : Your step towards detection\n" +
                "\n"+
                "Why NIRVANA?\n" +
                "\n"+
                "With changing health patterns among Indians, mental, behavioural and substance use disorders are coming to the fore in health care delivery systems. These disorders contribute for significant morbidity, disability and even mortality amongst those affected. Due to the prevailing stigma, these disorders often are hidden by the society and consequently persons with mental disorders lead a poor quality of life. \n" +
                "\n"+

                "Nirvana is one such step towards catering the needs of those people, recognising the need for good quality, scientific and reliable information to strengthen mental health.\n";
        text=view.findViewById(R.id.whatwedo_text);
        text.setText(content);
        return view;



    }
}
