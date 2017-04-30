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
import com.seniorproject.sallemapp.Activities.localdb.NotifyDataSource;
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
    public static Location CURRENT_LOCATION = null;
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
        //Initiate location tracking
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //Get location service
        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Get best provider among the three, GPS, network and WIFI
        String best = location.getBestProvider(criteria, true);
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Get location updated every minute and after 100 meters distance
            location.requestLocationUpdates(best, 60000, 100,  this);
        }
        //Refresh local cach from database
        listenForDatabaseChanges();


    }

    /**
     * Build and send notification to Android notification  center
     * @param title of the notification
     * @param content of the notification
     */
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
            String seenAt = MyHelper.getCurrentDateTime();
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
        //sendNotification("Service", "Location Changed");
        CURRENT_LOCATION = location;
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
                //Start in a backroind thread. Refersh content every 30 seconds
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

    /**
     * Refresh posts of user and his friends
     */
    private void refreshPosts(){
        String lastUpdate = "";
        LoadFriendsPostsAsync loadFriends = new LoadFriendsPostsAsync(getApplicationContext(),  this);
        loadFriends.LoadAsync(DomainUser.CURRENT_USER.getId(), lastUpdate);

    }

    /**
     * Check of there are new notifications for current user
     */
    private void refreshNotifies(){
        LoadNotifiesAsync loadNotifies = new LoadNotifiesAsync(DomainUser.CURRENT_USER.getId(), getApplicationContext(), this);
        loadNotifies.execute();

    }

    /**
     * Result of refreshing posts
     * @param result the new posts coming from database.
     */
    @Override
    public void onGotResult(List<DomainPost> result) {
            //update local cache
            mMyApp.Posts_Cach = result;
            //notify subscribed views that a new result has come.
            Intent i = new Intent();
            i.setAction(CommonMethods.ACTION_NOTIFY_REFRESH);
            sendBroadcast(i);
        }

    /**
     * Result of refreshing notifications
     * @param result the new notification from database.
     */
    @Override
    public void processFinish(List<Notify> result) {
        if(result != null) {
            for (Notify n : result){
                //Store them locally to be shown when user open notification fragment
                NotifyDataSource notifyDataSource =new NotifyDataSource(getApplicationContext());
                notifyDataSource.open();
                notifyDataSource.insert(n);
                notifyDataSource.close();
                //Publish them to Android notification
                String title = n.getTitle();
                String content = n.getSubject();
                sendNotification(title, content);
            }

        }

    }
}

