package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by abdul on 19-Feb-2017.
 */

public class User {
    //********** This is backened database table columns decorated with the sepcial Azure annotation to serialize them***********
    @com.google.gson.annotations.SerializedName("id")
    private UUID _id;
    @com.google.gson.annotations.SerializedName("firstname")
    private String _firstName;
    @com.google.gson.annotations.SerializedName("lastname")
    private String _lastName;
    @com.google.gson.annotations.SerializedName("password")
    private String _password;
    @com.google.gson.annotations.SerializedName("email")
    private String _email;
    @com.google.gson.annotations.SerializedName("imagetitle")
    private String _imageTitle;
    @com.google.gson.annotations.SerializedName("joinedAt")
    private String _joinedAt;
    @com.google.gson.annotations.SerializedName("statusid")
    private int _status;



    public UUID getId() {
        return _id;
    }

    public void setId(UUID _id) {
        this._id = _id;
    }


    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }


    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }


    public String getPassword() {
        return _password;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }


    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }
    public int getStatus() {
        return _status;
    }

    public void setStatus(int _status) {
        this._status = _status;
    }
    public String getJoinedAt() {
        return _joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        _joinedAt = joinedAt;
    }
//    public byte[] getAvatar() {
//        return _avatar;
//    }
//
//    public void setAvatar(byte[] _avatar) {
//        this._avatar = _avatar;
//    }





    public String getImageTitle() {
        return this._imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this._imageTitle = imageTitle;
    }

    public enum UserStatus{ONLINE, BUSY, OFFLINE}

    public DateTime getJoinedAtDate(){
        return new DateTime();
    }
    public  static User CURRENT_USER = null;

    private String encryptPassword(String password){
       return null;
    }
    private String decryptPassword(String password){
        return null;
    }
    public User(){

    }

}
