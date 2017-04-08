package com.seniorproject.sallemapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.Activities.listsadpaters.FriendsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.Activity;
import com.seniorproject.sallemapp.entities.ActivityDetail;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AddEventActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 8000;
    private static final int PARTICIPANTS_REQUEST_CODE = 9000;
    private ListView mParticipantsList;
    private LatLng mSelectedLocation;
    private EditText mSubjectEditText;
    private String selectedDate;
    private String selectedTime;
    private ArrayList<ActivityDetail> mActitityDetails;
    private ArrayList<DomainUser> mParticipants;
    MobileServiceClient mClient;
    private MyApplication mMyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        mParticipantsList = (ListView)findViewById(R.id.addEvent_paticiList);
        mSubjectEditText = (EditText) findViewById(R.id.addEvent_txtEventSubject);
        mActitityDetails = new ArrayList<>();
        attachAddLocationButton();
        attachSelectEventDate();
        attachSelectEventTime();
        attachAddPariticipants();
        attachSaveButton();
        try {
            mClient = MyHelper.getAzureClient(this);

        }
        catch (Exception e){
            Log.e("Add Event Activity", "onCreate: "+e.getMessage() );
        }
    }

    private void attachSaveButton() {
        Button button = (Button)findViewById(R.id.addEvent_btnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });

    }

    private void saveActivity() {
        String subject = mSubjectEditText.getText().toString();
        if(isValid()){
            Activity activity = new Activity();
            activity.setId(UUID.randomUUID().toString());
            activity.setOrganizerId(DomainUser.CURRENT_USER.getId());
            activity.setSubject(subject);
            DateTimeFormatter formatter = DateTimeFormat.forPattern ("dd/MM/yyyy HH:mm:ss");
            DateTime onDate = formatter.parseDateTime(selectedDate + " "+ selectedTime);

            String onDateString = onDate.toLocalDateTime().toString();
            String joinedAt = new LocalDateTime().toString();
            activity.setHelodOn(onDateString);

            activity.setLongitude(mSelectedLocation.longitude);
            activity.setLatitude(mSelectedLocation.latitude);
            for(DomainUser paritipant:mParticipants){
                ActivityDetail detail = new ActivityDetail();
                detail.setId(UUID.randomUUID().toString());
                detail.setActivityId(activity.getId());
                detail.setParticipantId(paritipant.getId());
                detail.setParticipationStatus(1);
                mActitityDetails.add(detail);
            }
            SaveActivityAsyn saveAsync = new SaveActivityAsyn(this, activity);
            saveAsync.execute();

        }
    }
    private boolean isValid(){
        return true;
    }

    private void attachAddPariticipants() {
       ImageButton button = (ImageButton) findViewById(R.id.addEvent_btn_addParticipant);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddEventActivity.this, SelectParticipantsActivity.class);
                startActivityForResult(i, PARTICIPANTS_REQUEST_CODE);
            }
        });


    }

    private void attachSelectEventTime() {
        final EditText eventTime = (EditText)findViewById(R.id.addEvent_txtEventTime);
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int hour =  cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                boolean is24Hhoure = true;
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                //cal.add(Calendar.HOUR, hour);
                                //cal.add(Calendar.MINUTE, minute);
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
                                String s = hour + ":" + minute + ":" + "00";
                                DateTime time = formatter.parseDateTime(s);
                                // String time = hour + ":" + minute;
                                eventTime.setText(time.toLocalTime().toString("HH:mm"));
                                selectedTime = time.toLocalTime().toString("HH:mm:ss");
                            }
                        }, hour, minute,true );
                timePickerDialog.setTitle("Select event time");
                timePickerDialog.show();
            }
        });

    }


    private void attachSelectEventDate() {
        final EditText eventDate = (EditText)findViewById(R.id.addEvent_txtEventDate);
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
                                        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                                        String dateString = formater.format(cal.getTime());
                                        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
                                        //String s = hour + ":"+ minute + ":"+"00";
                                        DateTime date = formatter.parseDateTime(dateString);
                                        selectedDate = date.toString("dd/MM/yyyy");
                                        eventDate.setText(date.toString("dd/MM/yyyy"));

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
                TextView location = (TextView) findViewById(R.id.addEvent_lblEventLocation);
                mSelectedLocation = data.getParcelableExtra("result");
                String selected = mSelectedLocation.toString();
                location.setText(selected);
            }
        }
        if(requestCode == PARTICIPANTS_REQUEST_CODE){
            if(resultCode == RESULT_OK ){
                mParticipants = data.getParcelableArrayListExtra("selected");
                if(mParticipants != null){
                    FriendsListAdapter adapter = new FriendsListAdapter(this, mParticipants);
                    mParticipantsList.setAdapter(adapter);
                }
            }
        }

    }
    public class SaveActivityAsyn extends AsyncTask{
        private Activity mActivity;
        private Context mContext;
        public SaveActivityAsyn(Context context, Activity activtiy) {
            this.mActivity = activtiy;
            this.mContext = context;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try{
                //MobileServiceClient client = AzureHelper.CreateClient(this.mContext);
                MobileServiceTable<Activity> activityTable = mClient.getTable(Activity.class);
                    activityTable.insert(this.mActivity);
                }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
         SaveActivityDetailsAsync saveDetails = new SaveActivityDetailsAsync(mContext);
          saveDetails.execute();
        }
    }


    public class SaveActivityDetailsAsync extends AsyncTask{
        private Context mContext;
        public SaveActivityDetailsAsync(Context context){
            this.mContext = context;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try{
                //MobileServiceClient client = AzureHelper.CreateClient(this.mContext);
                MobileServiceTable<ActivityDetail> detailsTable = mClient.getTable(ActivityDetail.class);
                for(ActivityDetail detail:mActitityDetails){
                    detailsTable.insert(detail);
                }

            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage());

            }
            return null;
        }
    }
}
