package com.seniorproject.sallemapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.Notify;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.entities.UserLocation;
import com.seniorproject.sallemapp.entities.UserOnMap;
import com.seniorproject.sallemapp.helpers.AzureBlob;
import com.seniorproject.sallemapp.helpers.LocationService;
import com.seniorproject.sallemapp.helpers.MyHelper;
import com.seniorproject.sallemapp.helpers.SendNotifyAsync;

import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearByFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearByFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearByFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, GoogleMap.OnMarkerClickListener {
    private static final int MY_PERMISSION_FOR_ACCESS_LOCATION = 2;
    MapView mMapView;
    private GoogleMap mGooglMap;
    Context mContext;
    private LocationManager mLocationManager;
    private View mCurrentView;
    private Marker mSelectedMarker;
    private ProgressBar mProgressBar;
    private BottomSheetBehavior mBottomSheetBehavior;
    List<Pair<LatLng, UserOnMap>> mUsersOnMarkers;
    private TextView mUserInfo;
    private Button mNotifyBotton;
    private String mNotifyReceiverId;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mCurrentView =  inflater.inflate( R.layout.fragment_near_by, container, false);
        mContext = getActivity().getApplicationContext();
        getPermissionToAccessLocation();
        mMapView = (MapView) mCurrentView.findViewById (R.id.newFriends_mapView);
        mProgressBar = (ProgressBar) mCurrentView.findViewById(R.id.nearFriends_progBar);
        View bottomSheet = mCurrentView.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
        mUsersOnMarkers = new ArrayList<>();
        mUserInfo = (TextView) mCurrentView.findViewById(R.id.bottom_lblInfo);
        attachButtomNotify();
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
                        googleMap.setOnMarkerClickListener(NearByFragment.this);
                        Location lastLocation = LocationService.LAST_LOCATION;
                        updateLocation(lastLocation);
                        mProgressBar.setVisibility(View.VISIBLE);
//                        mGooglMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                            @Override
//                            public void onMapClick(LatLng latLng) {
//                                PopupMenu popupMenu = new PopupMenu(getContext().getApplicationContext(), v);
//                                //MenuInflater menuInflater = popupMenu.getMenuInflater();
//                                //inflater.inflate(R.menu.map_popup_actions, popupMenu.getMenu());
//                                popupMenu.setOnMenuItemClickListener(NearByFragment.this);
//                                popupMenu.inflate(R.menu.map_popup_actions);
//                                popupMenu.show();
//                            }
//                        });
                    }
                }
        );
        SearchNearFriendsAsycn nearFriendsAsycn = new SearchNearFriendsAsycn(DomainUser.CURRENT_USER.getId());
        nearFriendsAsycn.execute();
            return  mCurrentView;
        }
        private void attachButtomNotify(){
            mNotifyBotton = (Button)mCurrentView.findViewById(R.id.bottom_btnNotify);
            mNotifyBotton.setEnabled(false);
            mNotifyBotton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentUserId = DomainUser.CURRENT_USER.getId();
                    if(mNotifyReceiverId != null && !mNotifyReceiverId.isEmpty() && !mNotifyReceiverId.equals(currentUserId)) {
                        List<Notify> notifies = new ArrayList<Notify>();
                        Notify notify = new Notify();
                        notify.setId(UUID.randomUUID().toString());
                        notify.setSourceUser(currentUserId);
                        notify.setDestUser(mNotifyReceiverId);
                        notify.setTitle(DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName());
                        notify.setSubject("Invited you for a meeting");
                        notify.setDelivered(false);
                        notify.setPublishedAt(new LocalDateTime().toString());
                        notifies.add(notify);
                        SendNotifyAsync sendNotify = new SendNotifyAsync(notifies, mContext);
                        sendNotify.execute();
                    }
                }
            });
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
        LatLng point = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        String userName = DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName();
        mGooglMap.addMarker(new MarkerOptions().position(point).title(userName)); //.snippet("Marker Description"));
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
        mGooglMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void updateMap(List<UserOnMap> result){
        for(UserOnMap user:result){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
          List<Address> addresses = null;
            Geocoder geocoder = new Geocoder(mContext);
            List<Address> list = null;
            try {
                list = geocoder.getFromLocation(user.getLatitude(), user.getLongitude(), 1);

            } catch (IOException e) {

            }
            Address address = list.get(0);
//            if (marker != null) {
//                marker.remove();
//            }
            LatLng point = new LatLng(user.getLatitude(),user.getLongitude());
            BitmapDescriptor icon = null;
            if(user.getAvatar() != null){
                icon = BitmapDescriptorFactory.fromBitmap(user.getAvatar());
            }
            else {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_black_24dp);
            }
            MarkerOptions options = new MarkerOptions()
                    .title(address.getLocality())
                    .position(point)
                    .icon(icon) ;
            //marker = mMap.addMarker(options);
           builder.include(point);
             Marker m =  mGooglMap.addMarker(options);
            Pair<LatLng, UserOnMap> userOnMaker = new Pair<>(point, user);
            mUsersOnMarkers.add(userOnMaker);
            }
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),measuredWidth, measuredHeight, 100));
        }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sendNotify:
                Toast.makeText(mContext, mSelectedMarker.getTitle() , Toast.LENGTH_LONG).show();
                return true;

            default:
                return false;
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        mSelectedMarker = marker;
        //showPopup(mProgressBar);
        UserOnMap userOnMap = null;
        for(Pair<LatLng, UserOnMap> u: mUsersOnMarkers){
            if(u.first.equals(marker.getPosition())){
                userOnMap = u.second;
                break;
            }
        }
        if(userOnMap != null) {
            mNotifyBotton.setEnabled(true);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mUserInfo.setText(userOnMap.getUserName());
            mNotifyReceiverId = userOnMap.getUserId();
        }
        return true;
    }
    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        //MenuInflater menuInflater = popupMenu.getMenuInflater();
        //inflater.inflate(R.menu.map_popup_actions, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(NearByFragment.this);
        popupMenu.inflate(R.menu.map_popup_actions);

        popupMenu.show();
    }
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new
            BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if(newState == BottomSheetBehavior.STATE_HIDDEN){
                      mNotifyBotton.setEnabled(false);
                    }
                    if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                        mNotifyBotton.setEnabled(false);
                    }
                   }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            };


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
                MobileServiceClient client = MyHelper.getAzureClient(mContext);
                MobileServiceTable<User> usersTable = client.getTable(User.class);
                MobileServiceTable<Friendship> friendsTable = client.getTable(Friendship.class);
                MobileServiceTable<UserLocation> locationTable = client.getTable(UserLocation.class);
                List<Friendship> friends = friendsTable.where().field("id").eq(mUserId)
                        .and().field("statusId").eq(2)
                        .execute().get();
                if(friends.size() > 0){
                    for(Friendship friend :friends) {
                        String lastSeen = new LocalDateTime().minusMinutes(5).toString();
//                        List<UserLocation> locations = locationTable.where().field("userId").eq(friend.getFriendId())
//                                .and().field("seenAt").ge(lastSeen).execute().get();
                        List<UserLocation> locations = locationTable.where().field("userId").eq(friend.getFriendId())
                                .execute().get();
                        String tempUserId = null;
                        if (locations != null && locations.size() > 0) {
                            for (UserLocation friendLocation : locations) {
                                //Check to take the user once, in case has has more than one location
                                if(friendLocation.getUserId().equals(tempUserId)){continue;}

                                tempUserId = friendLocation.getUserId();
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
                                if (distance <= 3000) {
                                    User user = usersTable.where().field("id").eq(friend.getFriendId()).execute().get().get(0);
                                    if (user != null) {
                                        Bitmap avatar = null;
                                        String imageTitle = user.getImageTitle();
                                        if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {

                                            avatar = MyHelper.decodeImage(imageTitle);
                                        }
                                        else{
                                            avatar =  MyHelper.getDefaultAvatar(mContext);
                                        }
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
            mProgressBar.setVisibility(View.GONE);
            if(result != null && result.size() > 0){
                updateMap( result);
            }
        }
    }
}
