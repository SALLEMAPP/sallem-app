package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainUser {

    private User mDbUser;
    public DomainUser(User dbUser){
        mDbUser = dbUser;
    }

    private Bitmap mAvatar;
    public String getId(){
        return mDbUser.getId();
    }
    public String getFirstName(){
        return mDbUser.getFirstName();
    }
    public String getLasttName(){
        return mDbUser.getLastName();
    }
    public String getEmail(){
        return mDbUser.getEmail();
    }
    public String getJoinedAt(){
        return mDbUser.getJoinedAt();
    }
    public String getPassowrd(){
        return mDbUser.getPassword();
    }
    public String getImageTitle(){
        return mDbUser.getImageTitle();
    }
    public int getStatus(){
        return mDbUser.getStatus();
    }
    public Bitmap getAvatar(){
        return mAvatar;
    }
    public void setAvatar(Bitmap avatar){
        mAvatar = avatar;
    }



    public static DomainUser CURRENT_USER;


}
