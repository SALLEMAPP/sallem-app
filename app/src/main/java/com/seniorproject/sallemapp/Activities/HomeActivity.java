package com.seniorproject.sallemapp.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.seniorproject.sallemapp.Activities.pagesadapters.ActivitiesPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.FriendsPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.HomePageAdater;
import com.seniorproject.sallemapp.Activities.pagesadapters.NearbyPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.NotificationPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.SettingsPageAdapter;
import com.seniorproject.sallemapp.R;


public class HomeActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        FriendsFragment.OnFragmentInteractionListener,
        SearchFriendsFragment.OnFragmentInteractionListener,
        FriendRequestFragment.OnFragmentInteractionListener,
        NearByFragment.OnFragmentInteractionListener,
        PostsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener,
        UpcomingActivitiesFragment.OnFragmentInteractionListener,
        OrganizeActivityFragment.OnFragmentInteractionListener,
        PastActivitiesFragment.OnFragmentInteractionListener

{
    FragmentStatePagerAdapter adapterViewPager;
    ViewPager myViewPager;
    private enum CurrentMenu{
        HOME,
        ACTIVITIES,
        FIRENDS,
    }
    private CurrentMenu _currnetMenu;
    FloatingActionButton fab;
    UserLocationService mService;
    boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myViewPager = (ViewPager)findViewById(R.id.viewPager);
        TabLayout  tableLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tableLayout.setupWithViewPager(myViewPager);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (_currnetMenu){
                    case HOME:
                        Intent addPost = new Intent(HomeActivity.this, AddPostActivity.class);
                        startActivity(addPost);
                        break;
                    case FIRENDS:
                        break;
                    case ACTIVITIES:
                        Intent addActivity = new Intent(HomeActivity.this, AddEventActivity.class);
                        startActivity(addActivity);
                        break;
                    default:
                        break;
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Simulate click on home menu item to make the one that loaded when the activity first loaded.
        MenuItem m =  navigationView.getMenu().getItem(0);
        onNavigationItemSelected(m);

        //Update navigation header to contains user information
        View v = navigationView.getHeaderView(0);
        TextView t =(TextView) v.findViewById(R.id.textView);
        t.setText("Abdullah");
        _currnetMenu = CurrentMenu.HOME;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ViewPager viewPager = null;

    @Override
    protected void onStart() {
        super.onStart();

            super.onStart();
        Intent intent = new Intent(this, UserLocationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound =false;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            adapterViewPager = new HomePageAdater(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            _currnetMenu = CurrentMenu.HOME;

        } else if (id == R.id.nav_friends) {

            adapterViewPager = new FriendsPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            _currnetMenu = CurrentMenu.FIRENDS;


        } else if (id == R.id.nav_near_friends) {
            adapterViewPager = new NearbyPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);


        } else if (id == R.id.nav_activities) {

            adapterViewPager = new ActivitiesPageAdapter(getSupportFragmentManager());
            //myViewPager.setOffscreenPageLimit(3);


            myViewPager.setAdapter(adapterViewPager);
            _currnetMenu = CurrentMenu.ACTIVITIES;
            updateFloatingActionBar(_currnetMenu);


        } else if (id == R.id.nav_notifications) {

            adapterViewPager = new NotificationPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);

        } else if (id == R.id.nav_settings) {

            adapterViewPager = new SettingsPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num,
            Toast.LENGTH_LONG).show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void updateFloatingActionBar(CurrentMenu selecteMenu){
        switch (selecteMenu){
            case HOME:
                break;
            case ACTIVITIES:
                fab.setImageResource(R.drawable.ic_me_24px);
                break;
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserLocationService.LocalBinder binder = (UserLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

}
