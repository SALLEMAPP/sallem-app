package com.seniorproject.sallemapp.helpers;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.User;

import org.joda.time.LocalDateTime;

import java.util.UUID;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class SaveFriendshipRequestAsync extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private String mUserId;
    private String mFriendId;
    public SaveFriendshipRequestAsync(Context context, String userId, String friendId ){
        mContext = context;
        mUserId = userId;
        mFriendId = friendId;

    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            MobileServiceClient client = MyHelper.getAzureClient(mContext);
            Friendship firstFriendship = creatFirstFriendship();
            //Friendship secondFriendship = creatSecondFriendship();
            MobileServiceTable<Friendship> userTable = client.getTable(Friendship.class);
            userTable.insert(firstFriendship).get();
            //userTable.insert(secondFriendship);
       }
         catch (Exception e){
             e.printStackTrace();
             Log.e("SALLEM AA", "doInBackground: " + e.getCause());
         }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        String msg = "Your request has sent";
        MyHelper.showToast(mContext, msg);

    }

    private Friendship creatFirstFriendship(){
        Friendship firstFriendship = new Friendship();
        firstFriendship.setId(mUserId);
        firstFriendship.setFriendId(mFriendId);
        firstFriendship.setFriendsSince(new LocalDateTime().toString());
        firstFriendship.setStatusId(1);
        return firstFriendship;
    }

}
