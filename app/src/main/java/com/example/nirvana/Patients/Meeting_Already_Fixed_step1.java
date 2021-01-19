package com.example.nirvana.Patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toolbar;

import com.example.nirvana.Adapter.viewPagerAdapter;
import com.example.nirvana.R;
import com.google.android.material.tabs.TabLayout;

public class Meeting_Already_Fixed_step1 extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__already__fixed_step1);
        tabLayout=(TabLayout) findViewById(R.id.tabs);
        viewpager=(ViewPager) findViewById(R.id.myViewPager);
        setViewpager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setViewpager(ViewPager viewpager){
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new UpcomingMeetings(),"UPCOMING");
        viewPagerAdapter.addFragment(new MeetingsHistory(),"HISTORY");
        viewpager.setAdapter(viewPagerAdapter);
    }
}
