package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.seniorproject.sallemapp.R;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        attachAddLocationButton();
    }

    private void attachAddLocationButton() {
        ImageButton b = (ImageButton) findViewById(R.id.addEvent_btn_eventLocation);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddEventActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });


    }
}
