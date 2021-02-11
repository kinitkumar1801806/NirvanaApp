package com.example.nirvana;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
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

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        view= inflater.inflate(R.layout.fragment_about, container, false);
        content= "The only journey is the journey within, and what mental health needs is more sunlight, more openness and more unashamed conversation. We believe that nothing has a worth more than your happiness and mental contentment.\n" +
                "Does having mental illness mean being mad? The answer is ‘No.’ Rather mental illness could include your tiniest scar of depression, anxiety or addiction or to even severe OCDs or bipolar disorders.\n" +
                "\n"+
                "Unfortunately, in our country there’s almost total negligence towards our mental health. We ought to bring the difference.\n" +
                "\n"+
                "Here at Nirvana, we help you tackle all your emotional problems and help you lead a happy life.\n" +
                "\n"+
                "By making Nirvana simple and free, no longer is cost a barrier to your mental health. By anonymous entry, we help you reach out to us without the world knowing it and get relieved and hence attain nirvana.\n" +
                "\n"+
                "Nirvana is for all\n" +
                "\n"+
                "From individuals to companies we have designed it for all, the one stop solution to your problem, its fixation, management and e-medicine!\n" +
                "\n"+
                "Nirvana is free\n" +
                "\n"+
                "Just pay the counsellor charge and enjoy the rest services for free.\n" +
                "\n"+
                "NIRI is the new Doraemon\n" +
                "\n"+
                "NIRI has all in its bag, from reminders to tests, from booking appointments to payments, all through her own gateway. Simple and easy to use. Mental health can be addressed through both the ancient and modern methods. NIRI helps you there.\n" +
                "E-counsel is the new discussion forum\n" +
                "Easily talk and discuss your heart out to verified counsellors and get support.\n" +
                "Telemedicine should complement, not replace, traditional care delivery\n" +
                "\n"+
                "A strong doctor-patient relationship is the foundation for high-quality patient care and reducing health care costs. Nirvana provides a simple and convenient way for healthcare providers to meet with their patients remotely, improving the health care experience.\n" +
                "We as a team tried our best to bring a solution to the most neglected and prime problem. For suggestions please mail us at: nirvana@gmail.com\n" +
                "\n"+
                "Team Nirvana;\n";
        text=view.findViewById(R.id.about_text);
        text.setText(content);
        return view;
    }
}
