package com.seniorproject.sallemapp.entities;

import org.joda.time.DateTime;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainComment {

    private String _id;
    private String _commentedAt;
    private String _userId;
    private String _subject;
    private DomainUser _user;


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





    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public DomainUser get_user() {
        return _user;
    }

    public void set_user(DomainUser _user) {
        this._user = _user;
    }
}
