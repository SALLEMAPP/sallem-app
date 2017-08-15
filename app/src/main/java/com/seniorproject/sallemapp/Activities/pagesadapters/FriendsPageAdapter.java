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

/*    private Context nContext;
    private Context context = nContext;;*/

    //private Resources context;
  // private String friend_menu = /*context.getString*/String.valueOf(R.string.friends_menu_label);
/*    private String friend_menu = context.getResources().getString(R.string.friends_menu_label);
    private String search_menu = context.getResources().getString(R.string.searchFriend_btn);
    private String requests_menu = context.getResources().getString(R.string.friends_requests_menu);
    private String nearby_menu = getPageTitle(R.string.nearFriends_label);*/


    //FragmentManager _fm;
   // private Context _context;
    public FriendsPageAdapter(FragmentManager fm) {
        super(fm);
       // _context = c;
      //  this._fm = fm;
    }

  //  private String friend_menu = _context.getResources().getString(R.string.friends_menu_label);

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
                return NearByFragment.newInstance(3, "Nearby");
            default:
                return null ;
        }
    }



    @Override
    public CharSequence getPageTitle(int position) {
        //CharSequence title = null;
        String title;
        switch (position){
            case   0:
                title = "Friends الأصدقاء";
                break;
            case 1:
                title = "Search بحث";
                break;
            case 2:
                title = "Requests الطلبات";
                break;
            case 3:
                title = "Nearby بالقرب";
                break;
            default:
                title = R.string.page_position + "" + position;
                break;
        }

        return title;

    }

}
