package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        attachSigninButton();
        attachResetButton();
    }

    private void attachResetButton() {
        Button resetButton = (Button)findViewById(R.id.btn_reset_link);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetIntent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(resetIntent);
            }
        });

    }

    private void attachSigninButton() {
        Button signinButton = (Button)findViewById(R.id.Btn_Sign_in);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(signinIntent);
            }
        });
    }


}
