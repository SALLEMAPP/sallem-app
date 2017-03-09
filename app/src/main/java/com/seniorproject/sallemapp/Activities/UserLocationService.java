package com.seniorproject.sallemapp.Activities;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.UserLocation;
import com.seniorproject.sallemapp.helpers.LocationService;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.LocalDateTime;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdul on 04-Mar-2017.
 */

public class UserLocationService extends Service implements LocationService.LocationChanged  {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    private LatLng previousLocation;



    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        UserLocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return UserLocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        LocationService service = LocationService.getLocationManager(this);
        return 0;
    }

    @Override
    public void onLocationChanged(LatLng newLocation) {
        saveLocation(newLocation);

    }
    private void saveLocation(LatLng location){
        MobileServiceClient client;
        MobileServiceTable<UserLocation> userLocationTable;
        try {
            client = new MobileServiceClient(
                    "https://sallem.azurewebsites.net",
                    this);


            client.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient okHttpClient =new OkHttpClient();
                    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
                    okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                    return okHttpClient;
                }
            });
            UserLocation userLocation = new UserLocation();
            userLocation.setId(UUID.randomUUID().toString());
            userLocation.setLongitude(location.longitude);
            userLocation.setLatitude(location.latitude);
            userLocation.setUserId("095bd4e3-8331-4626-a1ef-0a622eed1b59");
            String seenAt = new LocalDateTime().toString();
            userLocation.setSeenAt(seenAt);
            userLocationTable = client.getTable(UserLocation.class);
            userLocationTable.insert(userLocation);


        }
        catch (Exception e){
            Log.d("SALLEM APP", e.getCause().getMessage());
        }

    }
}
