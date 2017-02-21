package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

/**
 * Created by abdul on 19-Feb-2017.
 */

public class User {
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
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

    public Bitmap getAvatar() {
        return _avatar;
    }

    public void setAvatar(Bitmap _avatar) {
        this._avatar = _avatar;
    }

    public UserStatus getStatus() {
        return _status;
    }

    public void setStatus(UserStatus _status) {
        this._status = _status;
    }

    public enum UserStatus{ONLINE, BUSY, OFFLINE}
    private int _id;
    private String _firstName;
    private String _lastName;
    private String _password;
    private String _email;
    private Bitmap _avatar;
    private UserStatus _status;

    public  static User CURRENT_USER = null;

    private String encryptPassword(String password){
       return null;
    }
    private String decryptPassword(String password){
        return null;
    }

}
