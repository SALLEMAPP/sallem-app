package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.seniorproject.sallemapp.Activities.FriendRequestFragment;
import com.seniorproject.sallemapp.Activities.FriendsFragment;
import com.seniorproject.sallemapp.Activities.NearByFragment;
import com.seniorproject.sallemapp.Activities.SearchFriendsFragment;

/**
 * Created by abdul on 17-Feb-2017.
 */

public  abstract class FriendsPageAdapter extends FragmentPagerAdapter {
    public FriendsPageAdapter(FragmentManager fm) {
        super(fm);
    }
    private static int NUM_ITEMS =4;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return FriendsFragment.newInstance(0, "Friends");

            case 1:
                return SearchFriendsFragment.newInstance(1, "Search");
            case 2:
                return FriendRequestFragment.newInstance(2, "Requests");
            case 3:
                return NearByFragment.newInstance(3, "NearBy");
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position){
            case   0:
                title = "Friends";
                break;
            case 1:
                title = "Search";
                break;
            case 2:
                title = "Requests";
                break;
            case 3:
                title = "NearBy";
                break;
            default:
                    title = "Page " + position;
                    break;
        }
        return title;

    }
}
