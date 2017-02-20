package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.NearByFragment;

/**
 * Created by abdul on 18-Feb-2017.
 */

public class NearbyPageAdapter extends FragmentStatePagerAdapter {
    public NearbyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private static int NUM_ITEMS =1;
    @Override
    public Fragment getItem(int position) {
        return NearByFragment.newInstance(1,"Nearby");
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Nearby";
    }
}
