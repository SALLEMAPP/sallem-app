package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.FriendRequestFragment;
import com.seniorproject.sallemapp.Activities.FriendsFragment;
import com.seniorproject.sallemapp.Activities.NearByFragment;
import com.seniorproject.sallemapp.Activities.OrganizeActivityFragment;
import com.seniorproject.sallemapp.Activities.PastActivitiesFragment;
import com.seniorproject.sallemapp.Activities.SearchFriendsFragment;
import com.seniorproject.sallemapp.Activities.UpcomingActivitiesFragment;

/**
 * Created by abdul on 17-Feb-2017.
 */

public  class FriendsPageAdapter extends FragmentStatePagerAdapter {
    //FragmentManager _fm;
    public FriendsPageAdapter(FragmentManager fm) {
        super(fm);
      //  this._fm = fm;
    }

    private Fragment _currentFragment = null;
    private static int NUM_ITEMS =4;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){


        switch (position){
            case 0:

                _currentFragment = FriendsFragment.newInstance(0, "Friends");
                break;
            case 1:
                _currentFragment= SearchFriendsFragment.newInstance(1, "Search");
                break;
            case 2:
                _currentFragment=  FriendRequestFragment.newInstance(2, "Requests");
                break;
            case 3:
                _currentFragment = NearByFragment.newInstance(3, "NearBy");
                break;
            default:
                _currentFragment = null ;
                break;
        }
        return _currentFragment;
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
