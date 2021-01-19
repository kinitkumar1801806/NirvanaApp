package com.example.nirvana.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static okhttp3.internal.tls.CertificateChainCleaner.get;

public class viewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentslist= new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();


    public viewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentslist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentslist.size();
    }

    public void addFragment (Fragment fragment, String title){
        fragmentslist.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
