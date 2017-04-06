package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Centeral on 2/21/2017.
 */

public class ActivityDetail {
    @SerializedName("id")
    private String id;
    @SerializedName("activityId")
    private String activityId;
    @SerializedName("participantId")
    private String participantId;
    @SerializedName("participationStatus")
    private int participationStatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public int getParticipationStatus() {
        return participationStatus;
    }
    /*
    1- pending
    2-accept
    3-refused
     */
    public void setParticipationStatus(int participationStatus) {
        this.participationStatus = participationStatus;
    }
}
