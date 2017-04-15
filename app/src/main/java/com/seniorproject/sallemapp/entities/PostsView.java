package com.seniorproject.sallemapp.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdul on 14-Apr-2017.
 */

public class PostsView {
    @SerializedName("postId")
    private String postId;

    @SerializedName("postedAt")
    private String postedAt ;

    @SerializedName("subject")
    private String subject ;

    @SerializedName("posterId")
    private String posterId ;

    @SerializedName("imagePath")
    private String imagePath;

    @SerializedName("postImage")
    private String postImage ;

    @SerializedName("posterFirstName")
    private String posterFirstName ;

    @SerializedName("posterLastName")
    private String posterLastName ;

    @SerializedName("posterImage")
    private String posterImage ;

    @SerializedName("postComments")
    private CommentsView[] postComments ;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPosterFirstName() {
        return posterFirstName;
    }

    public void setPosterFirstName(String posterFirstName) {
        this.posterFirstName = posterFirstName;
    }

    public String getPosterLastName() {
        return posterLastName;
    }

    public void setPosterLastName(String posterLastName) {
        this.posterLastName = posterLastName;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public CommentsView[] getPostComments() {
        return postComments;
    }

    public void setPostComments(CommentsView[] postComments) {
        this.postComments = postComments;
    }
}
