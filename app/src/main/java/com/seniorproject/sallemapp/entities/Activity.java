package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Activity {
    private int _activityId;
    private int _organizerId;
    private String _reason;
    private DateTime _dateTime;
    private long _longitude;
    private long _latitude;

    public int getActivityId() {
        return _activityId;
    }

    public void setActivityId(int activityId) {
        _activityId = activityId;
    }

    public int getOrganizerId() {
        return _organizerId;
    }

    public void setOrganizerId(int organizerId) {
        _organizerId = organizerId;
    }

    public String getReason() {
        return _reason;
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public DateTime getDateTime() {
        return _dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        _dateTime = dateTime;
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
}
