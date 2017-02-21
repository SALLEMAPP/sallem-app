package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.User;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        attachSigninButton();
        attachResetButton();
    }



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
    /*private void attachResetButton() {
        Button resetButton = (Button)findViewById(R.id.btn_reset_link);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetIntent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(resetIntent);
            }
        });

    }*/

    private void attachSigninButton() {
        Button signinButton = (Button)findViewById(R.id.Btn_Sign_in);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User current = new User();
                current.setFirstName("Abdullah");
                current.setLastName("BaMusa");
                String uri = "drawable/icon";
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable avatar = getResources().getDrawable(imageResource);

                //current.setAvatar(avatar.);
                //set current to be the global user.
                User.CURRENT_USER = current;

                Intent signinIntent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(signinIntent);
            }
        });
    }


}
