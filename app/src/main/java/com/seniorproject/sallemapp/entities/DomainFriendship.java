package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdullahbamusa on 4/1/17.
 */

public class DomainFriendship {
    private String id;
    private String FriendId;
    private String FriendsSince;
    private int StatusId;
    private Friendship mDbFriendship;
    private DomainUser mDomainUser;
    public DomainFriendship(Friendship dbFriendship, DomainUser domainUser){
        mDbFriendship = dbFriendship;
        mDomainUser = domainUser;


    }

    public String getId() {
        return mDbFriendship.getId();
    }

    public String getFriendId() {
        return mDbFriendship.getFriendId();
    }

    public String getFriendsSince() {
        return mDbFriendship.getFriendsSince();
    }

    public int getStatusId() {
        return mDbFriendship.getStatusId();
    }
    public DomainUser getmDomainUser(){
        return mDomainUser;
    }
}
