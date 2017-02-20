package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.seniorproject.sallemapp.Activities.PostsFragment;

/**
 * Created by abdul on 18-Feb-2017.
 */

public  class HomePageAdater extends FragmentStatePagerAdapter {
    public HomePageAdater(FragmentManager fm) {
        super(fm);
    }
    private static int NUM_ITEMS= 1;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return PostsFragment.newInstance(0, "Friends");
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return "Posts";
    }
    

}
