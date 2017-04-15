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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.HomeActivity;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Notify;
import com.seniorproject.sallemapp.entities.UserLocation;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.seniorproject.sallemapp.helpers.LoadFriendsPostsAsync;
import com.seniorproject.sallemapp.helpers.LoadNotifiesAsync;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;
import com.seniorproject.sallemapp.helpers.RefreshedPostsResult;

import org.joda.time.LocalDateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by abdul on 04-Mar-2017.
 */

public class SallemService extends Service implements LocationListener, RefreshedPostsResult, ListAsyncResult<Notify> {
    private static final int SALLEM_NOTIFY = 0x2013;
    private LocationManager location = null;
    private NotificationManager notifier = null;
    public static final String SALLEM_SERVICE = "com.seniorproject.sallemapp.SallemService.SERVICE";
    public static boolean LISTEN_TO_DATABASE =true;
    MyApplication mMyApp;
    public void onCreate(){
        super.onCreate();
        mMyApp =(MyApplication) getApplication();
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
            //Get location updated every minute and after 100 distance
            location.requestLocationUpdates(best, 60000, 100,  this);
        }
        sendNotification("Service", "Service started");
        listenForDatabaseChanges();


    }



    private void sendNotification(String title, String content){
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
                R.drawable.ic_sallem_notify);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
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
        Log.e("Sallem service", "Sallem service onDestroy called");

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
            client = MyHelper.getAzureClient(getApplicationContext());
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
        sendNotification("Service", "Location Changed");
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
                                    refreshNotifies();
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


        String lastUpdate = "";
        //Note if we assign the last update argument, local cach will go out of sync.
//        if(mMyApp.Posts_Cach != null && mMyApp.Posts_Cach.size() > 0){
//            DomainPost max = Collections.max(mMyApp.Posts_Cach, new Comparator<DomainPost>() {
//                @Override
//                public int compare(DomainPost o1, DomainPost o2) {
//                    return o1.get_postedAt().compareTo(o2.get_postedAt());
//                }
//            });
//            lastUpdate = max.get_postedAt();
//        }
        LoadFriendsPostsAsync loadFriends = new LoadFriendsPostsAsync(getApplicationContext(),  this);
        loadFriends.LoadAsync(DomainUser.CURRENT_USER.getId(), lastUpdate);

    }
    private void refreshNotifies(){
        LoadNotifiesAsync loadNotifies = new LoadNotifiesAsync(DomainUser.CURRENT_USER.getId(), getApplicationContext(), this);
        loadNotifies.execute();

    }




    @Override
    public void onGotResult(List<DomainPost> result) {
        if(result == null){
            return;
        }
            mMyApp.Posts_Cach.addAll(0, result);
            Intent i = new Intent();
            i.setAction(CommonMethods.ACTION_NOTIFY_REFRESH);
            sendBroadcast(i);
        }
    @Override
    public void processFinish(List<Notify> result) {
        if(result != null) {
            for (Notify n : result){
                String title = n.getTitle();
                String content = n.getSubject();
                sendNotification(title, content);
            }

        }

    }
}

