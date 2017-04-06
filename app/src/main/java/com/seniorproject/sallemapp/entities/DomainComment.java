package com.seniorproject.sallemapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainComment implements Parcelable {

    private String _id;
    private String _commentedAt;
    private String _userId;
    private String _subject;
    private DomainUser _user;
    private String _posId;
    private DomainPost _post;


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

    public String get_posId() {
        return _posId;
    }

    public void set_posId(String _posId) {
        this._posId = _posId;
    }

    public DomainPost get_post() {
        return _post;
    }

    public void set_post(DomainPost _post) {
        this._post = _post;
    }
    public DomainComment(){}
    protected DomainComment(Parcel in) {
        _id = in.readString();
        _commentedAt = in.readString();
        _userId = in.readString();
        _subject = in.readString();
        _user = (DomainUser) in.readValue(DomainUser.class.getClassLoader());
        _posId = in.readString();
        _post = (DomainPost) in.readValue(DomainPost.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_commentedAt);
        dest.writeString(_userId);
        dest.writeString(_subject);
        dest.writeValue(_user);
        dest.writeString(_posId);
        dest.writeValue(_post);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DomainComment> CREATOR = new Parcelable.Creator<DomainComment>() {
        @Override
        public DomainComment createFromParcel(Parcel in) {
            return new DomainComment(in);
        }

        @Override
        public DomainComment[] newArray(int size) {
            return new DomainComment[size];
        }
    };
}
