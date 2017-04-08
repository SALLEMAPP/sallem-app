package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 06-Apr-2017.
 */

public class Notify {
    @SerializedName("id")
    private String id;

    @SerializedName("sourceUser")
    private String sourceUser;

    @SerializedName("destUser")
    private String destUser;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("title")
    private String title;

    @SerializedName("subject")
    private String subject;

    @SerializedName("delivered")
    private boolean delivered;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getDestUser() {
        return destUser;
    }

    public void setDestUser(String destUser) {
        this.destUser = destUser;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
