package com.example.nirvana.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class viewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentslist= new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    private final Bundle bundle;
    public viewPagerAdapter(FragmentManager fm,Bundle bn) {
        super(fm);
        bundle=bn;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        fragmentslist.get(position).setArguments(this.bundle);
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
