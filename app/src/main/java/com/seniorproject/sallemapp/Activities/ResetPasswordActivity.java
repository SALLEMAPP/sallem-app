package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seniorproject.sallemapp.R;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        attachResetButton();

    }

    private void attachResetButton() {
        Button v = (Button)findViewById(R.id.Btn_Reset);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                startActivity(signinIntent);
            }
        });
    }
}
