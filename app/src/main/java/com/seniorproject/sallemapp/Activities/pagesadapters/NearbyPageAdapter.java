package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.NearByFragment;
import com.seniorproject.sallemapp.R;

/**
 * Created by abdul on 18-Feb-2017.
 */

public class NearbyPageAdapter extends FragmentStatePagerAdapter {

    private String nearby_menu = String.valueOf(R.string.nearFriends_label);


    public NearbyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private static int NUM_ITEMS =1;
    @Override
    public Fragment getItem(int position) {
        return NearByFragment.newInstance(1,nearby_menu);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nearby_menu;
    }
}
