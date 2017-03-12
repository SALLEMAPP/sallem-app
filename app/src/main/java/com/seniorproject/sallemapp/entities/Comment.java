package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Centeral on 2/21/2017.
 */

public class Comment {
    @SerializedName("id")
    private String _id;
    @SerializedName("commentedAt")
    private String _commentedAt;
    @SerializedName("userId")
    private String _userId;
    @SerializedName("subject")
    private String _subject;
    @SerializedName("postId")
    private String _postId;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_commentedAt() {
        return _commentedAt;
    }

    public void set_commentedAt(String _commentedAt) {
        this._commentedAt = _commentedAt;
    }

    public String get_userId() {
        return _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }



    public String get_postId() {
        return _postId;
    }

    public void set_postId(String _postId) {
        this._postId = _postId;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }
}
