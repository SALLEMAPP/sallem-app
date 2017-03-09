package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.User;
import com.squareup.okhttp.OkHttpClient;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.*;


import com.seniorproject.sallemapp.R;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    MobileServiceClient _client;
    MobileServiceTable<User> _userTable;
    ProgressBar _savingProgressBar;
    UserLocationService mService;
    boolean mBound = false;
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
            _userTable = _client.getTable(User.class);

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
                //findUser(email);
                openHomeActivity();

            }
        });

    }
    private void  findUser(final String email) throws IllegalStateException {
//        java.util.List<android.util.Pair<java.lang.String,java.lang.String>> parameters = new ArrayList<>();
//        parameters.add(new android.util.Pair<String, String>("email", email));
//
//        AsyncTask<Void, Void,Void> task = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                //com.google.common.util.concurrent.ListenableFuture<User> future =
//                _client.invokeApi("getUserByEmail", "Get", parameters, User.class, new ApiOperationCallback<User>() {
//                    @Override
//                    public void onCompleted(User result, Exception exception, ServiceFilterResponse response) {
//                        String s = result.getEmail();
//                    }
//                });
//
//
//                return null;
//            }
//        };
        if (email != null || email != "") {
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                   try {
                       final List<User> users = getUserByEmail(email);
                       if (users.size() > 1) {
                           throw new IllegalStateException("There should not be more that user with the same eamil");
                       }
                       for(User user: users){
                           if(user.getEmail().equals(email)){
                               SharedPreferences sallemPref = getSharedPreferences("SALLEMAPP", MODE_PRIVATE);
                               sallemPref.edit().putString("email", email).commit();
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       openHomeActivity();
                                   }
                               });
                               break;
                           }

                       }

                   }
                   catch( ExecutionException ex){
                       Log.d("ex", ex.getCause().getMessage());
                   }
                   catch (InterruptedException ex){
                       Log.d("ex", ex.getCause().getMessage());
                   }
                   catch (Exception e){
                       Log.d("ex", e.getCause().getMessage());
                   }

                    return null;
                }
            };
            task.execute();
        }
    }
    private void openHomeActivity(){
        Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(homeIntent);
    }

        private List<User> getUserByEmail(String email) throws ExecutionException, InterruptedException{

            return _userTable.where().field("email").
                    eq(val(email)).execute().get();


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
//                    Intent signinIntent = new Intent(RegistrationActivity.this, SignInActivity.class);
//                    startActivity(signinIntent);
//                    finish();
                }
            });

            return resultFuture;
        }
    }


}
