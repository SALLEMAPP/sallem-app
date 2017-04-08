package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.AddPostActivity;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.PostImage;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdul on 07-Apr-2017.
 */

public class SavePostAsync extends AsyncTask<Void, Void, Object> {
    private Context mContext;
    private Post mPost;
    private Bitmap mImage;
    public SavePostAsync(Post post, Bitmap image, Context context) {
        mPost = post;
        mContext = context;
        mImage = image;
    }

    @Override
    protected Object doInBackground(Void... params) {
       Object result = null;
        try {
           MobileServiceClient client = MyHelper.getAzureClient(mContext);
           MobileServiceTable postTable = client.getTable(Post.class);
           String imagePath = mPost.get_imagePath();
           if (imagePath != null &&  !imagePath.isEmpty()) {
               AzureBlob.upload(imagePath, mImage, mContext);
           }
          result =  postTable.insert(mPost).get();

       }
       catch (Exception e){
           Log.e("Save Post Async", "doInBackground: "  +e.getMessage() );
       }
        return result;
    }

}
