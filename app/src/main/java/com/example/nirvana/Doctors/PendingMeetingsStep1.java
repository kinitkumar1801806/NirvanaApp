package com.example.nirvana.Doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.nirvana.Adapter.viewPagerAdapter;
import com.example.nirvana.Patients.MeetingsHistory;
import com.example.nirvana.Patients.UpcomingMeetings;
import com.example.nirvana.R;
import com.google.android.material.tabs.TabLayout;

public class PendingMeetingsStep1 extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_meetings_step1);
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
        viewPagerAdapter.addFragment(new Doctor_UpcomingMeetings(),"UPCOMING");
        viewPagerAdapter.addFragment(new Doctor_HistoryMeetings(),"HISTORY");
        viewpager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }
}
