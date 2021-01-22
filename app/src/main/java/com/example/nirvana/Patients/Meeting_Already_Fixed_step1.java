package com.example.nirvana.Patients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toolbar;

import com.example.nirvana.Adapter.viewPagerAdapter;
import com.example.nirvana.R;
import com.google.android.material.tabs.TabLayout;

public class Meeting_Already_Fixed_step1 extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting__already__fixed_step1);
        Intent intent=getIntent();
        Id=intent.getStringExtra("Id");
        tabLayout=(TabLayout) findViewById(R.id.tabs);
        viewpager=(ViewPager) findViewById(R.id.myViewPager);
        setViewpager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setViewpager(ViewPager viewpager){
        Bundle bundle=new Bundle();
        bundle.putString("Id",Id);
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(),bundle);
        viewPagerAdapter.addFragment(new UpcomingMeetings(),"UPCOMING");
        viewPagerAdapter.addFragment(new MeetingsHistory(),"HISTORY");
        viewpager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}
