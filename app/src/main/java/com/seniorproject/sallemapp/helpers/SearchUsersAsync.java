package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 06-Apr-2017.
 */

public class SearchUsersAsync extends AsyncTask<Void,Void,List<DomainUser>> {
    private final String mEmail;
    private Context mContext;
    private ListAsyncResult<DomainUser> mCallback;
    public SearchUsersAsync(String email, Context context, ListAsyncResult<DomainUser> callback ){
        mEmail = email;
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected List<DomainUser> doInBackground(Void... params) {
        List<DomainUser> resultUsers = new ArrayList<>();
        try {
            MobileServiceClient client =  MyHelper.getAzureClient(mContext);
            MobileServiceTable<User> userTable = client.getTable(User.class);
            List<User> users = userTable.where().startsWith("email", mEmail).execute().get();
            if(users.size() > 0){
                for(User user :users) {
                    Bitmap avatar = null;
                    String imageTitle = user.getImageTitle();
                    if(!imageTitle.equals(MyHelper.DEFAULT_AVATAR_TITLE)) {
                        avatar = MyHelper.decodeImage(imageTitle);
                    }
                    else{
                        avatar =  MyHelper.getDefaultAvatar(mContext);
                    }
                    DomainUser domainUser = new DomainUser(
                            user.getId(), user.getFirstName(), user.getLastName(),
                            user.getPassword(), user.getEmail(), user.getJoinedAt(),
                            user.getImageTitle(), user.getStatus(),
                            avatar, 0, 0, false);
                    resultUsers.add(domainUser)    ;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("SALLEM APP", e.getCause().getMessage());
        }
        return resultUsers;
    }

    @Override
    protected void onPostExecute(List<DomainUser> result) {
            mCallback.processFinish(result);
    }
}
