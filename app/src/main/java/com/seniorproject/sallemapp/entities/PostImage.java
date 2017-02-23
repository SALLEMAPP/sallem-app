package com.seniorproject.sallemapp.entities;

import android.graphics.Bitmap;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostImage {
    private int _id;
    private int _postId;
    private Bitmap _image;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getPostId() {
        return _postId;
    }

    public void setPostId(int postId) {
        _postId = postId;
    }

    public Bitmap getImage() {
        return _image;
    }

    public void setImage(Bitmap image) {
        _image = image;
    }
}
