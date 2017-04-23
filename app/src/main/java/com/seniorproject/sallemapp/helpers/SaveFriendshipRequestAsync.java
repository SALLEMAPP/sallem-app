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
import com.seniorproject.sallemapp.entities.Notify;

import java.util.ArrayList;
import java.util.List;
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
            MobileServiceTable<Friendship> userTable = client.getTable(Friendship.class);
            userTable.insert(firstFriendship).get();
       }
         catch (Exception e){
             e.printStackTrace();
             Log.e("SALLEM AA", "doInBackground: " + e.getCause());
         }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Notify notify = createNotify();
        List<Notify> notifies = new ArrayList<>();
        notifies.add(notify);
        sendNotify(notifies);
        String msg = "Your request has sent";
        MyHelper.showToast(mContext, msg);

    }
    private void sendNotify(List<Notify> notifies){
        SendNotifyAsync sendNotify = new SendNotifyAsync(notifies, mContext);
        sendNotify.execute();
    }
    private Notify createNotify(){

        Notify notify = new Notify();
        notify.setId(UUID.randomUUID().toString());
        notify.setSourceUser(mUserId);
        notify.setDestUser(mFriendId);
        notify.setTitle(DomainUser.CURRENT_USER.getFirstName() + " " + DomainUser.CURRENT_USER.getLasttName());
        notify.setSubject("Sent you friendship request");
        notify.setDelivered(false);
        notify.setPublishedAt(MyHelper.getCurrentDateTime());
        return notify;
    }

    private Friendship creatFirstFriendship(){
        Friendship firstFriendship = new Friendship();
        firstFriendship.setId(mUserId);
        firstFriendship.setFriendId(mFriendId);
        firstFriendship.setFriendsSince(MyHelper.getCurrentDateTime());
        firstFriendship.setStatusId(1);
        return firstFriendship;
    }

}
