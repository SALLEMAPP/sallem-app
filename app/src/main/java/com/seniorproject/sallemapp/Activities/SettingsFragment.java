package com.seniorproject.sallemapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.localdb.UserDataSource;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 1000;
    private View mCurrentView;
    private Context mContext;
    ImageView mUserImageView;
    TextView mUserNameTextView;
    ProgressBar mSaveProgress;
    Switch mLocationSwitchButton;
    Button mSaveButton;
    int mStatus;
    Bitmap mUserAvatar;
    String mImageAsString;
    private int _page;
    private String _title;

    private OnFragmentInteractionListener mListener;
    public SettingsFragment() {
        // Required empty public constructor
    }

    private AdView mAdView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(int page, String title) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _page = getArguments().getInt(ARG_PARAM1);
            _title = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);  //Reorder onCreateView by Fisal
        mCurrentView =view;
        mContext =getContext();
        mUserImageView = (ImageView) mCurrentView.findViewById(R.id.settings_imageAvatar);
        mUserNameTextView = (TextView)mCurrentView.findViewById(R.id.settings_lblUserName);
        mSaveProgress =(ProgressBar)mCurrentView.findViewById(R.id.settings_progress);
        mSaveProgress.setVisibility(View.GONE);
        mLocationSwitchButton = (Switch)mCurrentView.findViewById(R.id.settings_switchLocationDiscrovery);
        mSaveButton = (Button) mCurrentView.findViewById(R.id.settings_btnSave);
        mStatus = DomainUser.CURRENT_USER.getStatus();
        mUserAvatar = DomainUser.CURRENT_USER.getAvatar();
        initSettings();
        initSaveSettings();
        attachOpenAvatar();
        attachSwitchButton();

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getContext(), "ca-app-pub-7249219499142063~4926980836");
        mAdView = (AdView) mCurrentView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
              // .addTestDevice("DFDC2A32E5ECB1E43EB3ADAEFB76B2FF")      //TODO comment or uncomment test device in Google Ad.
                .build();
      /*  boolean isTestDevice = adRequest.isTestDevice(getContext());
        if (isTestDevice) {*/
            mAdView.loadAd(adRequest);
        //}

        return view;

    }

    private void attachSwitchButton() {
        mLocationSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mStatus = 0;
                }
                else{
                  mStatus = 1;
                }

            }
        });


    }

    //below initSettings() added by Fisal to start loading Sallem Settings from SharedPreferences and view it to layout
    private void initSettings() {
        Bitmap scaledForProfilePhoto = Bitmap.createScaledBitmap(DomainUser.CURRENT_USER.getAvatar(), 400, 400, false);
        mUserImageView.setImageBitmap(scaledForProfilePhoto);
        mUserNameTextView.setText(DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName());
        switch (mStatus){
            case 0:
                mLocationSwitchButton.setChecked(true);
                break;
            case 1:
                mLocationSwitchButton.setChecked(false);
        }

    }



    //below initSettings() added by Fisal to start saving Sallem Settings to SharedPreferences
    private void initSaveSettings() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveButton.setEnabled(true);
                mSaveButton.setAlpha(0.5f);
                mSaveProgress.setVisibility(View.VISIBLE);
                saveSettings(DomainUser.CURRENT_USER.getId(), mStatus, mUserAvatar, mImageAsString);

            }
        });

    }


    private void attachOpenAvatar() {
        ImageButton openAvatarButton = (ImageButton) mCurrentView.findViewById(R.id.settings_btnOpenImage);
        openAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGalary();
            }
        });

    }


    private void openImageFromGalary(){
        Intent gallaryIntent = new Intent();
        gallaryIntent.setType("image/*");
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallaryIntent, REQUEST_CODE);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            if (data != null) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(
                            mContext.getContentResolver(), data.getData()
                    );
                    //Scale down for database iamge
                    mUserAvatar = Bitmap.createScaledBitmap(photo, 90, 90, false);
                    mImageAsString = MyHelper.ImageAsString(mUserAvatar);
                    //scale up for profile showing
                    Bitmap profileImage = Bitmap.createScaledBitmap(photo, 400,400,false);
                    mUserImageView.setImageBitmap(profileImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveSettings(final String id, final  int status, final Bitmap avatar, final String imageAsString){
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    MobileServiceClient client = MyHelper.getAzureClient(mContext);
                    MobileServiceTable<User> userTable = client.getTable(User.class);
                    User user =  userTable.where().field("id")
                            .eq(id).execute().get().get(0);
                    if(user != null){
                        user.setStatus(status);
                        if(imageAsString != null){
                            user.setImageTitle(imageAsString);
                        }
                        userTable.update(user).get();
                    }
                    UserDataSource userDS = new UserDataSource(mContext);
                    userDS.open();
                    userDS.update(id, status, avatar);
                    userDS.close();
                }
                catch (Exception e){
                    Log.e("Save Settings ", "doInBackground: " + e.getMessage() );
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                HomeActivity.refreshAvatar(mUserAvatar);
                DomainUser.CURRENT_USER.setAvatar(mUserAvatar);
                DomainUser.CURRENT_USER.setStatus(mStatus);
                DomainUser.CURRENT_USER.setImageTitle(mImageAsString);
                mSaveButton.setEnabled(true);
                mSaveButton.setAlpha(1.0f);
                mSaveProgress.setVisibility(View.GONE);


            }
        };
        task.execute();


    }
}
