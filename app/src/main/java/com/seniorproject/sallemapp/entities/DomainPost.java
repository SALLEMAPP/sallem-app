package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainPost implements Parcelable {
    private String _id;
    private String _postedAt;
    private String _subject;
    private String _userId;
    private String _activityId;
    private DomainUser _user;
    private String _imagePath;
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

    public String getImagePath(){
        return _imagePath;
    }
    public void  setImagePath(String path){
        _imagePath = path;
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


    protected DomainPost(Parcel in) {
        _id = in.readString();
        _postedAt = in.readString();
        _subject = in.readString();
        _userId = in.readString();
        _activityId = in.readString();
        _imagePath = in.readString();
        _user = (DomainUser) in.readValue(DomainUser.class.getClassLoader());
        _image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        if (in.readByte() == 0x01) {
            _comments = new ArrayList<DomainComment>();
            in.readList(_comments, DomainComment.class.getClassLoader());
        } else {
            _comments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_postedAt);
        dest.writeString(_subject);
        dest.writeString(_userId);
        dest.writeString(_activityId);
        dest.writeString(_imagePath);
        dest.writeValue(_user);
        dest.writeValue(_image);
        if (_comments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(_comments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DomainPost> CREATOR = new Parcelable.Creator<DomainPost>() {
        @Override
        public DomainPost createFromParcel(Parcel in) {
            return new DomainPost(in);
        }

        @Override
        public DomainPost[] newArray(int size) {
            return new DomainPost[size];
        }
    };
}
