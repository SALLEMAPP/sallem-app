package com.seniorproject.sallemapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        TextView termsTextView = (TextView)findViewById(R.id.lbl_term);
        termsTextView.setMovementMethod(new ScrollingMovementMethod());
    }
}
