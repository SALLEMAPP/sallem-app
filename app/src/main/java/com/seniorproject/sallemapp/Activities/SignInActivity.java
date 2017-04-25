package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.localdb.UserDataSource;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.CommonMethods;
import com.seniorproject.sallemapp.helpers.AzureBlob;
import com.seniorproject.sallemapp.helpers.EncryptionHelper;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
import com.seniorproject.sallemapp.helpers.MyHelper;


import com.seniorproject.sallemapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignInActivity extends AppCompatActivity implements EntityAsyncResult<DomainUser> {

    ProgressBar _savingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        _savingProgressBar = (ProgressBar)findViewById(R.id.singin_progress_bar);
        _savingProgressBar.setVisibility(ProgressBar.GONE);
        attachSigninButton();
    }

    private void attachSigninButton() {
        Button signinButton = (Button) findViewById(R.id.Btn_Sign_in);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   String email = ((EditText)findViewById(R.id.sign_in_txt_user_name)).getText().toString();
                   String password = ((EditText)findViewById(R.id.txt_password)).getText().toString();
                   String encryptedPassword = EncryptionHelper.Encrypt(password);

                   LoadUserAsync loadUserAsync = new LoadUserAsync(SignInActivity.this, email, encryptedPassword, SignInActivity.this);
                   loadUserAsync.execute();
                   _savingProgressBar.setVisibility(View.VISIBLE);

               }
                catch (Exception e){
                    Log.e("Sign in", "onClick: " + e.getMessage());
                }
            }
        });

    }

    private void openHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeIntent);
        finish();

    }



    @Override
    public void processFinish(DomainUser result) {
        _savingProgressBar.setVisibility(View.GONE);
        if(result == null){
            Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences shared = getSharedPreferences(MyHelper.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String userId = shared.getString("userid", null);
        if(userId == null){
            shared.edit().putString("userid", result.getId()).apply();
        }
        DomainUser.CURRENT_USER = result;
        openHomeActivity();


    }
    private class LoadUserAsync extends AsyncTask<Void, Void, DomainUser>{
        private String mEmail;
        private String mPassword;

        private EntityAsyncResult<DomainUser> mCallback;
        private Context mContext;

    public LoadUserAsync(Context context, String email, String passowrd, EntityAsyncResult<DomainUser> callback){
            mEmail = email;
            mPassword = passowrd;
            mContext = context;
            mCallback =callback;
        }

        @Override
        protected DomainUser doInBackground(Void... params) {
            DomainUser domainUser = null;
            try {
                List<User> users = getUserByEmail(mEmail, mPassword);
                if(users.size() == 1){
                    User user = users.get(0);
                    String imageTitle = user.getImageTitle();
                    Bitmap avatar = null;
                    if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
                        avatar = MyHelper.decodeImage(imageTitle);
                    }
                    else{
                        avatar =  MyHelper.getDefaultAvatar(getApplicationContext());
                    }
                    domainUser = new DomainUser(
                            user.getId(), user.getFirstName(), user.getLastName(),
                            user.getPassword(), user.getEmail(), user.getJoinedAt(),
                            user.getImageTitle(), user.getStatus(),
                            avatar, 0, 0, false
                    );
                    UserDataSource dc = new UserDataSource(mContext);
                    dc.open();
                    DomainUser localUser = dc.getUser(domainUser.getId());
                    if(localUser ==null) {
                        dc.insert(user, domainUser.getAvatar());
                    }
                    dc.close();
                }
            }
            catch (ExecutionException e){
                Log.d(CommonMethods.APP_TAG, e.getCause().getMessage());
            }
            catch (InterruptedException e){
                Log.d(CommonMethods.APP_TAG, e.getCause().getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return domainUser;
        }
        private List<User> getUserByEmail(String email, String password) throws ExecutionException, InterruptedException, MalformedURLException{
            MobileServiceClient client = MyHelper.getAzureClient(mContext);
            MobileServiceTable<User> userTable = client.getTable(User.class);
            return userTable.where().field("email").eq(email)
                    .and()
                    .field("password").eq(password)
                    .execute().get();
        }
        @Override
        protected void onPostExecute(DomainUser user) {
            if(mCallback != null) {
                mCallback.processFinish(user);
            }
        }
    }
}
