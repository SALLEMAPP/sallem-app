package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.PostsFragment;
import com.seniorproject.sallemapp.R;

/**
 * Created by abdul on 18-Feb-2017.
 */

public  class HomePageAdater extends FragmentStatePagerAdapter {

    private String friend_menu = String.valueOf(R.string.friends_menu_label);
    private String posts_menu = String.valueOf(R.string.addPost_btn);

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
