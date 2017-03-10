package com.seniorproject.sallemapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class RegistrationActivity extends AppCompatActivity {

   public static MobileServiceClient _client;
    MobileServiceTable<User> _userTable;
    ProgressBar _savingProgressBar;
    public static final String storageConnectionString =
                    "DefaultEndpointsProtocol=http;" +
                    "AccountName=sallemappphotos;" +
                    "AccountKey=0ROm5ARwztUrPMEWcVuZYb4EgOS7/rB5v0y0kuaNPgRkoTnjBhHFXqaT82ydmgIIV+GeUqpCR5Mq/gI7WVcYyA==";
    Bitmap bm;
    public static final  String SENDER_ID ="1091231496982";
    private static final int REQUEST_CODE = 1000;
    private EditText mTextFirstName =null;
    private EditText mTextLastName = null;
    private EditText mTextEmail = null;
    private EditText mTextPassword = null;
    private EditText mTextRePassword = null;

    private ImageButton mButtoneAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        _savingProgressBar = (ProgressBar)findViewById(R.id.registeration_prgSaving);
        _savingProgressBar.setVisibility(ProgressBar.GONE);
        mTextFirstName = ((EditText) findViewById(R.id.registeration_txtfirstName));
        mTextLastName = ((EditText) findViewById(R.id.registeration_txtLastName));
        mTextEmail = ((EditText) findViewById(R.id.registeration_txtemail));
        mTextPassword = ((EditText) findViewById(R.id.registeration_txtPassword));
        mButtoneAvatar = ((ImageButton) findViewById(R.id.registration_btnAvatar));
        mTextRePassword = (EditText) findViewById(R.id.registeration_txtConfirmPassword);

        attachRegisterButton();
        attachOpenAvatar();


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
            Log.d("SALLEMAPP", e.getCause().getMessage());

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
    private void attachOpenAvatar() {
        ImageButton openAvatarButton = (ImageButton) findViewById(R.id.registration_btnAvatar);
        openAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageFromGalary();

            }
        });

    }

    private void registerUser() {
        if (isValidUser()) {
            String firstName = mTextFirstName.getText().toString();
            String lastName = mTextLastName.getText().toString();
            String email = mTextEmail.getText().toString();
            String password = mTextPassword.getText().toString();
            String joinedAt = new LocalDateTime().toString();
            //Byte[] photo = null;
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setEmail(email);
            user.setJoinedAt(joinedAt);
            user.setImageTitle(UUID.randomUUID().toString());
            user.setStatus(0);

            addUserToDb(user);
        }


    }
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
    private void uploadUserImage(String avatarName){
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
            //container.createIfNotExists();

            // Upload an image file.

            String name = avatarName + ".jpg";
            CloudBlockBlob blob = container.getBlockBlobReference(name);

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
    }

    private void addUserToDb(final User user) {


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
               try {

                   uploadUserImage(user.getImageTitle());
                   _userTable = _client.getTable(User.class);
                   _userTable.insert(user).get();
               }
               catch (Exception e){
                   createAndShowDialogFromTask(e, "Error");
               }
                return null;
            }

        };
       task.execute();


    }

    public boolean isValidUser() {
        clearErrors();

        boolean valid = true;
        String firstName = mTextFirstName.getText().toString();
        String lastName = mTextLastName.getText().toString();
        String email = mTextEmail.getText().toString();
        String password = mTextPassword.getText().toString();
        String rePassword = mTextRePassword.getText().toString();
        if(firstName.isEmpty()){
            mTextFirstName.setError("You must enter your first name");
            valid = false;
        }
        if(lastName.isEmpty()){
            mTextLastName.setError("You must enter your last name");
            valid = false;
        }
        if(email.isEmpty()){
            mTextEmail.setError("You must enter your Email");
            valid = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mTextEmail.setError("You must enter a valid Email");
        }
        if(password.length() < 4){
            mTextPassword.setError("Password must be at least 4 characters");
            valid = false;
        }
        if(rePassword.isEmpty()){
            mTextRePassword.setError("Password is not matched");
            valid = false;
        }
        if(!rePassword.equals(password)){
            mTextRePassword.setError("Password is not matched");
            valid = false;
        }
//        try {
//            if (registeredEmail(email)) {
//                mTextEmail.setError("This email has already registered");
//                valid = false;
//            }
//        }
//        catch (Exception e){
//            Log.e(CommonMethods.APP_TAG, e.getCause().getMessage());
//            valid = false;
//            createAndShowDialog("Cannot verify your email right, try again later", "Error");
//
//        }
        return valid;
    }

    private boolean registeredEmail(final String email)throws InterruptedException, ExecutionException {
        _userTable = _client.getTable(User.class);
        List<User> user = _userTable.where()
                            .field("email").eq(val(email)).select("email").execute().get();
        if(user.size() > 0){
            return false;
        }
        return true;

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
            ImageButton avatarButton = (ImageButton) findViewById(R.id.registration_btnAvatar);

            if (data != null) {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(), data.getData()
                    );
                   int i1 = photo.getByteCount();
                    Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 90, 90, false);
                   int i2 = scaledPhoto.getByteCount();
                    avatarButton.setImageBitmap(scaledPhoto);
                    bm = scaledPhoto;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearErrors() {
        mTextFirstName.setError(null);
        mTextLastName.setError(null);
        mTextPassword.setError(null);
        mTextEmail.setError(null);
        mTextRePassword.setError(null);

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (_savingProgressBar != null) _savingProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });
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
