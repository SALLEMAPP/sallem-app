package com.seniorproject.sallemapp.entities;

/**
 * Created by Centeral on 2/21/2017.
 */

public class ActivityDetails {
    private int _detailId;
    private int _activityId;
    private int _participantId;
    private int _participationStatus;


    public int getDetailId() {
        return _detailId;
    }

    public void setDetailId(int detailId) {
        _detailId = detailId;
    }

    public int getActivityId() {
        return _activityId;
    }

    public void setActivityId(int activityId) {
        _activityId = activityId;
    }

    public int getParticipantId() {
        return _participantId;
    }

    public void setParticipantId(int participantId) {
        _participantId = participantId;
    }

    public int getParticipationStatus() {
        return _participationStatus;
    }

    public void setParticipationStatus(int participationStatus) {
        _participationStatus = participationStatus;
    }
}
