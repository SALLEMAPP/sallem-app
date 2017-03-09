package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by abdul on 19-Feb-2017.
 */

public class User {
    //********** This is backened database table columns decorated with the sepcial Azure annotation to serialize them***********
    @SerializedName("id")
    private String _id;
    @SerializedName("firstName")
    private String _firstName;
    @SerializedName("lastName")
    private String _lastName;
    @SerializedName("password")
    private String _password;
    @SerializedName("email")
    private String _email;
    @SerializedName("imageTitle")
    private String _imageTitle;
    @SerializedName("joinedAt")
    private String _joinedAt;
    @SerializedName("statusId")
    private int _status;



    public String getId() {
        return _id;
    }

    public void setId(String _id) {  this._id = _id;    }


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


    public String getEmail() {      return _email;    }

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
        _imageTitle = imageTitle;
    }

    public enum UserStatus{ONLINE, BUSY, OFFLINE}

    public DateTime getJoinedAtDate(){
        return new DateTime();
    }


    private String encryptPassword(String password){
       return null;
    }
    private String decryptPassword(String password){
        return null;
    }
    public User(){

    }
    @Override
    public boolean equals(Object o) {
        return o instanceof User && ((User) o)._id == _id;
    }

}
