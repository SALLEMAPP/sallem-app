package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 03-Apr-2017.
 */

public class LoadFriendsAsync extends AsyncTask<Void, Void, List<DomainUser>> {
    public ListAsyncResult<DomainUser> delegat;
    private Context mContext;
    private String mUserId;
    public LoadFriendsAsync(Context context, String userId ){
        mContext = context;
        mUserId = userId;
    }

    @Override
    protected List<DomainUser> doInBackground(Void... params) {
        ArrayList<DomainUser> result = new ArrayList<>();
        try {
            MobileServiceClient client =  MyHelper.getAzureClient(mContext);
            MobileServiceTable<Friendship> friendsTable = client.getTable(Friendship.class);
            MobileServiceTable<User> usersTable = client.getTable(User.class);
            List<Friendship> userFriends = friendsTable.where()
                    .field("id").eq(mUserId)
                    .and().field("statusId").eq(2)
                    .execute().get();
            if(userFriends != null && userFriends.size() > 0){
                for(Friendship friend: userFriends){
                    User user = usersTable.where()
                            .field("id").eq(friend.getFriendId()).execute().get().get(0);
                    Bitmap avatar = null;
                    String imageTitle = user.getImageTitle();
                    if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
                        avatar = MyHelper.decodeImage(imageTitle);
                    }
                    else{
                        avatar =  MyHelper.getDefaultAvatar(mContext);
                    }
                    DomainUser domainUser = new DomainUser(
                            user.getId(),user.getFirstName(), user.getLastName(),
                            user.getPassword(), user.getEmail(),
                            user.getJoinedAt(), user.getImageTitle(),
                            user.getStatus(), avatar,0,0,false);
                    result.add(domainUser);

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("SALLEM APP", "doInBackground: " + e.getCause().getMessage() );
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<DomainUser> domainUsers) {
        delegat.processFinish(domainUsers);
    }
}
