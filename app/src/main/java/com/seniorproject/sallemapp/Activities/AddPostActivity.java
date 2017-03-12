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
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Post;
import com.seniorproject.sallemapp.entities.PostImage;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AddPostActivity extends AppCompatActivity implements EntityAsyncResult<Post> {
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=sallemappphotos;" +
                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==";
    Bitmap bm;
    private static final int REQUEST_CODE = 121;
    ProgressBar progressBar;
    private String mPostId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        attchPostButton();
        attachOpenGalaryButton();


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

    private void openImageFromGalary() {
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
        String userId = DomainUser.CURRENT_USER.getId();
        Post post = new Post();

        post.setId(UUID.randomUUID().toString());
        post.setPostedAt(postedAt);
        post.setUserId(userId);
        post.setSubject(subject);
        if (bm != null) {
            post.set_imagePath(UUID.randomUUID().toString() + ".jpg");
        }
        //
        SavePostAsync savePostAsync = new SavePostAsync(post);
        savePostAsync.delegate = this;
        savePostAsync.execute();


    }

    @Override
    public void processFinish(Post result) {
        mPostId = result.getId();
        finish();

    }



    public class SavePostAsync extends AsyncTask<Void, Void, Post> {
        EntityAsyncResult<Post> delegate;
        MobileServiceClient mClient;
        MobileServiceTable<Post> mPostTable;
        MobileServiceTable<PostImage> mPostImageTable;

        private Post mPost;

        public SavePostAsync(Post post) {
            mPost = post;
        }

        @Override
        protected Post doInBackground(Void... params) {
            String imagePath = mPost.get_imagePath();
            try {

                mClient = new MobileServiceClient(
                        "https://sallem.azurewebsites.net",
                        AddPostActivity.this);
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
                    e.printStackTrace();
                }


                if (imagePath != null &&  !imagePath.isEmpty()) {

                    try{
                    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                    CloudBlobClient serviceClient = account.createCloudBlobClient();

                    // Container name must be lower case.
                    CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
                    //container.createIfNotExists();

                    // Upload an image file.
                    CloudBlockBlob blob = container.getBlockBlobReference(imagePath);

                    File outputDir = getBaseContext().getCacheDir();
                    File sourceFile = File.createTempFile("101", "jpg", outputDir);
                    OutputStream outputStream = new FileOutputStream(sourceFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    outputStream.close();
                    blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (StorageException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPostTable = mClient.getTable(Post.class);
           try {
               mPostTable.insert(mPost).get();
           }
           catch (ExecutionException e){
               e.printStackTrace();
           }
           catch (InterruptedException e){
               e.printStackTrace();
           }
            return mPost;
        }


        @Override
        protected void onPostExecute(Post post) {
            if (delegate != null) {
                delegate.processFinish(post);
            }
        }
    }


}
