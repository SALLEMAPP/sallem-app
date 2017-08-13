package com.seniorproject.sallemapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.seniorproject.sallemapp.Activities.pagesadapters.FriendsPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.HomePageAdater;
import com.seniorproject.sallemapp.Activities.pagesadapters.NearbyPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.NotificationPageAdapter;
import com.seniorproject.sallemapp.Activities.pagesadapters.SettingsPageAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.SallemService;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.helpers.LocationService;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FriendsFragment.OnFragmentInteractionListener,
        SearchFriendsFragment.OnFragmentInteractionListener, FriendRequestFragment.OnFragmentInteractionListener,
        NearByFragment.OnFragmentInteractionListener, PostsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener,
        UpcomingActivitiesFragment.OnFragmentInteractionListener, OrganizeActivityFragment.OnFragmentInteractionListener,
        PastActivitiesFragment.OnFragmentInteractionListener,
        LocationService.LocationChanged, ServiceConnection
{
    FragmentStatePagerAdapter adapterViewPager;
    ViewPager myViewPager;
    Intent sallemService;
    private static final int REQUEST_SALLEM_PERMISSIONS = 123;
    private static NavigationView navigationView;

    @Override
    public void onLocationChanged(LatLng newLocation) {

    }



    private enum CurrentMenu{
        HOME,
        ACTIVITIES,
        FIRENDS,
    }
    private CurrentMenu _currnetMenu;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getPermissions();
        myViewPager = (ViewPager)findViewById(R.id.viewPager);
        TabLayout  tableLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tableLayout.setupWithViewPager(myViewPager);
        LocationService service = LocationService.getLocationManager(this);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Simulate click on home menu item to make the one that loaded when the activity first loaded.
        MenuItem m =  navigationView.getMenu().getItem(0);
        onNavigationItemSelected(m);

        //Update navigation header to contains user information
        View v = navigationView.getHeaderView(0);
        TextView userName=(TextView) v.findViewById(R.id.menu_lblUserName);
        TextView email=(TextView) v.findViewById(R.id.menu_lblEmail);
        userName.setText(DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName());
        email.setText(DomainUser.CURRENT_USER.getEmail());
        ImageView avatar =(ImageView) v.findViewById(R.id.navHeader_avatr);
        Bitmap scaledPhoto = Bitmap.createScaledBitmap(DomainUser.CURRENT_USER.getAvatar(), 185, 185, false);
        avatar.setImageBitmap(scaledPhoto);
        _currnetMenu = CurrentMenu.HOME;
        sallemService = new Intent(getApplicationContext(), SallemService.class);
        startService(sallemService);

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
    //No options menu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            adapterViewPager = new HomePageAdater(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            _currnetMenu = CurrentMenu.HOME;
            this.setTitle(R.string.addPost_btn);
            fab.setVisibility(View.VISIBLE);


        } else if (id == R.id.nav_friends) {

            adapterViewPager = new FriendsPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            _currnetMenu = CurrentMenu.FIRENDS;
            this.setTitle(R.string.friends_menu_label);
            fab.setVisibility(View.GONE);



        } else if (id == R.id.nav_near_friends) {
            adapterViewPager = new NearbyPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            this.setTitle(R.string.nearFriends_label);
            fab.setVisibility(View.GONE);


        }
//        else if (id == R.id.nav_activities) {
//
//            adapterViewPager = new ActivitiesPageAdapter(getSupportFragmentManager());
//            //myViewPager.setOffscreenPageLimit(3);
//
//
//            myViewPager.setAdapter(adapterViewPager);
//            _currnetMenu = CurrentMenu.ACTIVITIES;
//            updateFloatingActionBar(_currnetMenu);
//
//
//        }
        else if (id == R.id.nav_notifications) {

            adapterViewPager = new NotificationPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            this.setTitle(R.string.notifications_menu_label);
            fab.setVisibility(View.GONE);

        } else if (id == R.id.nav_settings) {

            adapterViewPager = new SettingsPageAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(adapterViewPager);
            this.setTitle(R.string.settings_menu_label);
            fab.setVisibility(View.GONE);

        }
           else if(id == R.id.nav_logout){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.logout_msg)
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SharedPreferences shared = getSharedPreferences(MyHelper.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                            shared.edit().putString("userid", null).apply();
                            MyApplication myApp = (MyApplication)getApplication();
                            myApp.Posts_Cach.clear();
                            myApp.Posts_Cach = null;
                            myApp.Friends_Cach.clear();
                            myApp.Friends_Cach = null;
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
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
                fab.setImageResource(R.drawable.ic_account_circle_black_24dp);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(sallemService, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        stopService(sallemService);
        super.onStop();
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Make sure it's our original READ_LOCATION request
        if (requestCode == REQUEST_SALLEM_PERMISSIONS) {

            Toast.makeText(getApplicationContext(), "Needed Permissions Granted", Toast.LENGTH_SHORT).show();

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @TargetApi(23)
    public void getPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        {

            shouldShowRequestPermissionRationale(
                    Manifest.permission_group.LOCATION);
            shouldShowRequestPermissionRationale(
                    Manifest.permission_group.STORAGE);
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_SALLEM_PERMISSIONS
            );

        }

    }
    public static void refreshAvatar(Bitmap image){
       if(navigationView != null) {
           View v = navigationView.getHeaderView(0);
           ImageView avatar = (ImageView) v.findViewById(R.id.navHeader_avatr);
           Bitmap scaledPhoto = Bitmap.createScaledBitmap(image, 185, 185, false);
           avatar.setImageBitmap(scaledPhoto);
       }

    }


}
