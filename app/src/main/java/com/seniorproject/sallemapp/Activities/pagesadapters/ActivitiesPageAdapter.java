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
 * Created by abdul on 18-Feb-2017.
 */

public class ActivitiesPageAdapter extends FragmentStatePagerAdapter {
    //FragmentManager _fm;
    public ActivitiesPageAdapter(FragmentManager fm) {
        super(fm);
        //  this._fm = fm;
    }


    private Fragment _currentFragment = null;
    private static int NUM_ITEMS =3;
    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
    @Override
    public Fragment getItem(int position){

        switch (position){
            case 0:

                _currentFragment = UpcomingActivitiesFragment.newInstance(0,"Upcoming");
                break;
            case 1:
                _currentFragment = OrganizeActivityFragment.newInstance(1,"Organize");
                break;
            case 2:
                _currentFragment = PastActivitiesFragment.newInstance(2,"Past");
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
                title = "Upcoming";
                break;
            case 1:
                title = "Organize";
                break;
            case 2:
                title = "Past";
                break;

            default:
                title = "Page " + position;
                break;
        }
        return title;

    }
}
