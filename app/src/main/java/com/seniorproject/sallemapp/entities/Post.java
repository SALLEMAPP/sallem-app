package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Post {
    @SerializedName("id")
    private String _id;
    @SerializedName("postedAt")
    private String _postedAt;
    @SerializedName("subject")
    private String _subject;
    @SerializedName("userId")
    private String _userId;
    @SerializedName("imagePath")
    private String _imagePath;
    @SerializedName("activityId")
    private String _activityId;




    public String getId() {
        return _id;
    }

    public void setId(String postId) {
        _id = postId;
    }

    public String getPostedAt() {
        return _postedAt;
    }

    public void setPostedAt(String postedAt) {
        _postedAt = postedAt;
    }

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String content) {
        _subject = content;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public String getActivityId() {
        return _activityId;
    }

    public void setActivityId(String activityId) {
        _activityId = activityId;
    }


    public String get_imagePath() {
        return _imagePath;
    }

    public void set_imagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }
}
