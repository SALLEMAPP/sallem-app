package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Activity {
    @SerializedName("id")
    private String id;
    @SerializedName("organizerId")
    private String organizerId;
    @SerializedName("subject")
    private String subject;
    @SerializedName("heldOn")
    private String helodOn;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHelodOn() {
        return helodOn;
    }

    public void setHelodOn(String helodOn) {
        this.helodOn = helodOn;
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
}
