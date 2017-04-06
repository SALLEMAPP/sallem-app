package com.seniorproject.sallemapp.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by abdul on 07-Mar-2017.
 */

public class CommonMethods {


    public final static String APP_TAG = "SALLEM APP";
//    public static final String storageConnectionString =
//            "DefaultEndpointsProtocol=http;" +
//                    "AccountName=sallemappphotos;" +
//                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==";
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=sallemappphotos;" +
                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==;EndpointSuffix=core.windows.net";

    public static final String ACTION_NOTIFY_REFRESH ="com.seniorproject.sallemapp.helpers.NOTIFY_REFRESH";
}
