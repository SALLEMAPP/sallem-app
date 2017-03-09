package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Centeral on 2/21/2017.
 */

public class UserLocation {
    @SerializedName("id")
    private String _id;
    @SerializedName("userId")
    private String _userId;
    @SerializedName("longitude")
    private double _longitude;
    @SerializedName("latitude")
    private double _latitude;
    @SerializedName("seenAt")
    private String _seenAt;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        _longitude = longitude;
    }

    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        _latitude = latitude;
    }

    public String getSeenAt() {
        return _seenAt;
    }

    public void setSeenAt(String seenHereAt) {
        _seenAt = seenHereAt;
    }
    public UserLocation(){

    }
}
