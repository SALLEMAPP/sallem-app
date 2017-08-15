package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.PostsFragment;

/**
 * Created by abdul on 18-Feb-2017.
 */

public  class HomePageAdater extends FragmentStatePagerAdapter {

    private String friend_menu = "Friends الأصدقاء";
    private String posts_menu = "Post نشر";

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
                return PostsFragment.newInstance(0, friend_menu);
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return posts_menu;
    }
    

}
