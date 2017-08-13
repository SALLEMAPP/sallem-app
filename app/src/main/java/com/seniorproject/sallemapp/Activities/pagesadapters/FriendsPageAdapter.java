package com.seniorproject.sallemapp.Activities.pagesadapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.seniorproject.sallemapp.Activities.FriendRequestFragment;
import com.seniorproject.sallemapp.Activities.FriendsFragment;
import com.seniorproject.sallemapp.Activities.NearByFragment;
import com.seniorproject.sallemapp.Activities.SearchFriendsFragment;
import com.seniorproject.sallemapp.R;

/**
 * Created by abdul on 17-Feb-2017.
 */

public  class FriendsPageAdapter extends FragmentStatePagerAdapter {

    private String friend_menu = String.valueOf(R.string.friends_menu_label);
    private String search_menu = String.valueOf(R.string.searchFriend_btn);
    private String requests_menu = String.valueOf(R.string.friends_requests_menu);
    private String nearby_menu = String.valueOf(R.string.nearFriends_label);

    //FragmentManager _fm;
    public FriendsPageAdapter(FragmentManager fm) {
        super(fm);
      //  this._fm = fm;
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
                return FriendsFragment.newInstance(0, friend_menu);
            case 1:
                return SearchFriendsFragment.newInstance(1, search_menu);
            case 2:
                return FriendRequestFragment.newInstance(2, requests_menu);
            case 3:
                return NearByFragment.newInstance(3, nearby_menu);
            default:
                return null ;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position){
            case   0:
                title = friend_menu;
                break;
            case 1:
                title = search_menu;
                break;
            case 2:
                title = requests_menu;
                break;
            case 3:
                title = nearby_menu;
                break;
            default:
                title = R.string.page_position + "" + position;
                break;
        }

        return title;

    }

}
