package com.seniorproject.sallemapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.seniorproject.sallemapp.entities.User;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    MobileServiceClient _client;
    MobileServiceTable<User> _userTable;
    ProgressBar _savingProgressBar;
    public static final String storageConnectionString =
                    "DefaultEndpointsProtocol=http;" +
                    "AccountName=sallemappphotos;" +
                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==";
    Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        _savingProgressBar = (ProgressBar)findViewById(R.id.regist_saving_progress);
        _savingProgressBar.setVisibility(ProgressBar.GONE);
        attachRegisterButton();

        try {
            _client = new MobileServiceClient(
                    "https://sallem.azurewebsites.net",
                    this).withFilter(new ProgressFilter());

            _client.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient okHttpClient =new OkHttpClient();
                    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
                    okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                    return okHttpClient;
                }
            });
        }
        catch (MalformedURLException e){


        }
    }


    private void attachRegisterButton() {
        Button signinButton = (Button)findViewById(R.id.Btn_resgisteration);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();

            }
        });

    }
    private void registerUser(){
        String firstName = ((EditText)findViewById(R.id.txt_first_name)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.txt_last_name)).getText().toString();
        String email = ((EditText)findViewById(R.id.txt_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText4)).getText().toString();
        String joinedAt = new LocalDateTime().toString();
        //Byte[] photo = null;
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setJoinedAt(joinedAt);
        Drawable drawable = this.getResources().getDrawable(R.drawable.testimage);
        bm = ((BitmapDrawable)drawable).getBitmap();

        user.setStatus(0);

        AsyncTask task =  addUserToDb(user);


    }
    String imageName = null;
//    private AsyncTask<Void, Void, Void> uploadUserImage() {
//
//        AsyncTask<Void, Void,Void> task =new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                try {
//                    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
//                    CloudBlobClient serviceClient = account.createCloudBlobClient();
//
//                    // Container name must be lower case.
//                    CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
//                    //container.createIfNotExists();
//
//                    // Upload an image file.
//                    imageName = UUID.randomUUID().toString();
//                    CloudBlockBlob blob = container.getBlockBlobReference(imageName);
//
//                    File outputDir = getBaseContext().getCacheDir();
//                    File sourceFile = File.createTempFile("101", "jpg", outputDir);
//                    OutputStream outputStream = new FileOutputStream(sourceFile);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                    outputStream.close();
//                    blob.upload(new FileInputStream(sourceFile), sourceFile.length());
//
//                    // Download the image file.
//                    //File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
//                    //blob.downloadToFile(destinationFile.getAbsolutePath());
//
//                } catch (FileNotFoundException fileNotFoundException) {
//                    createAndShowDialogFromTask(fileNotFoundException, "Error");
//                } catch (StorageException storageException) {
//                    createAndShowDialogFromTask(storageException, "Error");
//                } catch (Exception e) {
//                    createAndShowDialogFromTask(e, "Error");
//                }
//                return null;
//            }
//        };
//
//        return task.execute();
//    }
    private String uploadUserImage(){
        String s = null;
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
            //container.createIfNotExists();

            // Upload an image file.

            s = UUID.randomUUID().toString() + ".jpg";
            CloudBlockBlob blob = container.getBlockBlobReference(s);

            File outputDir = getBaseContext().getCacheDir();
            File sourceFile = File.createTempFile("101", "jpg", outputDir);
            OutputStream outputStream = new FileOutputStream(sourceFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            blob.upload(new FileInputStream(sourceFile), sourceFile.length());

            // Download the image file.
            //File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
            //blob.downloadToFile(destinationFile.getAbsolutePath());



        } catch (FileNotFoundException fileNotFoundException) {
            createAndShowDialogFromTask(fileNotFoundException, "Error");
        } catch (StorageException storageException) {
            createAndShowDialogFromTask(storageException, "Error");
        } catch (Exception e) {
            createAndShowDialogFromTask(e, "Error");
        }
        return s;
    }

    private AsyncTask<Void,Void,Void> addUserToDb(User user) {


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
               try {

                   String s2 =  uploadUserImage();
                   user.setImageTitle(s2);
                   _userTable = _client.getTable(User.class);
                   _userTable.insert(user).get();




               }
               catch (Exception e){
                   createAndShowDialogFromTask(e, "Error");
               }


                return null;
            }

        };
       return task.execute();


    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (_savingProgressBar != null) _savingProgressBar.setVisibility(ProgressBar.VISIBLE);
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
                            if (_savingProgressBar != null) _savingProgressBar.setVisibility(ProgressBar.GONE);

                        }


                    });


                    resultFuture.set(response);
                    Intent signinIntent = new Intent(RegistrationActivity.this, SignInActivity.class);
                    startActivity(signinIntent);
                    finish();
                }
            });

            return resultFuture;
        }
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }
    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

}
