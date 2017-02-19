package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.seniorproject.sallemapp.R;
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        attachSigninButton();
        attachRegisterButton();

    }

    private void attachRegisterButton() {
        Button registerButton = (Button)findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void attachSigninButton() {
        Button signinButton = (Button)findViewById(R.id.btn_signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(signinIntent);
            }
        });
    }
}
