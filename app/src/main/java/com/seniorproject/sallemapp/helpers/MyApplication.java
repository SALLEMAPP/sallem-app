package com.seniorproject.sallemapp.helpers;

import android.app.Application;
import android.content.Context;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdul on 06-Apr-2017.
 */

public class MyApplication extends Application {
    public List<DomainUser> Friends_Cach;
    public List<DomainPost> Posts_Cach;



}
