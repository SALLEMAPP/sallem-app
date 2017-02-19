package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        TextView termsTextView = (TextView)findViewById(R.id.lbl_term);
        termsTextView.setMovementMethod(new ScrollingMovementMethod());
        attachRegisterButton();
    }

    private void attachRegisterButton() {
        Button signinButton = (Button)findViewById(R.id.Btn_resgisteration);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(RegistrationActivity.this, SignInActivity.class);
                startActivity(signinIntent);
            }
        });

    }

}
