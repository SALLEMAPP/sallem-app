package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.DownloadImage;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
import com.squareup.okhttp.OkHttpClient;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.*;


import com.seniorproject.sallemapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity implements EntityAsyncResult<DomainUser> {

    MobileServiceClient _client;
    MobileServiceTable<User> _userTable;
    ProgressBar _savingProgressBar;
    UserLocationService mService;
    boolean mBound = false;
    User mCurrentUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        _savingProgressBar = (ProgressBar)findViewById(R.id.singin_progress_bar);
        _savingProgressBar.setVisibility(ProgressBar.GONE);
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
        attachResetButton();
        attachSigninButton();


    }




    private void attachSigninButton() {
        Button signinButton = (Button) findViewById(R.id.Btn_Sign_in);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ((EditText)findViewById(R.id.sign_in_txt_user_name)).getText().toString();
                String password = ((EditText)findViewById(R.id.txt_password)).getText().toString();

                LoadUserAsync loadUserAsync = new LoadUserAsync(email, password);
                loadUserAsync.delegate = SignInActivity.this;
                loadUserAsync.execute();


            }
        });

    }

    private void openHomeActivity(){
        Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();

    }


//    private void fetchUserName(){
//        // Create a new item
//    if(mToDoTable == null){return;}
//
//        // Insert the new item
//        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    final User entity = mToDoTable.where().execute().get().get(0);
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            userNameText.setText(entity.getEmail());
//                            //userNameText.postInvalidate();
//
//                        }
//                    });
//                } catch (final InterruptedException e) {
//                    createAndShowDialogFromTask(e, "Error");
//                }
//                catch (final ExecutionException e) {
//
//                    createAndShowDialogFromTask(e, "Error");
//
//                }
//                return null;
//            }
//        };
//
//        runAsyncTask(task);
//
//
//    }


    private void attachResetButton() {

        TextView forget = (TextView)findViewById(R.id.lbl_forgot_password);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //frgt is the name of intent that will be used to link between sign in activity and reset password activity

                Intent frgt = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(frgt);
            }
        });
    }

    @Override
    public void processFinish(DomainUser result) {
        if(result == null){
            Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
            return;
        }
        DomainUser.CURRENT_USER = result;
        openHomeActivity();


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


                }
            });

            return resultFuture;
        }
    }

    private class LoadUserAsync extends AsyncTask<Void, Void, DomainUser>{
        private String mEmail;
        private String mPassword;
        public EntityAsyncResult<DomainUser> delegate;

        public LoadUserAsync(String email, String passowrd){
            mEmail = email;
            mPassword = passowrd;
        }

        @Override
        protected DomainUser doInBackground(Void... params) {
            DomainUser domainUser = null;
            try {
                List<User> users = getUserByEmail(mEmail, mPassword);
                if(users.size() == 1){
                    User user = users.get(0);
                    Bitmap avatar = DownloadImage.getImage(SignInActivity.this, user.getImageTitle()+".jpg");
                    domainUser = new DomainUser(user);
                    domainUser.setAvatar(avatar);
                }

            }
            catch (ExecutionException e){
                Log.d(CommonMethods.APP_TAG, e.getCause().getMessage());
            }
            catch (InterruptedException e){
                Log.d(CommonMethods.APP_TAG, e.getCause().getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (StorageException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }


            return domainUser;
        }
        private List<User> getUserByEmail(String email, String password) throws ExecutionException, InterruptedException{
            _userTable = _client.getTable(User.class);
            return _userTable.where().field("email").eq(email)
                    .and()
                    .field("password").eq(password)
                    .execute().get();
        }
        @Override
        protected void onPostExecute(DomainUser user) {
            if(delegate != null) {
                delegate.processFinish(user);
            }
        }


    }


}
