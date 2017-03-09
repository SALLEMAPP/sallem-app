package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainPost {
    private String _id;
    private String _postedAt;
    private String _subject;
    private String _userId;
    private String _activityId;
    private DomainUser _user;
    private Bitmap _image;
    private List<DomainComment> _comments;


    public DomainPost() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_postedAt() {
        return _postedAt;
    }

    public void set_postedAt(String _postedAt) {
        this._postedAt = _postedAt;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public String get_userId() {
        return _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String get_activityId() {
        return _activityId;
    }

    public void set_activityId(String _activityId) {
        this._activityId = _activityId;
    }

    public DomainUser get_user() {
        return _user;
    }

    public void set_user(DomainUser _user) {
        this._user = _user;
    }

    public Bitmap get_image() {
        return _image;
    }

    public void set_image(Bitmap _image) {
        this._image = _image;
    }

    public List<DomainComment> get_comments() {
        return _comments;
    }

    public void set_comments(List<DomainComment> _comments) {
        this._comments = _comments;
    }
}
