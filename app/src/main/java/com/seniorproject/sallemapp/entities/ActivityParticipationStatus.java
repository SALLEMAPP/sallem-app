package com.seniorproject.sallemapp.entities;

/**
 * Created by Centeral on 2/21/2017.
 */

public class ActivityParticipationStatus {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParticipationStatus getStatus() {
        return _status;
    }

    public void setStatus(ParticipationStatus status) {
        _status = status;
    }

    public enum ParticipationStatus{
        IN,
        OUT
    }
    private int id;
    private ParticipationStatus _status;

}
