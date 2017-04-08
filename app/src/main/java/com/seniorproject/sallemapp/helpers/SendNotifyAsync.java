package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.Notify;

import java.util.List;
import java.util.UUID;

/**
 * Created by abdul on 06-Apr-2017.
 */

public class SendNotifyAsync extends AsyncTask<Void,Void, Void> {
    private List<Notify> mNotifies;
    private Context mContext;
    public SendNotifyAsync(List<Notify> notifies, Context context){
     mNotifies = notifies;
        mContext = context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            MobileServiceClient client =  MyHelper.getAzureClient(mContext);
            MobileServiceTable<Notify> notifyTable = client.getTable(Notify.class);
            for(Notify notify: mNotifies){
               notifyTable.insert(notify).get();
            }
            
        }
        catch (Exception e){
            Log.e("Send Notify Async", "doInBackground: "+ e.getMessage() );
        }
        return null;
    }
}
