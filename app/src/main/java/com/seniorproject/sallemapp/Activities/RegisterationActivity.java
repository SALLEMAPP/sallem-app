package com.seniorproject.sallemapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;

public class RegisterationActivity extends AppCompatActivity {
      TextView lbl_terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        lbl_terms = (TextView)findViewById(R.id.lbl_term);
        lbl_terms.setMovementMethod(new ScrollingMovementMethod());

    }
}
