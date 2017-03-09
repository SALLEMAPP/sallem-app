package com.seniorproject.sallemapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.PostImage;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AddPostActivity extends AppCompatActivity {
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=sallemappphotos;" +
                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==";
    Bitmap bm;
    private static final int REQUEST_CODE = 121;
    ProgressBar progressBar;
    MobileServiceClient mClient;
    MobileServiceTable<Post> mPostTable;
    MobileServiceTable<PostImage> mPostImageTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        attchPostButton();
        attachOpenGalaryButton();
        try {
            mClient = new MobileServiceClient(
                    "https://sallem.azurewebsites.net",
                    AddPostActivity.this).withFilter(new ProgressFilter());
            ;


            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
                    okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                    return okHttpClient;
                }
            });
        }

        catch (MalformedURLException e){
        Log.d("SALLEM APP", e.getCause().getMessage());

    }

}

    private void attachOpenGalaryButton() {
        ImageButton b = (ImageButton) findViewById(R.id.addPost_imgbtnChoosePhoto);
        b.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            ImageView imageView = (ImageView) findViewById(R.id.addPost_imgPostImage);

            if (data != null) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(), data.getData()
                    );
                    Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 740, 412, false);
                    imageView.setImageBitmap(scaledPhoto);
                    bm = scaledPhoto;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getPicture(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }

    private void attchPostButton() {
        Button b = (Button) findViewById(R.id.addPost_btnPost);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    private void addPost() {
        String subject = ((EditText) findViewById(R.id.addPost_txtPostSubject)).getText().toString();
        ImageView image = (ImageView) findViewById(R.id.addPost_imgPostImage);
        String postedAt = new LocalDateTime().toString();
        String userId = "095bd4e3-8331-4626-a1ef-0a622eed1b59";
        Post post = new Post();

        post.setId(UUID.randomUUID().toString());
        post.setPostedAt(postedAt);
        post.setUserId(userId);
        post.setSubject(subject);
        //
        PostImage postImage = new PostImage();
        postImage.setId(UUID.randomUUID().toString());
        postImage.setPostId(post.getId());
        String imagePath = UUID.randomUUID().toString();
        postImage.set_path(imagePath);
        insert(post, postImage);


    }

    private void insert(final Post post, final PostImage postImage) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try
                {

                    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                    CloudBlobClient serviceClient = account.createCloudBlobClient();

                    // Container name must be lower case.
                    CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
                    //container.createIfNotExists();

                    // Upload an image file.

                    String s = postImage.get_path() + ".jpg";
                    CloudBlockBlob blob = container.getBlockBlobReference(s);

                    File outputDir = getBaseContext().getCacheDir();
                    File sourceFile = File.createTempFile("101", "jpg", outputDir);
                    OutputStream outputStream = new FileOutputStream(sourceFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    outputStream.close();
                    blob.upload(new FileInputStream(sourceFile), sourceFile.length());

                    mPostTable = mClient.getTable(Post.class);
                    mPostImageTable = mClient.getTable(PostImage.class);
                    mPostTable.insert(post).get();
                    mPostImageTable.insert(postImage);

                } catch (FileNotFoundException fileNotFoundException) {
                    Log.d("SALLEM APP", fileNotFoundException.getCause().getMessage());
                } catch (StorageException storageException) {
                    Log.d("SALLEM APP", storageException.getCause().getMessage());


                } catch (IOException e){
                    Log.d("SALLEM APP", e.getCause().getMessage());

                } catch (URISyntaxException e){
                    Log.d("SALLEM APP", e.getCause().getMessage());

                }catch (InvalidKeyException e){
                    Log.d("SALLEM APP", e.getCause().getMessage());
                }
                catch (Exception e) {
                    Log.d("SALLEM APP", e.getCause().getMessage());
                }


                return null;
            }
        };
        task.execute();
    }
    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);

                        }


                    });



                    //finish();
                }
            });

            return resultFuture;
        }
    }

}
