package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.Notify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 07-Apr-2017.
 */

public class LoadNotifiesAsync extends AsyncTask<Void, Void, List<Notify>> {
    private String mUserId;
    private Context mContext;
    private ListAsyncResult<Notify> mCallback;
    public LoadNotifiesAsync(String userId, Context context, ListAsyncResult<Notify> callback){
        mUserId = userId;
        mContext = context;
        mCallback = callback;
    }
    @Override
    protected List<Notify> doInBackground(Void... params) {
        try {
            MobileServiceClient client =  MyHelper.getAzureClient(mContext);
            MobileServiceTable<Notify> notifyTable = client.getTable(Notify.class);
            List<Notify> notifies = notifyTable.where().field("destUser").eq(mUserId)
                    .add().field("delivered").eq(false)
                    .execute().get();
            if(notifies != null && notifies.size() > 0){
                for(Notify n: notifies){
                    n.setDelivered(true);
                    notifyTable.update(n).get();
                }
            }
            return notifies;

        }
        catch (Exception e){
            Log.e("Load Notifies Async", "doInBackground: " + e.getMessage() );
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<Notify> notifies) {
        if(mCallback != null){
            mCallback.processFinish(notifies);
        }
    }
}
