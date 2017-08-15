package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.NotificationFragment;


/**
 * Created by abdul on 18-Feb-2017.
 */

public class NotificationPageAdapter extends FragmentStatePagerAdapter {

    private String notifications_menu = "Notifications إشعارات";

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
        return NotificationFragment.newInstance(0, notifications_menu);
        // }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return notifications_menu;
    }
}
