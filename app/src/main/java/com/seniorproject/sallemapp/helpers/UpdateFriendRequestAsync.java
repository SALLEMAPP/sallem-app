package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.Friendship;

import org.joda.time.LocalDateTime;

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
            Friendship updatable = friendsTable.where().field("id").eq(mFriendship.getId())
                    .and().field("friendId").eq(mFriendship.getFriendId()).execute().get().get(0);
            if(updatable != null){
                updatable.setStatusId(mFriendship.getStatusId());
            }
            friendsTable.update(updatable).get();
            //When i response to frienship request,  create second second part of friendship by
            // swap the first friendship in that friendid in the first part will be id of the second part.

        }
        catch (Exception e){
            e.printStackTrace();

        }
        return null;


    }

    @Override
    protected void onPostExecute(Object o) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
               try {
                   MobileServiceClient client = MyHelper.getAzureClient(mContext);
                   MobileServiceTable<Friendship> friendsTable =
                           client.getTable(Friendship.class);
                   Friendship secondPart = creatSecondFriendship(mFriendship.getFriendId(), mFriendship.getId(), mFriendship.getStatusId());
                   friendsTable.insert(secondPart).get();
               }
               catch (Exception e){
                   e.printStackTrace();
               }

                return null;
            }
        };
        task.execute();
    }

    private Friendship creatSecondFriendship(String userId, String friendId, int status){
        Friendship secondFriendship = new Friendship();
        secondFriendship.setId(userId);
        secondFriendship.setFriendId(friendId);
        secondFriendship.setFriendsSince(new LocalDateTime().toString());
        secondFriendship.setStatusId(status);
        return secondFriendship;
    }
}
