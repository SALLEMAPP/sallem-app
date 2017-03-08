package com.seniorproject.sallemapp.helpers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.NotificationManager;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.seniorproject.sallemapp.Activities.RegistrationActivity;
import com.seniorproject.sallemapp.R;

/**
 * Created by abdul on 02-Mar-2017.
 */

public class MyHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onRegistered(Context context, final String gcmRegistrationId) {
        super.onRegistered(context, gcmRegistrationId);
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try{

                    RegistrationActivity._client.getPush().register(gcmRegistrationId);


                    return null;
                }
                catch (Exception e){
                   Log.d("SALLEM", e.getCause().getMessage());
                }

                return null;
            }
        };
        task.execute();
    }

    @Override
    public void onReceive(Context context, Bundle bundle) {
        super.onReceive(context, bundle);
        String msg = bundle.getString("message");
        PendingIntent contentIntent = PendingIntent
                .getActivity(context,
                        0,
                        new Intent(context, RegistrationActivity.class),
                        0);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification Hub Demo")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .build();
        NotificationManager  notificationsManager
                = (NotificationManager )
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationsManager.notify(NOTIFICATION_ID, notification);


    }
}
