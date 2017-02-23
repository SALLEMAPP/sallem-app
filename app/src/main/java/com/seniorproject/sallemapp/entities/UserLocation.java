package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class UserLocation {
    private int _id;
    private int _userId;
    private long _longitude;
    private long _latitude;
    private DateTime _seenHereAt;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        _userId = userId;
    }

    public long getLongitude() {
        return _longitude;
    }

    public void setLongitude(long longitude) {
        _longitude = longitude;
    }

    public long getLatitude() {
        return _latitude;
    }

    public void setLatitude(long latitude) {
        _latitude = latitude;
    }

    public DateTime getSeenHereAt() {
        return _seenHereAt;
    }

    public void setSeenHereAt(DateTime seenHereAt) {
        _seenHereAt = seenHereAt;
    }
}
