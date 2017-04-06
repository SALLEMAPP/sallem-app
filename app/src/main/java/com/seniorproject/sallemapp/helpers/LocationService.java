package com.seniorproject.sallemapp.helpers;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abdul on 01-Mar-2017.
 */

public class LocationService implements LocationListener {

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    public static Location LAST_LOCATION = null;
    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private static LocationService instance = null;

    private LocationManager locationManager;
    public double longitude;
    public double latitude;
    private LocationChanged _locationChanged;
    private  boolean isGPSEnabled =false;
    private boolean isNetworkEnabled =false;

    /**
     * Singleton implementation
     * @return
     */
    public static LocationService getLocationManager(Context context)     {
        if (instance == null) {
            instance = new LocationService(context);
        }

        return instance;
    }

    /**
     * Local constructor
     */
    private LocationService( Context context )     {
        initLocationService(context);
        _locationChanged = (LocationChanged) context;
        Log.d("Location Services", "LocationService created");
    }



    /**
     * Sets up location service after permissions is granted
     */
    //@TargetApi(23)
    private void initLocationService(Context context) {

//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }

        //try   {
        this.longitude = 0.0;
        this.latitude = 0.0;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER


        // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

//            if (!isNetworkEnabled && !isGPSEnabled)    {
//                // cannot get location
//                this.locationServiceAvailable = false;
//            }
            //else
            //{
                //this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)   {
                        LAST_LOCATION = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }//end if

                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)  {
                        LAST_LOCATION = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
        }
            //}
        //}
//        catch (Exception ex)  {
//            ex.printStackTrace();
//        }

    }


    @Override
    public void onLocationChanged(Location location)     {
       LAST_LOCATION = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        _locationChanged.onLocationChanged(latLng);


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
    public interface LocationChanged{
        void onLocationChanged(LatLng newLocation);
    }

}
