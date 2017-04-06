package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 08-Mar-2017.
 */

public class DomainUser implements Parcelable  {

    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String joinedAt;
    private String imageTitle;
    private int status;
    private Bitmap avatar;
    private double latitude;
    private double longitude;
    private boolean isSelected;
    public DomainUser(String id, String firstName, String lastName, String password, String email,
                      String joinedAt, String imageTitle, int status, Bitmap avatar,
                      double latitude, double longitude, boolean isSelected
                      ){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.joinedAt = joinedAt;
        this.imageTitle = imageTitle;
        this.status = status;
        this.avatar = avatar;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSelected = isSelected;
    }

    public String getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLasttName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getJoinedAt(){
        return joinedAt;
    }
    public String getPassowrd(){
        return password;
    }
    public String getImageTitle(){
        return imageTitle;
    }
    public int getStatus(){
        return status;
    }
    public Bitmap getAvatar(){
        return avatar;
    }
    public void  setAvatar(Bitmap userAvatar){
        this.avatar =userAvatar;
    }

    public static DomainUser CURRENT_USER;


    public boolean getIsSelected() {
        return isSelected;
    }
    public void setIselected(Boolean selected){
        this.isSelected = selected;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    protected DomainUser(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        password = in.readString();
        email = in.readString();
        joinedAt = in.readString();
        imageTitle = in.readString();
        status = in.readInt();
        avatar = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(joinedAt);
        dest.writeString(imageTitle);
        dest.writeInt(status);
        dest.writeValue(avatar);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DomainUser> CREATOR = new Parcelable.Creator<DomainUser>() {
        @Override
        public DomainUser createFromParcel(Parcel in) {
            return new DomainUser(in);
        }

        @Override
        public DomainUser[] newArray(int size) {
            return new DomainUser[size];
        }
    };



}
