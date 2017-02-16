package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.seniorproject.sallemapp.R;

public class ResetPasswordActivity extends AppCompatActivity {
    public Button Btn_Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

       Btn_Reset =(Button)findViewById(R.id.Btn_Reset);
        Btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn =new Intent(ResetPasswordActivity.this,SignInActivity.class);
                 startActivity(signIn);
            }
        });

    }}

