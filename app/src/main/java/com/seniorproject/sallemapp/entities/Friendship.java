package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class Friendship {
    @SerializedName("id")
    private String id;
    @SerializedName("FriendId")
    private String FriendId;
    @SerializedName("FriendsSince")
    private String FriendsSince;
    @SerializedName("StatusId")
    private int StatusId;
    public Friendship(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getFriendId() {
        return FriendId;
    }

    public void setFriendId(String friendId) {
        FriendId = friendId;
    }

    public String getFriendsSince() {
        return FriendsSince;
    }

    public void setFriendsSince(String friendsSince) {
        FriendsSince = friendsSince;
    }

    /**
     * Status is one of these values
     * 1 for Pending status
     * 2 for Accept status
     * 3 for Decline status
     * @return An int represent the current status
     */
    public int getStatusId() {
        return StatusId;
    }

    /**
     * * Status is one of these values
     * 1 for Pending status
     * 2 for Accept status
     * 3 for Decline status
     * @param statusId An int represent the current status
     */
    public void setStatusId(int statusId) {
        StatusId = statusId;
    }
}
