package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class AbuseReport {
    private int _id;
    private int _typeId;
    private DateTime _reportedAt;
    private int _userId;
    private int _status;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getTypeId() {
        return _typeId;
    }

    public void setTypeId(int typeId) {
        _typeId = typeId;
    }

    public DateTime getReportedAt() {
        return _reportedAt;
    }

    public void setReportedAt(DateTime reportedAt) {
        _reportedAt = reportedAt;
    }

    public int getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        _userId = userId;
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
    }
}
