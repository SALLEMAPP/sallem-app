package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.Friendship;

/**
 * Created by abdullahbamusa on 4/1/17.
 */

public class UpdateFriendRequestAsync extends AsyncTask {

    private final Friendship mFriendship;
    private final Context mContext;

    public UpdateFriendRequestAsync(Context context, Friendship friendship){
        mContext = context;
       mFriendship = friendship;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try{
            MobileServiceClient client = MyHelper.getAzureClient(mContext);
            MobileServiceTable<Friendship> friendsTable =
                    client.getTable(Friendship.class);
            friendsTable.update(mFriendship).get();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;


    }
}
