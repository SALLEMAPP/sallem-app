package com.seniorproject.sallemapp.entities;

/**
 * Created by Centeral on 2/21/2017.
 */

public class AbuseReportStatus {
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public enum Status{
        OPEN,
        CLOSED,
        REJECTED
    }
    private int _id;
    private Status _status;
}
