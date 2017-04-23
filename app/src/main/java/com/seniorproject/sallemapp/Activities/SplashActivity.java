package com.seniorproject.sallemapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.seniorproject.sallemapp.Activities.localdb.UserDataSource;
import com.seniorproject.sallemapp.R;
import com.microsoft.windowsazure.mobileservices.*;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.helpers.MyHelper;

public class SplashActivity extends AppCompatActivity {
    private MobileServiceClient mClient;


    //Time period for splash window
    private static int SPLASH_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent welcomeIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
//                startActivity(welcomeIntent);
//                finish();
//            }
//        }, SPLASH_TIME);
        SharedPreferences shared = getSharedPreferences(MyHelper.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        final String userId = shared.getString("userid", null);
        if(userId == null){
                Intent welcomeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(welcomeIntent);
                finish();
        }
        else{
            AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    UserDataSource dc = new UserDataSource(SplashActivity.this);
                    dc.open();
                    DomainUser user = dc.getUser(userId);
                    dc.close();
                    if(user == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent welcomeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                startActivity(welcomeIntent);
                                finish();
                            }
                        });

                    }
                    else
                    {
                        DomainUser.CURRENT_USER = user;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(home);
                                finish();
                            }
                        });

                    }
                    return null;
                }
            };
            task.execute();
        }
    }




}
