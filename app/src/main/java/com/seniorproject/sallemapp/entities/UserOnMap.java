package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

/**
 * Created by abdul on 02-Apr-2017.
 */

public class UserOnMap {
    private String userId;
    private String userName;
    private double longitude;
    private double latitude;
    private Bitmap avatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Bitmap getAvatar() {
        Bitmap scaledUp = Bitmap.createScaledBitmap(avatar, 185,185,false);
        return scaledUp;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
