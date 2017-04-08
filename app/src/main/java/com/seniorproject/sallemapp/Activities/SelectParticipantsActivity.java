package com.seniorproject.sallemapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.seniorproject.sallemapp.Activities.listsadpaters.SelectUsersListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.helpers.EntityAsyncResult;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.seniorproject.sallemapp.helpers.LoadFriendsAsync;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.PassObject;

import java.util.ArrayList;
import java.util.List;

public class SelectParticipantsActivity extends AppCompatActivity implements PassObject<DomainUser>,
    ListAsyncResult<DomainUser> {
    ArrayList<DomainUser> mSelectedParticipants;
    ArrayList<DomainUser> mItems;
    SelectUsersListAdapter mAdapter;
    ListView mUsersListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_participants);
        mUsersListView = (ListView) findViewById(R.id.activityPartici_usersList);
        mSelectedParticipants = new ArrayList<>();
        attachOkButton();
        loadFriends();

    }

    private void loadFriends() {
        LoadFriendsAsync friendsAsync = new LoadFriendsAsync(this, DomainUser.CURRENT_USER.getId());
        friendsAsync.delegat = this;
        friendsAsync.execute();


    }

    private void attachOkButton() {
        Button okButton = (Button) findViewById(R.id.activityPartici_btnOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });
    }

    private void returnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected", mSelectedParticipants);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onPassed(DomainUser obj) {
        if(obj.getIsSelected() == true){
            if(mSelectedParticipants.contains(obj)){
                return;
            }
            else {
                mSelectedParticipants.add(obj);
            }
        }
        else{
            if(mSelectedParticipants.contains(obj)){
                mSelectedParticipants.remove(obj);
            }
        }


    }

    @Override
    public void processFinish(List<DomainUser> result) {
       if(result != null){
           mItems = (ArrayList<DomainUser>) result;
           mAdapter = new SelectUsersListAdapter(this, mItems, this);
           mUsersListView.setAdapter(mAdapter);

       }

    }
}
