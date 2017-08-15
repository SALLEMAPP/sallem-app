package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.Notify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class SaveFriendshipRequestAsync extends AsyncTask<Void, Void, BackgroundOperationResult> {
    private Context mContext;
    private String mUserId;
    private String mFriendId;
    public SaveFriendshipRequestAsync(Context context, String userId, String friendId ){
        mContext = context;
        mUserId = userId;
        mFriendId = friendId;

    }

    @Override
    protected BackgroundOperationResult doInBackground(Void... params) {
        boolean succeed = false;
        try{
            MobileServiceClient client = MyHelper.getAzureClient(mContext);
            Friendship firstFriendship = creatFirstFriendship();
            MobileServiceTable<Friendship> userTable = client.getTable(Friendship.class);
            userTable.insert(firstFriendship).get();
            succeed = true;
       }
         catch (Exception e){
             e.printStackTrace();
             Log.e("SALLEM AA", "doInBackground: " + e.getCause());
             succeed = false;
         }
        return new BackgroundOperationResult(succeed);
    }

    @Override
    protected void onPostExecute(BackgroundOperationResult result) {
        if(result.getSucceed()){
            Notify notify = createNotify();
            List<Notify> notifies = new ArrayList<>();
            notifies.add(notify);
            sendNotify(notifies);
            String msg = String.valueOf(R.string.FriendshipRequestSent_msg);   //TODO check it in Friendship request simulation
            MyHelper.showToast(mContext, msg);
        }
        else {
            String msg = mContext.getResources().getString(R.string.FriendshipRequestAlreadySent_msg);    //TODO check it in Friendship request simulation
            MyHelper.showToast(mContext, msg);

        }


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
        notify.setSubject(mContext.getString(R.string.FriendshipRequest_msgReceived));
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
