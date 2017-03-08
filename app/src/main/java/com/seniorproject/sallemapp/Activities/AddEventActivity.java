package com.seniorproject.sallemapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.seniorproject.sallemapp.R;

import org.joda.time.LocalDate;

public class AddEventActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 8000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        attachAddLocationButton();
        attachSelectEventDate();
        attachSelectEventTime();
    }

    private void attachSelectEventTime() {
        final EditText eventTime = (EditText)findViewById(R.id.addEvent_txt_eventTime);
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int hour =  cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                boolean is24Hhoure = true;
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                //cal.add(Calendar.HOUR, hour);
                                //cal.add(Calendar.MINUTE, minute);
                                String time = hour + ":" + minute;
                                eventTime.setText(time);
                            }
                        }, hour, minute, is24Hhoure );
                timePickerDialog.setTitle("Select event time");
                timePickerDialog.show();
            }
        });
    }


    private void attachSelectEventDate() {
        final EditText eventDate = (EditText)findViewById(R.id.addEvent_txt_eventDate);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(AddEventActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        cal.set(year, month, day);
                                        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                        String date = formater.format(cal.getTime());
                                        eventDate.setText(date);
                                    }
                                }, year, month,day);
                datePickerDialog.setTitle("Select event date");
                datePickerDialog.show();

            }
        }

        );



    }

    private void attachAddLocationButton() {
        ImageButton b = (ImageButton) findViewById(R.id.addEvent_btn_eventLocation);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddEventActivity.this, MapsActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                TextView location = (TextView) findViewById(R.id.addEvent_lbl_eventLocation);
                String selected = data.getStringExtra("result");
                location.setText(selected);
            }
        }

    }
}
