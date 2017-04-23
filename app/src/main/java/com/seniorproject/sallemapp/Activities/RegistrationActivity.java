package com.seniorproject.sallemapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.EncryptionHelper;
import com.seniorproject.sallemapp.helpers.MyHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class RegistrationActivity extends AppCompatActivity {

    private  MobileServiceClient _client;
    MobileServiceTable<User> _userTable;
    ProgressBar _savingProgressBar;
    Button mRegistrationButton;


    Bitmap bm;
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
        mRegistrationButton = (Button)findViewById(R.id.Btn_resgisteration);
        mRegistrationButton.setEnabled(false);
        mRegistrationButton.setAlpha(0.5f);
        attachRegisterButton();
        attachOpenAvatar();
        attachAgreeCheckbox();


        try {
            _client = MyHelper.getAzureClient(this);
        }
        catch (MalformedURLException e){
            Log.d("SALLEMAPP", e.getCause().getMessage());
        }

    }

    private void attachAgreeCheckbox() {
        CheckBox cb = (CheckBox)findViewById(R.id.registeration_cbAgree);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRegistrationButton.setEnabled(true);
                    mRegistrationButton.setAlpha(1.0f);

                }
                else{
                    mRegistrationButton.setEnabled(false);
                    mRegistrationButton.setAlpha(0.5f);

                }
            }
        });

    }


    private void attachRegisterButton() {
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _savingProgressBar.setVisibility(View.VISIBLE);
                mRegistrationButton.setEnabled(false);
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
            String joinedAt = MyHelper.getCurrentDateTime();
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(encrypt(password));
            user.setEmail(email);
            user.setJoinedAt(joinedAt);
            if(bm == null){
                user.setImageTitle(MyHelper.DEFAULT_AVATAR_TITLE);
            }
            else {
                user.setImageTitle(MyHelper.ImageAsString(bm));
            }
            user.setStatus(0);

            addUserToDb(user);
        }

    }
    private String encrypt(String password){
        String encryptedPassword = null;
        try{
           encryptedPassword = EncryptionHelper.Encrypt(password);
        }
        catch (Exception e){
            Log.e("encrypt", "encrypt: " + e.getMessage() );

        }
       return encryptedPassword;
    }
    private String decrypt(String encrypted){
        String decrypted = null;
        try{
            decrypted = EncryptionHelper.decrypt(encrypted);
        }
        catch (Exception e){
            Log.e("encrypt", "encrypt: " + e.getMessage() );

        }
        return decrypted;
    }

    private void addUserToDb(final User user) {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private boolean isEmailRegistered = false;
            @Override
            protected Void doInBackground(Void... params) {
               try {
                   _userTable = _client.getTable(User.class);
                   List<User> emails = _userTable.where()
                           .field("email").eq(val(user.getEmail())).select("email").execute().get();
                   if(emails != null && emails.size() != 0){
                       isEmailRegistered = true;
                       return null;
                   }
                   _userTable.insert(user).get();
               }
               catch (Exception e){
                   createAndShowDialogFromTask(e, "Error");
               }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(isEmailRegistered){
                    mTextEmail.setError("Email already used");
                    _savingProgressBar.setVisibility(View.GONE);
                    mRegistrationButton.setEnabled(true);

                }
                else{
                    _savingProgressBar.setVisibility(View.GONE);
                    mRegistrationButton.setEnabled(true);
                    Intent signinIntent = new Intent(RegistrationActivity.this.getApplicationContext(), SignInActivity.class);
                    startActivity(signinIntent);
                    finish();
                }

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

        return valid;
    }

    private boolean registeredEmail(final String email) {
        boolean isRegistered = true;
        try {
            _userTable = _client.getTable(User.class);
            List<User> user = _userTable.where()
                    .field("email").eq(val(email)).select("email").execute().get();
            if (user.size() == 0) {
                isRegistered =  false;
            }
        }
        catch ( Exception e ){
            Log.e("Registration Activity", "registeredEmail: "+ e.getMessage() );
        }
        return isRegistered;
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
