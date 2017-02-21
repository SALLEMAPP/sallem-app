package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.seniorproject.sallemapp.R;
import com.microsoft.windowsazure.mobileservices.*;
public class SplashActivity extends AppCompatActivity {
    private MobileServiceClient mClient;


    //Time period for splash window
    private static int SPLASH_TIME = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        }, SPLASH_TIME);
        mClient = new MobileServiceClient(
                "https://sallem.azurewebsites.net",
                this
        );
        TodoItem item = new TodoItem();
        item.Text = "Awesome item";
        mClient.getTable(TodoItem.class).insert(item, new TableOperationCallback<item>() {
            public void onCompleted(TodoItem entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    // Insert succeeded
                } else {
                    // Insert failed
                }
            }
        });
    }
    public class TodoItem {
        public String Id;
        public String Text;
    }
}
