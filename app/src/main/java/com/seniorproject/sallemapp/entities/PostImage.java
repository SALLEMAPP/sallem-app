package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Centeral on 2/21/2017.
 */

public class PostImage {
    @SerializedName("id")
    private String _id;
    @SerializedName("postId")
    private String _postId;
    @SerializedName("path")
    private String _path;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getPostId() {
        return _postId;
    }

    public void setPostId(String postId) {
        _postId = postId;
    }


    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }
    public PostImage(){

    }
}
