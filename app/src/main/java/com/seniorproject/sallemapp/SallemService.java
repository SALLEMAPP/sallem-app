package com.seniorproject.sallemapp;

/**
 * Created by abdul on 04-Apr-2017.
 */

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.HomeActivity;
import com.seniorproject.sallemapp.Activities.PostsFragment;
import com.seniorproject.sallemapp.Activities.localdb.PostDataSource;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.FriendPost;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.UserLocation;
import com.seniorproject.sallemapp.helpers.AzureHelper;
import com.seniorproject.sallemapp.helpers.CachStore;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.LoadFriendsPostsAsync;
import com.seniorproject.sallemapp.helpers.LoadPostsAsync;
import com.seniorproject.sallemapp.helpers.RefreshedPostsResult;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by abdul on 04-Mar-2017.
 */

public class SallemService extends Service implements LocationListener, RefreshedPostsResult {
    private static final int SALLEM_NOTIFY = 0x2013;
    private LocationManager location = null;
    private NotificationManager notifier = null;
    public static final String SALLEM_SERVICE = "com.seniorproject.sallemapp.SallemService.SERVICE";
    public static boolean LISTEN_TO_DATABASE =true;
    public void onCreate(){
        super.onCreate();
        initializeLocation();
    }

    private void initializeLocation() {
        location =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        notifier =(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        doServiceStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doServiceStart(intent, startId);
        return Service.START_REDELIVER_INTENT;
    }
    private void doServiceStart(Intent intent, int startId){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String best = location.getBestProvider(criteria, true);
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            location.requestLocationUpdates(best, 30000, 0, (LocationListener) this);
        }
        sendNotification("Service start");
        listenForDatabaseChanges();


    }



    private void sendNotification(String content){
        Intent toLauch = new Intent(
                getApplicationContext(),
                HomeActivity.class
        );
        PendingIntent intentBack =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0, toLauch,0
                );
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        getApplicationContext()
                );
        builder.setTicker("SALLEM");
        builder.setSmallIcon(
                android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("SALLEM");
        builder.setContentText(content );
        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);
        Notification notify =
                builder.build();
        notifier.notify(SALLEM_NOTIFY, notify);
    }

    public void onDestroy() {
        if(location != null){
            location.removeUpdates((LocationListener) this);
            location = null;
        }
        super.onDestroy();
        Log.e("SALLEMAPP", "Sallem service onDestroy called");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void saveLocation(Location location){
        MobileServiceClient client;
        MobileServiceTable<UserLocation> userLocationTable;
        try {
            client = AzureHelper.CreateClient(getApplicationContext());
            UserLocation userLocation = new UserLocation();
            userLocation.setId(UUID.randomUUID().toString());
            userLocation.setLongitude(location.getLongitude());
            userLocation.setLatitude(location.getLatitude());
            userLocation.setUserId(DomainUser.CURRENT_USER.getId());
            String seenAt = new LocalDateTime().toString();
            userLocation.setSeenAt(seenAt);
            userLocationTable = client.getTable(UserLocation.class);
            userLocationTable.insert(userLocation);
        }
        catch (Exception e){
            Log.d("SALLEM APP", e.getCause().getMessage());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        sendNotification("Location Changed");
        saveLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void listenForDatabaseChanges() {
            try {
                final Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                while (LISTEN_TO_DATABASE) {
                                    refreshPosts();
                                    try {
                                        Thread.sleep(60000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                );
                thread.start();
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEMAPP", "listenForDatabaseChanges:" + e.getCause().getMessage()  );
            }



    }
    private void refreshPosts(){

//        if(cachedPost != null && cachedPost.size() > 0){
//          DomainPost p = Collections.max(cachedPost,
//                  new Comparator<DomainPost>() {
//                      @Override
//                      public int compare(DomainPost o1, DomainPost o2) {
//                          return o1.get_postedAt().compareTo(o2.get_postedAt()) ;
//                      }
//                  }
//          );
//            date = p.get_postedAt();
//        }

        //LoadPostsAsync loadPosts = new LoadPostsAsync(getApplicationContext(), date, this);
        //loadPosts.execute();
        LoadFriendsPostsAsync loadFriends = new LoadFriendsPostsAsync(getApplicationContext(), this);
        loadFriends.LoadAsync(DomainUser.CURRENT_USER.getId());

    }




    @Override
    public void onGotResult(List<DomainPost> result) {

//        if(Looper.getMainLooper().getThread() == Thread.currentThread()){
//            Log.e("SALLEMAPP", "onGotResult: ON UI THREAD" );
//        }
//        else{
//            Log.e("SALLEMAPP", "onGotResult: NOT UI THREAD" );
//        }
        CachStore.POSTS_CACH = result;
        Intent i = new Intent();
        i.setAction(CommonMethods.ACTION_NOTIFY_REFRESH);
        sendBroadcast(i);

    }
}

