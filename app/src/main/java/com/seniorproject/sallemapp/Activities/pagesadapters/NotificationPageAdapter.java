package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.NotificationFragment;


/**
 * Created by abdul on 18-Feb-2017.
 */

public class NotificationPageAdapter extends FragmentStatePagerAdapter {
    public NotificationPageAdapter(FragmentManager fm) {
        super(fm);
    }

    private static int NUM_ITEMS= 1;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){
//        switch (position){
//
//            case 0:
        return NotificationFragment.newInstance(0, "Notifications");
        // }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return "Notifications";
    }
}
