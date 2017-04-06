package com.seniorproject.sallemapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.BaseBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ListFragment;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.entities.UserLocation;
import com.seniorproject.sallemapp.entities.UserOnMap;
import com.seniorproject.sallemapp.helpers.AzureHelper;
import com.seniorproject.sallemapp.helpers.DownloadImage;
import com.seniorproject.sallemapp.helpers.LocationService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearByFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearByFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearByFragment extends Fragment {
    private static final int MY_PERMISSION_FOR_ACCESS_LOCATION = 2;
    MapView mMapView;
    private GoogleMap mGooglMap;
    Context mContext;
    private LocationManager mLocationManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
        View  v =  inflater.inflate( R.layout.fragment_near_by, container, false);
        mContext = getActivity().getApplicationContext();
        getPermissionToAccessLocation();
        mMapView = (MapView) v.findViewById (R.id.newFriends_mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try{
            MapsInitializer.initialize(mContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        mMapView.getMapAsync(
                new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mGooglMap = googleMap;
                        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            googleMap.setMyLocationEnabled(true);
                        }
                        Location lastLocation = LocationService.LAST_LOCATION;
                        updateLocation(lastLocation);
                    }
                }
        );
        SearchNearFriendsAsycn nearFriendsAsycn = new SearchNearFriendsAsycn(DomainUser.CURRENT_USER.getId());
        nearFriendsAsycn.execute();
            return  v;
        }

    @Override
    public void onStart() {
        super.onStart();


    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String _title;
    private int _page;

    private OnFragmentInteractionListener mListener;

    public NearByFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment NearByFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearByFragment newInstance(int page, String title) {
        NearByFragment fragment = new NearByFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 //   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

 //   @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateLocation(Location lastLocation) {
        LatLng newLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        String userName = DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName();
        mGooglMap.addMarker(new MarkerOptions().position(newLocation).title(userName)); //.snippet("Marker Description"));
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(newLocation).zoom(12).build();
        mGooglMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void updateMap(List<UserOnMap> result){
        for(UserOnMap user:result){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();


                Geocoder geo = new Geocoder(mContext);
                List<Address> addresses = null;

//                String address = currentContact.getStreetAddress() + ", " +
//                        currentContact.getCity() + ", " +
//                        currentContact.getState() + " " +
//                        currentContact.getZipCode();
//
//                try {
//                    addresses = geo.getFromLocationName(address, 1);
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
            LatLng point = new LatLng(user.getLatitude(),user.getLongitude());
            builder.include(point);
            BitmapDescriptor icon = null;
            if(user.getAvatar() != null){
             icon = BitmapDescriptorFactory.fromBitmap(user.getAvatar());
            }
            else {
                 icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_black_24dp);
            }

               mGooglMap.addMarker(new MarkerOptions().position(point).title(user.getUserName()).icon(icon));
            }
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),measuredWidth, measuredHeight, 100));

        }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == MY_PERMISSION_FOR_ACCESS_LOCATION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);


                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                    Toast.makeText(mContext, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @TargetApi(23)
    public void getPermissionToAccessLocation() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, MY_PERMISSION_FOR_ACCESS_LOCATION
            );

        }

    }

    public class SearchNearFriendsAsycn extends AsyncTask<Void,Void,List<UserOnMap>> {
        private final String mUserId;
        public SearchNearFriendsAsycn(String userId){
            mUserId = userId;
        }

        @Override
        protected List<UserOnMap> doInBackground(Void... params) {
            List<UserOnMap> resultUsers = new ArrayList<>();
            try {
                MobileServiceClient client = AzureHelper.CreateClient(mContext);
                MobileServiceTable<User> usersTable = client.getTable(User.class);
                MobileServiceTable<Friendship> friendsTable = client.getTable(Friendship.class);
                MobileServiceTable<UserLocation> locationTable = client.getTable(UserLocation.class);
                List<Friendship> friends = friendsTable.where().field("id").eq(mUserId)
                        .and().field("statusId").eq(2)
                        .execute().get();
                if(friends.size() > 0){
                    for(Friendship friend :friends) {
                        List<UserLocation> locations = locationTable.where().field("userId").eq(friend.getFriendId()).execute().get();
                        if (locations != null && locations.size() > 0) {
                            for (UserLocation friendLocation : locations) {
                                Location userCurrentLocation = LocationService.LAST_LOCATION;
                                double startLati = userCurrentLocation.getLatitude();
                                double startLongi = userCurrentLocation.getLongitude();

                                double endLati = friendLocation.getLatitude();
                                double endLongi = friendLocation.getLongitude();
                                float[] result = new float[1];
                                Location.distanceBetween(startLati, startLongi,
                                        endLati, endLongi, result
                                );
                                float distance = result[0];
                                if (distance <= 1000) {
                                    User user = usersTable.where().field("id").eq(friend.getFriendId()).execute().get().get(0);
                                    if (user != null) {
                                        Bitmap avatar = null;

                                        try {
                                            //In case no avatar, just fail gracefully.
                                            String title = user.getImageTitle() + ".jpg";
                                            avatar = DownloadImage.getImage(mContext, title);
                                        } catch (StorageException e) {
                                            //e.printStackTrace();
                                            //Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());
                                        }
                                        //DomainUser domainUser = new DomainUser(user);
                                        //domainUser.setAvatar(avatar);
                                        UserOnMap userOnMap = new UserOnMap();
                                        userOnMap.setUserId(user.getId());
                                        userOnMap.setUserName(user.getFirstName() + " "+ user.getLastName());
                                        userOnMap.setLatitude(friendLocation.getLatitude());
                                        userOnMap.setLongitude(friendLocation.getLongitude());
                                        userOnMap.setAvatar(avatar);
                                        resultUsers.add(userOnMap);

                                    }
                                }

                            }

                        }

                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEM APP", e.getCause().getMessage());
            }
            return resultUsers;
        }

        @Override
        protected void onPostExecute(List<UserOnMap> result) {
            if(result != null && result.size() > 0){
                updateMap( result);
            }
        }
    }
}
