package com.example.nirvana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class my_meetings extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meetings);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tabLayout=(TabLayout) findViewById(R.id.tabs);
        viewpager=(ViewPager) findViewById(R.id.myViewPager);
        
        setSupportActionBar(toolbar);
        setViewpager(viewpager);
        
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void setViewpager(ViewPager viewpager){
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new UpcomingMeetings(),"UPCOMING");
        viewPagerAdapter.addFragment(new MeetingsHistory(),"HISTORY");
        viewpager.setAdapter(viewPagerAdapter);
    }
}