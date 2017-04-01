package com.seniorproject.sallemapp.helpers;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class AzureHelper {
    public static MobileServiceClient CreateClient(Context context) throws MalformedURLException {
     MobileServiceClient client =   new MobileServiceClient(
                "https://sallem.azurewebsites.net", context);
        client.setAndroidHttpClientFactory(new OkHttpClientFactory() {
            @Override
            public OkHttpClient createOkHttpClient() {
                OkHttpClient okHttpClient =new OkHttpClient();
                okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                return okHttpClient;
            }
        });
        return client;
    }
}