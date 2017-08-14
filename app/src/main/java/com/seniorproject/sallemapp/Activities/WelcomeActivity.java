package com.seniorproject.sallemapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.seniorproject.sallemapp.R;

public class WelcomeActivity extends AppCompatActivity {
    private static final int REQUEST_SALLEM_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        attachSigninButton();
        attachRegisterButton();
        getPermissions();

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-7249219499142063~4926980836");
        final NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        adView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        //TODO comment or uncomment test device in Google Ad.
        final AdRequest request = new AdRequest.Builder()/*.addTestDevice("DFDC2A32E5ECB1E43EB3ADAEFB76B2FF")*/.build();
        final boolean isTestDevice = request.isTestDevice(this);
        if (isTestDevice) {
        adView.loadAd(request);
         }
        VideoController vc = adView.getVideoController();
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Here apps can take action knowing video playback is finished
                // It's always a good idea to wait for playback to complete before
                // replacing or refreshing a native ad, for example.
                if (isTestDevice) {
                adView.loadAd(request);
                 }
                super.onVideoEnd();
            }
        });

    }

    private void attachRegisterButton() {
        Button registerButton = (Button)findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(registerIntent);
                //finish();
            }
        });
    }

    private void attachSigninButton() {
        Button signinButton = (Button)findViewById(R.id.btn_signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signinIntent);
               // finish();
            }
        });
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
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )
        {

            shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION);    //Manifest.permission_group.LOCATION
            shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE);            //Manifest.permission_group.STORAGE
            requestPermissions(
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_SALLEM_PERMISSIONS
            );

        }

/*        else {

            requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_SALLEM_PERMISSIONS );
        }*/

    }

}
