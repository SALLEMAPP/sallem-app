package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class MyHelper {

    public static final String DEFAULT_AVATAR_TITLE= "3d788b86-4313-40aa-8a4f-172344ed139d";
    public static final String SHARED_PREFERENCE_NAME = "sallemappsettings";
    public static Bitmap getDefaultAvatar(Context context){
        Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user_avatar);
        return avatar;
    }
    public static byte[] encodeBitmap(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }
    public static String ImageAsString(Bitmap image){
        byte[] imageInbytes = encodeBitmap(image);
        String imageAsString = Base64.encodeToString(imageInbytes, Base64.DEFAULT);
        return imageAsString;
    }
    public static Bitmap decodeImage(byte[] stream){

        Bitmap bm = BitmapFactory.decodeByteArray(stream, 0, stream.length);
        return bm;
    }
    public static Bitmap decodeImage(String imageAsString){
        byte[] imageAsBytes = Base64.decode(imageAsString, Base64.DEFAULT);
        return decodeImage(imageAsBytes);
    }
    public static MobileServiceClient getAzureClient(Context context) throws MalformedURLException {
       MobileServiceClient client =
         new MobileServiceClient(
                    "http://sallemapp.azurewebsites.net", context);
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


    private static CloudBlobContainer mBlobContainer;

    public static CloudBlobContainer getBlobContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        if(mBlobContainer == null) {
            CloudStorageAccount account = CloudStorageAccount.parse(CommonMethods.storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            mBlobContainer = serviceClient.getContainerReference("sallemphotos");

        }
        return mBlobContainer;
    }
    public static void saveImageToDisk(Context context, String fileName, Bitmap image){
        FileOutputStream out = null;
        String name = fileName +"."+"jpg";
        try{
            out = context.openFileOutput(name, Context.MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static Bitmap readImageFromDisk(Context context, String fileName){
        String name = fileName +"."+"jpg";
        try {
            FileInputStream fileInputStream =
                    context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
            return b;
        }
        catch (Exception e){

        }
        return null;

    }

}
